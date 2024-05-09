package WELCOME.EMRSERVICE.Dto.Doctor;

import WELCOME.EMRSERVICE.Domain.Doctor.Doctor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DoctorDto {
    private Long doctor_id;
    private String doctor_name;
    private String doctor_login_id;
    private String doctor_pw;
    private Long role_id;



    public Doctor toEntity() {
        return Doctor.builder()
                .doctor_id(doctor_id)
                .doctor_name(doctor_name)
                .doctor_pw(doctor_pw)
                .doctorLoginId(doctor_login_id)
                .role_id(role_id)
                .build();
    }

    @Builder
    public DoctorDto(Long doctor_id, String doctor_name, String doctor_login_id, String doctor_pw,Long role_id) {
        this.doctor_id = doctor_id;
        this.doctor_name = doctor_name;
        this.doctor_pw = doctor_pw;
        this.doctor_login_id = doctor_login_id;
        this.role_id=role_id;

    }



}
