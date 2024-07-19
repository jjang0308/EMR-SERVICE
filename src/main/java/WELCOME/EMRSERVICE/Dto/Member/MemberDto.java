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
    private String patientName;
    private String gender;
    private int age;
    private int weight;
    private int height;
    private String bloodType;
    private String patientPw;
    private String patientLoginId;
    private String roles;

    public Member toEntity() {
        return Member.builder()
                .patient_id(patient_id)
                .patientName(patientName)
                .gender(gender)
                .age(age)
                .weight(weight)
                .height(height)
                .bloodType(bloodType)
                .patientPw(patientPw)
                .patientLoginId(patientLoginId)
                .roles(roles)
                .build();
    }

    @Builder
    public MemberDto(Long patient_id, String patientName, String gender, int age, int height, int weight, String bloodType, String patientLoginId, String patientPw,String roles) {
        this.patient_id = patient_id;
        this.patientName = patientName;
        this.gender = gender;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.bloodType = bloodType;
        this.patientPw = patientPw;
        this.patientLoginId = patientLoginId;
        this.roles=roles;

    }



}

