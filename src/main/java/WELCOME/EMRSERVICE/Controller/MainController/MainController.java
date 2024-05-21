package WELCOME.EMRSERVICE.Controller.MainController;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class MainController {

    @GetMapping("/home/loginForm")
    public String choiceMember_loginForm() {
        return "/home/loginForm";
    }

    @GetMapping("/home/choiceMember")
    public String choiceMember() {
        return "/home/choiceMember_signup";
    }


    @GetMapping("/home/dashboard")
    public String dashboard() {
        return "/home/dashboard";
    }




}
