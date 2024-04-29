package WELCOME.EMRSERVICE.Controller.Member;

import WELCOME.EMRSERVICE.Dto.Member.MemberDto;
import WELCOME.EMRSERVICE.Service.Member.MemberService;
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
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/")
    public String index() {
        return "home/index";
    }

    @GetMapping("/member/choiceMember")
    public String choiceMember() {
        return "member/choiceMember";
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

    @GetMapping("/member/login")
    public String login() {
        return "member/loginForm";
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
        // 현재 사용자의 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 인증 정보에서 사용자의 로그인 ID 가져오기
        String loginId = ((UserDetails) authentication.getPrincipal()).getUsername();
        memberService.modify( loginId,currentPassword,newPassword, confirmPassword);
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
            return "redirect:/member/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/member/delete"; // 탈퇴 실패 시 다시 탈퇴 페이지로 이동
        }
    }


}

