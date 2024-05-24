package WELCOME.EMRSERVICE.Controller.Doctor;

import WELCOME.EMRSERVICE.Domain.Doctor.Dept;
import WELCOME.EMRSERVICE.Dto.Doctor.DoctorDto;
import WELCOME.EMRSERVICE.Repository.Doctor.DeptRepository;
import WELCOME.EMRSERVICE.Service.Doctor.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@AllArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    private final DeptRepository deptRepository;

    @GetMapping("/doctor/dashboard")
    public String dashboard() {
        return "/doctor/dashboard";
    }

    @GetMapping("/doctor/signup")
    public String signupFormDoctor(Model model) {
        List<Dept> depts = deptRepository.findAll();
        model.addAttribute("doctor", new DoctorDto());
        model.addAttribute("depts", depts);
        return "doctor/signupForm";
    }

    @PostMapping("/doctor/signup")
    public String signup(DoctorDto doctorDto) {
        doctorService.signUp(doctorDto);
        return "redirect:/";
    }
    @GetMapping("/doctor/login")
    public String loginPage() {
        // GET 요청에 대한 로그인 페이지를 반환합니다.
        return "/home/loginForm";
    }

    @PostMapping("/doctor/login")
    public String login(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getAuthorities().isEmpty()) {
            System.out.println("사용자가 인증되지 않았거나, 권한이 없습니다.");
        }
        System.out.println("authentication = " + authentication);

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            System.out.println("사용자의 권한: " + authority.getAuthority());
        }
        // 인증이 성공한 경우
        if (authentication != null && authentication.isAuthenticated()) {
            // 사용자의 권한 체크
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))) {
                // 의사 대시보드로 리다이렉트
                return "redirect:/doctor/dashboard";
            }
        }

        // 인증이 실패하거나 권한이 없는 경우
        redirectAttributes.addFlashAttribute("error", "의사 계정으로 로그인할 수 없습니다.");
        return "redirect:/doctor/login";


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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            // 인증 정보가 없거나 인증되지 않은 경우 처리
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
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
            // 에러 처리
            return "redirect:/error";
        }

        return "redirect:/";
    }

    @GetMapping("/doctor/delete")
    public String delete() {
        return "/doctor/delete";
    }
    @PostMapping("/doctor/delete")
    public String deleteMember(@RequestParam("password") String password, RedirectAttributes redirectAttributes) {
        try {
            doctorService.deleteDoctor(password);
            redirectAttributes.addFlashAttribute("success", "회원 탈퇴가 완료되었습니다.");
            return "redirect:/logout";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/doctor/delete"; // 탈퇴 실패 시 다시 탈퇴 페이지로 이동
        }
    }
}