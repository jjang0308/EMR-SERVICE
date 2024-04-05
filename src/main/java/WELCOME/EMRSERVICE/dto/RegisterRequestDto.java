package WELCOME.EMRSERVICE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {
    private Long patient_id;
    private String patient_name;
    private String gender;
    private int age;
    private int height;
    private int weight;
    private String blood_type;
    private String patient_login_id;
    private String patient_pw;
}