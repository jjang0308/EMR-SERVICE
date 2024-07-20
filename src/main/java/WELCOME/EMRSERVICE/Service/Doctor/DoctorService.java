package WELCOME.EMRSERVICE.Service.Doctor;

import WELCOME.EMRSERVICE.Domain.Doctor.Dept;
import WELCOME.EMRSERVICE.Domain.Doctor.Doctor;
import WELCOME.EMRSERVICE.Domain.Member.Member;
import WELCOME.EMRSERVICE.Dto.Doctor.DoctorDto;
import WELCOME.EMRSERVICE.Repository.Doctor.DeptRepository;
import WELCOME.EMRSERVICE.Repository.Doctor.DoctorRepository;
import WELCOME.EMRSERVICE.Service.Member.MemberPrincipalDetails;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class DoctorService implements UserDetailsService {

    private final DoctorRepository doctorRepository;
    private final DeptRepository deptRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DoctorService(DoctorRepository doctorRepository, DeptRepository deptRepository,  @Lazy BCryptPasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.deptRepository = deptRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String signUp(DoctorDto doctorDto) {
        // 중복된 아이디 체크
        if (doctorRepository.existsByDoctorLoginId(doctorDto.getDoctorLoginId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 비밀번호 암호화
        doctorDto.setDoctorPw(passwordEncoder.encode(doctorDto.getDoctorPw()));
        doctorDto.setRoles("ROLE_DOCTOR");

        // 부서 찾기
        Dept dept = deptRepository.findById(doctorDto.getDeptId())
                .orElseThrow(() -> new IllegalArgumentException("부서를 찾을 수 없습니다."));

        // 의사 정보 저장
        Doctor doctor = doctorDto.toEntity(dept);
        doctorRepository.save(doctor);

        // 저장한 사용자의 아이디를 반환
        return doctor.getDoctorLoginId();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Doctor doctorEntity = doctorRepository.findByDoctorLoginId(username);
        if (doctorEntity == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }
        System.out.println("Found user: " + doctorEntity.getDoctorLoginId());
        return new DoctorPrincipalDetails(doctorEntity);
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
        if (!passwordEncoder.matches(currentPassword, doctorEntity.getDoctorPw())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        String encPassword = passwordEncoder.encode(newPassword);
        doctorEntity.updatePassword(encPassword);
    }

    @Transactional
    public void deleteDoctor(String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = ((UserDetails) authentication.getPrincipal()).getUsername();

        Doctor doctorEntity = doctorRepository.findByDoctorLoginId(loginId);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(password, doctorEntity.getDoctorPw())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        doctorRepository.delete(doctorEntity);
    }
}
