package WELCOME.EMRSERVICE.Controller;

import WELCOME.EMRSERVICE.Dto.MemberDto;
import WELCOME.EMRSERVICE.Service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/")
    public String index() {
        return "home/index";
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


}

