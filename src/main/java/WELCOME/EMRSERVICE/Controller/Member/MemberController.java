package WELCOME.EMRSERVICE.Controller.Member;

import WELCOME.EMRSERVICE.Dto.Member.MemberDto;
import WELCOME.EMRSERVICE.Service.Member.MemberService;
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

@Controller
@AllArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/")
    public String index() {
        return "home/index";
    }


    @GetMapping("/member/choiceMember")
    public String choiceMember() {
        return "home/choiceMember_signup";
    }

    @GetMapping("/member/signup")
    public String signupForm(Model model) {
        model.addAttribute("member", new MemberDto());
        return "member/signupForm";
    }


    @PostMapping("/member/signup")
    public String signup(MemberDto memberDto) {
        memberService.signUp(memberDto);
        return "redirect:/";
    }
    @GetMapping("/member/dashboard")
    public String dashboard() {
        return "/member/dashboard";
    }
    @GetMapping("/member/login")
    public String loginPage() {
        // GET 요청에 대한 로그인 페이지를 반환합니다.
        return "/home/loginForm";
    }
    @PostMapping("/member/login")
    public String login(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // 인증 객체 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getAuthorities().isEmpty()) {
            System.out.println("사용자가 인증되지 않았거나, 권한이 없습니다.");

        }
        System.out.println("authentication = " + authentication);
        // 사용자의 권한을 확인합니다.
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            System.out.println("사용자의 권한: " + authority.getAuthority());
        }
        // 인증이 성공한 경우
        if (authentication != null && authentication.isAuthenticated()) {
            // 사용자의 권한 체크
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MEMBER"))) {
                // 의사 대시보드로 리다이렉트
                return "redirect:/member/dashboard";
            }
        }
        // 인증이 실패하거나 권한이 없는 경우
        redirectAttributes.addFlashAttribute("error", "의사 계정으로 로그인할 수 없습니다.");
        return "redirect:/member/login";
    }

    @GetMapping("/member/updatePassword")
    public String updatePassword(Model model) {
        return "/member/updatePassword";
    }

    @PostMapping("/member/updatePassword")
    public String updatePassword(MemberDto memberDto,
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
            memberService.modify(loginId, currentPassword, newPassword, confirmPassword);
        } catch (IllegalArgumentException e) {
            // 에러 처리
            return "redirect:/error";
        }

        return "redirect:/";
    }



    @GetMapping("/member/delete")
    public String delete() {
        return "/member/delete";
    }
    @PostMapping("/member/delete")
    public String deleteMember(@RequestParam("password") String password, RedirectAttributes redirectAttributes) {
        try {
            memberService.deleteMember(password);
            redirectAttributes.addFlashAttribute("success", "회원 탈퇴가 완료되었습니다.");
            return "redirect:/logout";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/member/delete"; // 탈퇴 실패 시 다시 탈퇴 페이지로 이동
        }
    }


}