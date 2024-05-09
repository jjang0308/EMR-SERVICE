package WELCOME.EMRSERVICE.Service.Member;

import WELCOME.EMRSERVICE.Domain.Member.Member;
import WELCOME.EMRSERVICE.Dto.Member.MemberDto;
import WELCOME.EMRSERVICE.Repository.Member.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {
    private MemberRepository memberRepository;

    // 회원가입
    @Transactional
    public String signUp(MemberDto memberDto) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberDto.setPatient_pw(passwordEncoder.encode(memberDto.getPatient_pw()));

        // 사용자가 의사일 경우 ROLE_DOCTOR 권한 부여
        memberDto.setRole_id(Long.valueOf("2"));

        Member member = memberDto.toEntity();
        memberRepository.save(member);

        // 저장한 사용자의 아이디를 반환
        return member.getPatientLoginId();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> memberWrapper = memberRepository.findByPatientLoginId(username);
        Member member = memberWrapper.orElseThrow(() -> new UsernameNotFoundException("해당하는 사용자가 없습니다."));

        List<GrantedAuthority> authorities = new ArrayList<>();

        return new User(member.getPatientLoginId(), member.getPatient_pw(), authorities);
    }


    @Transactional
    public void modify(String loginId,String currentPassword,String newPassword, String confirmPassword) {
        if (loginId == null) {
            throw new IllegalArgumentException("로그인 ID가 null입니다.");
        }
        Optional<Member> optionalMember = memberRepository.findByPatientLoginId(loginId);
        Member member = optionalMember.orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다. 로그인 ID: " + loginId));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(currentPassword, member.getPatient_pw())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        String encPassword = passwordEncoder.encode(newPassword);
        member.modify(encPassword);
    }

    @Transactional
    public void deleteMember(String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = ((UserDetails) authentication.getPrincipal()).getUsername();

        Member member = memberRepository.findByPatientLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(password, member.getPatient_pw())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        memberRepository.delete(member);
    }







}
