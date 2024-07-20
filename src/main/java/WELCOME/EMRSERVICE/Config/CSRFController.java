package WELCOME.EMRSERVICE.Config;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class CSRFController {

    @GetMapping("/csrf-token")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }
}