package WELCOME.EMRSERVICE.Controller.Member;

import WELCOME.EMRSERVICE.Dto.Member.MemberDto;
import WELCOME.EMRSERVICE.Dto.Registration.RegistrationDto;
import WELCOME.EMRSERVICE.Service.Member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/member")
@AllArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("Welcome to the Home Page");
    }

    @GetMapping("/choiceMember")
    public ResponseEntity<String> choiceMember() {
        return ResponseEntity.ok("Choice Member Signup Page");
    }

    @GetMapping("/signup")
    public ResponseEntity<MemberDto> signupForm() {
        return ResponseEntity.ok(new MemberDto());
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody Map<String, Object> request) {
        ObjectMapper objectMapper = new ObjectMapper();
        MemberDto memberDto = objectMapper.convertValue(request.get("memberDto"), MemberDto.class);
        RegistrationDto registrationDto = objectMapper.convertValue(request.get("registrationDto"), RegistrationDto.class);

        memberService.signUp(memberDto, registrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Member signed up successfully");
    }


    @GetMapping("/dashboard")
    public ResponseEntity<String> dashboard() {
        return ResponseEntity.ok("Member Dashboard");
    }

    @GetMapping("/login")
    public ResponseEntity<String> loginPage() {
        return ResponseEntity.ok("Login Page");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getAuthorities().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated or has no authorities");
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            System.out.println("User authority: " + authority.getAuthority());
        }

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MEMBER"))) {
            return ResponseEntity.ok("Redirect to member dashboard");
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot login with member account");
    }

    @GetMapping("/updatePassword")
    public ResponseEntity<String> updatePasswordForm() {
        return ResponseEntity.ok("Update Password Page");
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody MemberDto memberDto,
                                                 @RequestParam("currentPassword") String currentPassword,
                                                 @RequestParam("newPassword") String newPassword,
                                                 @RequestParam("confirmPassword") String confirmPassword) {
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

        try {
            memberService.modify(loginId, currentPassword, newPassword, confirmPassword);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.ok("Password updated successfully");
    }

    @GetMapping("/delete")
    public ResponseEntity<String> deleteForm() {
        return ResponseEntity.ok("Delete Member Page");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteMember(@RequestParam("password") String password) {
        try {
            memberService.deleteMember(password);
            return ResponseEntity.ok("Member deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
