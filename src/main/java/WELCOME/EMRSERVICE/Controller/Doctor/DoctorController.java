package WELCOME.EMRSERVICE.Controller.Doctor;

import WELCOME.EMRSERVICE.Config.JwtTokenUtil;
import WELCOME.EMRSERVICE.Domain.Doctor.Dept;
import WELCOME.EMRSERVICE.Domain.Doctor.Doctor;
import WELCOME.EMRSERVICE.Domain.Member.Member;
import WELCOME.EMRSERVICE.Dto.Doctor.DoctorDto;
import WELCOME.EMRSERVICE.Repository.Doctor.DeptRepository;
import WELCOME.EMRSERVICE.Repository.Doctor.DoctorRepository;
import WELCOME.EMRSERVICE.Service.Doctor.DeptService;
import WELCOME.EMRSERVICE.Service.Doctor.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/doctor")
@AllArgsConstructor
public class DoctorController {

    private AuthenticationManager authenticationManager;
    private final DoctorService doctorService;
    private final DeptRepository deptRepository;
    private final DeptService deptService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private DoctorRepository doctorRepository;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody DoctorDto doctorDto) {
        doctorService.signUp(doctorDto);
        return ResponseEntity.ok("Doctor signup successful");
    }

    @GetMapping("/departments")
    public ResponseEntity<List<Dept>> getDepartments() {
        List<Dept> dept = deptService.getAllDepts();
        return ResponseEntity.ok(dept);
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getDoctorsByDepartment(@RequestParam String department) {
        List<Doctor> doctors = doctorService.getDoctorsByDepartmentName(department);
        return ResponseEntity.ok(doctors);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        try {
            String doctorLoginId = loginData.get("doctorLoginId");
            String doctorPw = loginData.get("doctorPw");

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(doctorLoginId, doctorPw);
            Authentication authentication = authenticationManager.authenticate(authToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = doctorService.loadUserByUsername(doctorLoginId);
            if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
                String jwtToken = jwtTokenUtil.generateToken(userDetails);
                return ResponseEntity.ok().body(Map.of("message", "Redirect to doctor dashboard", "token", jwtToken, "role", "DOCTOR"));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot login with doctor account");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @GetMapping("/doctor-id")
    public ResponseEntity<Long> getDoctorId(@RequestParam String loginId) {
        Doctor doctor = doctorRepository.findByDoctorLoginId(loginId);
        if (doctor != null) {
            return ResponseEntity.ok(doctor.getDoctorId());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<DoctorDto> getDoctorProfile(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String loginId = authentication.getName();
        DoctorDto doctorDto = doctorService.getDoctorProfileByLoginId(loginId);

        if (doctorDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(doctorDto);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateDoctorProfile(@RequestBody DoctorDto doctorDto, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String loginId = authentication.getName();
        try {
            doctorService.updateDoctorProfile(loginId, doctorDto);  // 정보 수정 서비스 호출
            return ResponseEntity.ok("의사 정보가 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("정보 수정 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody DoctorDto doctorDto, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        String loginId = authentication.getName();

        try {
            doctorService.updatePassword(loginId, doctorDto.getDoctorPw(), doctorDto.getNewPassword());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.ok("Password updated successfully");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteDoctor(@RequestBody Map<String, String> requestBody) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        String loginId;

        if (principal instanceof UserDetails) {
            loginId = ((UserDetails) principal).getUsername();
        } else {
            loginId = principal.toString();
        }

        String password = requestBody.get("password");

        try {
            // 비밀번호 확인 후 탈퇴 처리
            doctorService.deleteDoctor(loginId, password);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.ok("Account deleted successfully");
    }
}
