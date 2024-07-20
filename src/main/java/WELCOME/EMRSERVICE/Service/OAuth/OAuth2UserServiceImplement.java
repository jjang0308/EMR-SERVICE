package WELCOME.EMRSERVICE.Service.OAuth;

import WELCOME.EMRSERVICE.Domain.Member.Member;
import WELCOME.EMRSERVICE.Repository.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImplement extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);
        String oauthClientName = request.getClientRegistration().getClientName();

        Member member = null;
        String patientLoginId = null;
        String nickname = null;
        String gender = null;

        // 카카오 로그인 시
        if (oauthClientName.equalsIgnoreCase("kakao")) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            nickname = (String) ((Map<String, Object>) attributes.get("properties")).get("nickname");

            patientLoginId = "kakao_" + attributes.get("id");
            gender = (String) kakaoAccount.get("gender");
            member = memberRepository.findByPatientLoginId(patientLoginId);
            if (member == null) {
                member = new Member(patientLoginId, "kakao", nickname, gender);
                member.setPatientPw(passwordEncoder.encode(member.getPatientPw()));
                memberRepository.save(member);
            }

            return new DefaultOAuth2User(
                    Collections.singletonList(new SimpleGrantedAuthority(member.getRoles())),
                    attributes,
                    "id"
            );
        }

        // 네이버 로그인 시
        if (oauthClientName.equalsIgnoreCase("naver")) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            if (response != null) {
                patientLoginId = "naver_" + response.get("id");
                nickname = (String) response.get("nickname");
                gender = (String) response.get("gender");
                member = memberRepository.findByPatientLoginId(patientLoginId);
                if (member == null) {
                    member = new Member(patientLoginId, "naver", nickname, gender);
                    member.setPatientPw(passwordEncoder.encode(member.getPatientPw()));
                    memberRepository.save(member);
                }

                return new DefaultOAuth2User(
                        Collections.singletonList(new SimpleGrantedAuthority(member.getRoles())),
                        response,
                        "id"
                );
            } else {
                throw new OAuth2AuthenticationException("Failed to fetch user details from Naver");
            }
        }

        // 구글 로그인 시
        if (oauthClientName.equalsIgnoreCase("google")) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            patientLoginId = "google_" + attributes.get("sub");
            nickname = (String) attributes.get("name");
            gender = ""; // 구글에서 성별을 가져오려면 추가 설정이 필요할 수 있습니다.
            member = memberRepository.findByPatientLoginId(patientLoginId);
            if (member == null) {
                member = new Member(patientLoginId, "google", nickname, gender);
                member.setPatientPw(passwordEncoder.encode("password"));
                memberRepository.save(member);
            }

            return new DefaultOAuth2User(
                    Collections.singletonList(new SimpleGrantedAuthority(member.getRoles())),
                    attributes,
                    "sub"
            );
        }

        throw new OAuth2AuthenticationException("OAuth2 login failed for client: " + oauthClientName);
    }
}
