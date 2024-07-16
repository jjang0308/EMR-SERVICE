package WELCOME.EMRSERVICE.Service.OAuth;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomLogoutHandler implements LogoutHandler {
    private final JwtProvider jwtProvider;

    public CustomLogoutHandler(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = jwtProvider.resolveToken(request);
        if (token != null) {
            jwtProvider.invalidateToken(token);
        }
    }
}
