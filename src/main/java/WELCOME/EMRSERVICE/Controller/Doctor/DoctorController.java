package WELCOME.EMRSERVICE.Controller.Doctor;

import WELCOME.EMRSERVICE.Domain.Doctor.Dept;
import WELCOME.EMRSERVICE.Dto.Doctor.DoctorDto;
import WELCOME.EMRSERVICE.Repository.Doctor.DeptRepository;
import WELCOME.EMRSERVICE.Service.Doctor.DeptService;
import WELCOME.EMRSERVICE.Service.Doctor.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/doctor")
@AllArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    private final DeptRepository deptRepository;
    private final DeptService deptService;

    @GetMapping("/dashboard")
    public ResponseEntity<String> dashboard() {
        return ResponseEntity.ok("Doctor Dashboard");
    }

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

    @GetMapping("/login")
    public ResponseEntity<String> loginPage() {
        return ResponseEntity.ok("Login Page");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getAuthorities().isEmpty()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            System.out.println("사용자의 권한: " + authority.getAuthority());
        }

        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
                return ResponseEntity.ok("Redirect to Doctor Dashboard");
            }
        }

        return ResponseEntity.status(403).body("Forbidden");
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
}
