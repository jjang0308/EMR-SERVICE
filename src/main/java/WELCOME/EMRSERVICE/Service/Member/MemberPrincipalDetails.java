package WELCOME.EMRSERVICE.Service.Member;

import WELCOME.EMRSERVICE.Domain.Member.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class MemberPrincipalDetails implements UserDetails {

    private Member member;

    public MemberPrincipalDetails(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getRoles();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return member.getPatientPw();
    }

    @Override
    public String getUsername() {
        return member.getPatientLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
