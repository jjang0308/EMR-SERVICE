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



    @GetMapping("/updatePassword")
    public ResponseEntity<String> updatePasswordPage() {
        return ResponseEntity.ok("Update Password Page");
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody DoctorDto doctorDto,
                                                 @RequestParam("currentPassword") String currentPassword,
                                                 @RequestParam("newPassword") String newPassword,
                                                 @RequestParam("confirmPassword") String confirmPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Object principal = authentication.getPrincipal();
        String loginId;

        if (principal instanceof UserDetails) {
            loginId = ((UserDetails) principal).getUsername();
        } else {
            loginId = principal.toString();
        }

        try {
            doctorService.modify(loginId, currentPassword, newPassword, confirmPassword);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok("Password updated successfully");
    }

    @GetMapping("/delete")
    public ResponseEntity<String> deletePage() {
        return ResponseEntity.ok("Delete Page");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteMember(@RequestParam("password") String password) {
        try {
            doctorService.deleteDoctor(password);
            return ResponseEntity.ok("Doctor account deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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
}
