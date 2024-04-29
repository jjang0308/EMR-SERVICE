package WELCOME.EMRSERVICE.Controller.MainController;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class MainController {

    @GetMapping("/home/loginForm")
    public String Main() {
        return "/home/choiceMember_loginForm";
    }

    @GetMapping("/home/choiceMember")
    public String Main1() {
        return "/home/choiceMember_signup";
    }
}
