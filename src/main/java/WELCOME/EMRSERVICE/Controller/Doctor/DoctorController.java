package WELCOME.EMRSERVICE.Controller.Doctor;

import WELCOME.EMRSERVICE.Dto.Doctor.DoctorDto;
import WELCOME.EMRSERVICE.Service.Doctor.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping("/doctor/signup")
    public String signupFormDoctor(Model model) {
        model.addAttribute("doctor", new DoctorDto());
        return "doctor/signupForm";
    }

    @PostMapping("/doctor/signup")
    public String signup(DoctorDto doctorDto) {
        doctorService.signUp(doctorDto);
        return "redirect:/";
    }
}
