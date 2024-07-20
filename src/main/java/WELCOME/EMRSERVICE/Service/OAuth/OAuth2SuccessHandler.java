package WELCOME.EMRSERVICE.Service.OAuth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

//        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
//        String userId = oAuth2User.getName();
//        String token = jwtProvider.create(userId);

//        response.sendRedirect("http://localhost:3000/auth/oauth-response/" + token + "/3600");

        String targetUrl = "http://localhost:3000/member/dashboard";

        getRedirectStrategy().sendRedirect(request, response, targetUrl);

    }
}
