package WELCOME.EMRSERVICE.Dto.Registration;

import WELCOME.EMRSERVICE.Domain.Member.Member;
import WELCOME.EMRSERVICE.Domain.Registration.Registration;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RegistrationDto {
    private Long registration_id;
    private Long doctor_id;
    private Long patient_id;
    private Long dept_id;
    private String symptom;
    private LocalDateTime treat_time;
    private LocalDateTime registration_time;
    private String doctor_comment;

    public Registration toEntity( Member patient) {
        return Registration.builder()
                .patient(patient)
                .dept_id(dept_id)
                .symptom(symptom)
                .treat_time(treat_time)
                .registration_time(registration_time)
                .doctor_comment(doctor_comment)
                .build();
    }

    @Builder
    public RegistrationDto(Long registration_id, Long doctor_id, Long patient_id, Long dept_id, String symptom, LocalDateTime treat_time, LocalDateTime registration_time, String doctor_comment) {
        this.registration_id = registration_id;
        this.doctor_id = doctor_id;
        this.patient_id = patient_id;
        this.dept_id = dept_id;
        this.symptom = symptom;
        this.treat_time = treat_time;
        this.registration_time = registration_time;
        this.doctor_comment = doctor_comment;
    }
}
