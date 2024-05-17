package WELCOME.EMRSERVICE.Service.Doctor;

import WELCOME.EMRSERVICE.Domain.Doctor.Doctor;
import WELCOME.EMRSERVICE.Dto.Doctor.DoctorDto;
import WELCOME.EMRSERVICE.Repository.Doctor.DoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class DoctorService implements UserDetailsService {

    private DoctorRepository doctorRepository;

    @Transactional
    public String signUp(DoctorDto doctorDto) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        doctorDto.setDoctor_pw(passwordEncoder.encode(doctorDto.getDoctor_pw()));

        doctorDto.setRoles("ROLE_DOCTOR");

        Doctor doctor = doctorDto.toEntity();
        doctorRepository.save(doctor);

        // 저장한 사용자의 아이디를 반환
        return doctor.getDoctorLoginId();
    }



    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      return null;
    }



    @Transactional
    public void modify(String loginId, String currentPassword, String newPassword, String confirmPassword) {
        if (loginId == null) {
            throw new IllegalArgumentException("로그인 ID가 null입니다.");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
        }

        Doctor doctorEntity = doctorRepository.findByDoctorLoginId(loginId);
        if (doctorEntity == null) {
            throw new IllegalArgumentException("회원을 찾을 수 없습니다. 로그인 ID: " + loginId);
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(currentPassword, doctorEntity.getDoctor_pw())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        String encPassword = passwordEncoder.encode(newPassword);
        doctorEntity.updatePassword(encPassword);
    }
//    @Transactional
//    public void deleteMember(String password) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String loginId = ((UserDetails) authentication.getPrincipal()).getUsername();
//
//        Doctor member = doctorRepository.findByDoctorLoginId(loginId)
//                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
//
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        if (!passwordEncoder.matches(password, member.getDoctor_pw())) {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
//
//        doctorRepository.delete(member);
//    }
}
