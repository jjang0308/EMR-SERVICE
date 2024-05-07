package WELCOME.EMRSERVICE.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ADMIN("ROLE_ADMIN"),
    MEMBER("ROLE_MEMBER"),
    DOCTOR("ROLE_DOCTOR");

    private String value;
}

