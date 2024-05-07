package WELCOME.EMRSERVICE.Domain.Doctor;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Getter @Setter
@Entity(name = "doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctor_id;
    private String doctor_name;
    private String doctorLoginId;
    private String doctor_pw;
    private String role;


    @Builder
    public Doctor(Long doctor_id, String doctor_name, String doctor_pw, String doctorLoginId,String role) {
        this.doctor_id = doctor_id;
        this.doctor_name = doctor_name;
        this.doctor_pw = doctor_pw;
        this.doctorLoginId = doctorLoginId;
        this.role=role;
    }

    public void modify(String patient_pw) {
        this.doctor_pw = doctor_pw;
    }


}
