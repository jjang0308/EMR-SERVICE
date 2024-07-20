package WELCOME.EMRSERVICE.Controller.MainController;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/main")
@CrossOrigin(origins = "http://localhost:3000")
public class MainController {

    @GetMapping("/loginForm")
    public ResponseEntity<String> choiceMember_loginForm() {
        return ResponseEntity.ok("Login Form");
    }

    @GetMapping("/choiceMember")
    public ResponseEntity<String> choiceMember() {
        return ResponseEntity.ok("Choice Member Signup");
    }

    @GetMapping("/dashboard")
    public ResponseEntity<String> dashboard() {
        return ResponseEntity.ok("Dashboard");
    }
}
