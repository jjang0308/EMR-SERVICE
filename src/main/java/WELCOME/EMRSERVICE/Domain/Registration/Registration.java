package WELCOME.EMRSERVICE.Domain.Registration;

import WELCOME.EMRSERVICE.Domain.Member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity(name = "registration")
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registration_id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Member patient;

    private Long dept_id;
    private String symptom;
    private LocalDateTime treat_time;
    private LocalDateTime registration_time;
    private String doctor_comment;

    @Builder
    public Registration(Member patient, Long dept_id, String symptom, LocalDateTime treat_time, LocalDateTime registration_time, String doctor_comment) {
        this.patient = patient;
        this.dept_id = dept_id;
        this.symptom = symptom;
        this.treat_time = treat_time;
        this.registration_time = registration_time;
        this.doctor_comment = doctor_comment;
    }
}
