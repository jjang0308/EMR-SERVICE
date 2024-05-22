package WELCOME.EMRSERVICE.Domain.Doctor;

import WELCOME.EMRSERVICE.Domain.Appointment;
import lombok.*;

import javax.persistence.*;
import java.util.List;

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
    private Long role_id;
    private String roles;


    @Builder
    public Doctor(Long doctor_id, String doctor_name, String doctor_pw, String doctorLoginId,Long  role_id,String roles) {
        this.doctor_id = doctor_id;
        this.doctor_name = doctor_name;
        this.doctor_pw = doctor_pw;
        this.doctorLoginId = doctorLoginId;
        this.role_id=role_id;
        this.roles=roles;
    }

    public Doctor(Long doctor_id) {
        this.doctor_id = doctor_id;
    }

    public void updatePassword(String newPassword) {
        this.doctor_pw = newPassword;
    }

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;



}
