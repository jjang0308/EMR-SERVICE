package WELCOME.EMRSERVICE.Controller.Member;

import WELCOME.EMRSERVICE.Config.JwtTokenUtil;
import WELCOME.EMRSERVICE.Domain.Member.Member;
import WELCOME.EMRSERVICE.Dto.Member.MemberDto;
import WELCOME.EMRSERVICE.Repository.Member.MemberRepository;
import WELCOME.EMRSERVICE.Service.Member.MemberService;
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
import java.util.Map;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/member")
@AllArgsConstructor
public class MemberController {


    private AuthenticationManager authenticationManager;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("Welcome to the Home Page");
    }

    @GetMapping("/choiceMember")
    public ResponseEntity<String> choiceMember() {
        return ResponseEntity.ok("Choice Member Signup Page");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody Member member) {
        try {
            memberService.signUp(member);
            return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 중 오류가 발생했습니다.");
        }
    }


    @GetMapping("/dashboard")
    public ResponseEntity<String> dashboard() {
        return ResponseEntity.ok("Member Dashboard");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        try {
            String patientLoginId = loginData.get("patientLoginId");
            String patientPw = loginData.get("patientPw");

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(patientLoginId, patientPw);
            Authentication authentication = authenticationManager.authenticate(authToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = memberService.loadUserByUsername(patientLoginId);
            if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MEMBER"))) {
                String jwtToken = jwtTokenUtil.generateToken(userDetails);
                return ResponseEntity.ok().body(Map.of("message", "Redirect to member dashboard", "token", jwtToken, "role", "MEMBER"));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot login with member account");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
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

    @GetMapping("/patient-id")
    public ResponseEntity<Long> getPatientId(@RequestParam String loginId) {
        Member member = memberRepository.findByPatientLoginId(loginId);
        if (member != null) {
            return ResponseEntity.ok(member.getPatientId());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
