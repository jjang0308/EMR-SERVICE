package WELCOME.EMRSERVICE.Controller.Doctor;

import WELCOME.EMRSERVICE.Dto.Doctor.DoctorDto;
import WELCOME.EMRSERVICE.Service.Doctor.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping("/doctor/login")
    public String login() {
        return "doctor/dashboard";
    }

    @GetMapping("/doctor/updatePassword")
    public String updatePassword(Model model) {
        return "/doctor/updatePassword";
    }

    @PostMapping("/doctor/updatePassword")
    public String updatePassword(DoctorDto doctorDto,
                                 @RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword) {
        // 현재 사용자의 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 인증 정보에서 사용자의 로그인 ID 가져오기
        String loginId = ((UserDetails) authentication.getPrincipal()).getUsername();
        doctorService.modify( loginId,currentPassword,newPassword, confirmPassword);
        return "redirect:/";
    }

    @GetMapping("/doctor/delete")
    public String delete() {
        return "/doctor/delete";
    }
    @PostMapping("/doctor/delete")
    public String deleteMember(@RequestParam("password") String password, RedirectAttributes redirectAttributes) {
        try {
            doctorService.deleteMember(password);
            redirectAttributes.addFlashAttribute("success", "회원 탈퇴가 완료되었습니다.");
            return "redirect:/doctor/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/doctor/delete"; // 탈퇴 실패 시 다시 탈퇴 페이지로 이동
        }
    }
}
