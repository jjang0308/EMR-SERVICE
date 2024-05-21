package WELCOME.EMRSERVICE.Service;

import lombok.Getter;


@Getter
public enum Role {
    ADMIN("ROLE_ADMIN"),
    MEMBER("ROLE_MEMBER"),
    DOCTOR("ROLE_DOCTOR");

    private String role;

    Role(String role) {
        this.role = role;
    }
}

