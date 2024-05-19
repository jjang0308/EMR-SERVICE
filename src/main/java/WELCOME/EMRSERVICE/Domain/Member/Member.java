package WELCOME.EMRSERVICE.Domain.Member;

import WELCOME.EMRSERVICE.Domain.Registration.Registration;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Getter @Setter
@Entity(name = "patient")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patient_id;
    private String patient_name;
    private String gender;
    private int age;
    private int weight;
    private int height;
    private String blood_type;
    private String patient_pw;
    private String patientLoginId;
    private String roles;

    @Builder
    public Member(Long patient_id, String patient_name, String gender, int age, int weight, int height, String blood_type, String patient_pw, String patientLoginId,String roles) {
        this.patient_id = patient_id;
        this.patient_name = patient_name;
        this.gender = gender;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.blood_type = blood_type;
        this.patient_pw = patient_pw;
        this.patientLoginId = patientLoginId;
        this.roles=roles;
    }
    @OneToMany(mappedBy = "patient")
    private List<Registration> appointments;
    public void updatePassword(String newPassword) {
        this.patient_pw = newPassword;
    }











}