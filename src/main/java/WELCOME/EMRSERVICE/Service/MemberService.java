package WELCOME.EMRSERVICE.Service;

import WELCOME.EMRSERVICE.Domain.Member;
import WELCOME.EMRSERVICE.Dto.MemberDto;
import WELCOME.EMRSERVICE.Repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

        // password를 암호화 한 뒤 dp에 저장

        return memberRepository.save(memberDto.toEntity()).getPatientLoginId();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 로그인을 하기 위해 가입된 user정보를 조회하는 메서드
        Optional<Member> memberWrapper = memberRepository.findByPatientLoginId(username);
        Member member = memberWrapper.get();

        List<GrantedAuthority> authorities = new ArrayList<>();

        // 여기서는 간단하게 username이 'admin'이면 admin권한 부여
        if("admin".equals(username)){
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        }

        // 아이디, 비밀번호, 권한리스트를 매개변수로 User를 만들어 반환해준다.
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
