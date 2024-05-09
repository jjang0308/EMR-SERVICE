package WELCOME.EMRSERVICE.Service.Doctor;

import WELCOME.EMRSERVICE.Domain.Doctor.Doctor;
import WELCOME.EMRSERVICE.Dto.Doctor.DoctorDto;
import WELCOME.EMRSERVICE.Repository.Doctor.DoctorRepository;
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
import java.util.*;

@Service
@AllArgsConstructor
public class DoctorService implements UserDetailsService {

    private DoctorRepository doctorRepository;
    @Transactional
    public String signUp(DoctorDto doctorDto) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        doctorDto.setDoctor_pw(passwordEncoder.encode(doctorDto.getDoctor_pw()));

        // 사용자가 의사일 경우 ROLE_DOCTOR 권한 부여
        doctorDto.setRole_id(Long.valueOf("3"));

        Doctor doctor = doctorDto.toEntity();
        doctorRepository.save(doctor);

        // 저장한 사용자의 아이디를 반환
        return doctor.getDoctorLoginId();
    }



    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Doctor> doctorWrapper = doctorRepository.findByDoctorLoginId(username);
        Doctor doctor = doctorWrapper.orElseThrow(() -> new UsernameNotFoundException("해당하는 사용자가 없습니다."));

        // 사용자의 권한 정보 설정
        String authorities = ""; // 데이터베이스에서 사용자의 권한 정보를 가져오는 코드 추가 필요
        List<GrantedAuthority> authoritiesList = getAuthorities(authorities);

        return new User(doctor.getDoctorLoginId(), doctor.getDoctor_pw(), authoritiesList);
    }

    public List<GrantedAuthority> getAuthorities(String authorities) {
        List<GrantedAuthority> list = new ArrayList<>();

        if (!"".equals(authorities)) {
            for (String str : Arrays.asList(authorities.split(","))) {
                list.add(new SimpleGrantedAuthority(str));
            }
        }

        return list;
    }


    @Transactional
    public void modify(String loginId,String currentPassword,String newPassword, String confirmPassword) {
        if (loginId == null) {
            throw new IllegalArgumentException("로그인 ID가 null입니다.");
        }
        Optional<Doctor> optionalMember = doctorRepository.findByDoctorLoginId(loginId);
        Doctor doctor = optionalMember.orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다. 로그인 ID: " + loginId));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(currentPassword, doctor.getDoctor_pw())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        String encPassword = passwordEncoder.encode(newPassword);
        doctor.modify(encPassword);
    }

    @Transactional
    public void deleteMember(String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = ((UserDetails) authentication.getPrincipal()).getUsername();

        Doctor member = doctorRepository.findByDoctorLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(password, member.getDoctor_pw())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        doctorRepository.delete(member);
    }
}
