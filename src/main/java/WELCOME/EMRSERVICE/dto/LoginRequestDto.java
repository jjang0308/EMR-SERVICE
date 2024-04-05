package WELCOME.EMRSERVICE.dto;

import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String patient_login_id;
    private String patient_pw;
}