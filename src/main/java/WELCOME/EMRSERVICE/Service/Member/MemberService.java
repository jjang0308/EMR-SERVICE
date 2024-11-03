package WELCOME.EMRSERVICE.Service.Member;

import WELCOME.EMRSERVICE.Domain.Member.Member;
import WELCOME.EMRSERVICE.Dto.Member.MemberDto;
import WELCOME.EMRSERVICE.Repository.Doctor.DoctorRepository;
import WELCOME.EMRSERVICE.Repository.Member.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {
    private MemberRepository memberRepository;
    private DoctorRepository doctorRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member memberEntity = memberRepository.findByPatientLoginId(username);
        if (memberEntity == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }
        System.out.println("Found user: " + memberEntity.getPatientLoginId());
        return new MemberPrincipalDetails(memberEntity);
    }

    @Transactional
    public String signUp(Member member) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        member.setPatientPw(passwordEncoder.encode(member.getPatientPw()));
        member.setRoles("ROLE_MEMBER");
        memberRepository.save(member);
        return member.getPatientLoginId();
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
        if (!passwordEncoder.matches(currentPassword, memberEntity.getPatientPw())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        String encPassword = passwordEncoder.encode(newPassword);
        memberEntity.updatePassword(encPassword);
    }
    public void deleteMember(String loginId, String rawPassword) {
        // 로그인 ID로 회원 정보 찾기
        Member member = memberRepository.findByPatientLoginId(loginId);

        if (member == null) {
            throw new IllegalArgumentException("회원이 존재하지 않습니다.");
        }
        if (!passwordEncoder.matches(rawPassword, member.getPatientPw())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        memberRepository.delete(member);
    }

    public Member findByPatientLoginId(String patientLoginId) {
        return memberRepository.findByPatientLoginId(patientLoginId);
    }

    @Transactional
    public void updateMember(MemberDto memberDto) {
        // 로그인된 사용자의 ID에 따라 해당 사용자의 정보를 불러옵니다.
        Optional<Member> optionalMember = memberRepository.findById(memberDto.getPatientId());

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setPatientName(memberDto.getPatientName());
            member.setGender(memberDto.getGender());
            member.setAge(memberDto.getAge());
            member.setWeight(memberDto.getWeight());
            member.setHeight(memberDto.getHeight());
            member.setBloodType(memberDto.getBloodType());

            // 변경된 정보를 저장합니다.
            memberRepository.save(member);
        } else {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
    }

}
