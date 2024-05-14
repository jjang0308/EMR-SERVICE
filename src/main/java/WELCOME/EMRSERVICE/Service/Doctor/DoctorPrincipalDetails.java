package WELCOME.EMRSERVICE.Service.Doctor;

import WELCOME.EMRSERVICE.Domain.Doctor.Doctor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class DoctorPrincipalDetails implements UserDetails {
    private Doctor doctor;

    public DoctorPrincipalDetails(Doctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return doctor.getRoles();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return doctor.getDoctor_pw();
    }

    @Override
    public String getUsername() {
        return null;
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
