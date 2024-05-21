package WELCOME.EMRSERVICE.Service.Member;

import WELCOME.EMRSERVICE.Domain.Doctor.Doctor;
import WELCOME.EMRSERVICE.Domain.Member.Member;
import WELCOME.EMRSERVICE.Dto.Member.MemberDto;
import WELCOME.EMRSERVICE.Dto.Registration.RegistrationDto;
import WELCOME.EMRSERVICE.Repository.Doctor.DoctorRepository;
import WELCOME.EMRSERVICE.Repository.Member.MemberRepository;
import WELCOME.EMRSERVICE.Repository.Registration.RegistrationRepository;
import WELCOME.EMRSERVICE.Service.Doctor.DoctorPrincipalDetails;
import WELCOME.EMRSERVICE.Service.Role;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {
    private MemberRepository memberRepository;
    private DoctorRepository doctorRepository;
    private RegistrationRepository registrationRepository;

    // 회원가입
    @Transactional
    public String signUp(MemberDto memberDto,RegistrationDto registrationDto) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberDto.setPatient_pw(passwordEncoder.encode(memberDto.getPatient_pw()));
        memberDto.setRoles("ROLE_MEMBER");

        Member member = memberDto.toEntity();
        memberRepository.save(member);

        // 저장한 사용자의 아이디를 반환
        return member.getPatientLoginId();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member memberEntity = memberRepository.findByPatientLoginId(username);
        Doctor doctorEntity = doctorRepository.findByDoctorLoginId(username);

        if (memberEntity != null) { // 환자인 경우
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(Role.MEMBER.getRole()));
            return new MemberPrincipalDetails(memberEntity);
        } else if (doctorEntity != null) { // 의사인 경우
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(Role.DOCTOR.getRole()));
            return new DoctorPrincipalDetails(doctorEntity);
        } else { // 사용자가 존재하지 않는 경우
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }
    }



    @Transactional
    public void modify(String loginId, String currentPassword, String newPassword, String confirmPassword) {
        if (loginId == null) {
            throw new IllegalArgumentException("로그인 ID가 null입니다.");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
        }

        Member memberEntity = memberRepository.findByPatientLoginId(loginId);
        if (memberEntity == null) {
            throw new IllegalArgumentException("회원을 찾을 수 없습니다. 로그인 ID: " + loginId);
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(currentPassword, memberEntity.getPatient_pw())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        String encPassword = passwordEncoder.encode(newPassword);
        memberEntity.updatePassword(encPassword);
    }




    @Transactional
    public void deleteMember(String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = ((UserDetails) authentication.getPrincipal()).getUsername();

        Member memberEntity = memberRepository.findByPatientLoginId(loginId);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(password, memberEntity.getPatient_pw())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        memberRepository.delete(memberEntity);
    }







}
