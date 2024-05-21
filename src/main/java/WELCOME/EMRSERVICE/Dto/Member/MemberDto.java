package WELCOME.EMRSERVICE.Dto.Member;

import WELCOME.EMRSERVICE.Domain.Member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MemberDto {
    private Long patient_id;
    private String patient_name;
    private String gender;
    private int age;
    private int weight;
    private int height;
    private String blood_type;
    private String patient_pw;
    private String patient_login_id;
    private String roles;

    public Member toEntity() {
        return Member.builder()
                .patient_id(patient_id)
                .patient_name(patient_name)
                .gender(gender)
                .age(age)
                .weight(weight)
                .height(height)
                .blood_type(blood_type)
                .patient_pw(patient_pw)
                .patientLoginId(patient_login_id)
                .roles(roles)
                .build();
    }

    @Builder
    public MemberDto(Long patient_id, String patient_name, String gender, int age, int height, int weight, String blood_type, String patient_login_id, String patient_pw,String roles) {
        this.patient_id = patient_id;
        this.patient_name = patient_name;
        this.gender = gender;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.blood_type = blood_type;
        this.patient_pw = patient_pw;
        this.patient_login_id = patient_login_id;
        this.roles=roles;

    }



}

