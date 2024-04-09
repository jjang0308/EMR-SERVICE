package WELCOME.EMRSERVICE.Domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Getter
@Entity(name = "patient")
public class Member {
    @Id
    @GeneratedValue
    private Long patient_id;
    private String patient_name;
    private String gender;
    private int age;
    private int weight;
    private int height;
    private String blood_type;
    private String patient_pw;
    private String patientLoginId;

    @Builder
    public Member(Long patient_id, String patient_name, String gender, int age, int weight, int height, String blood_type, String patient_pw, String patientLoginId) {
        this.patient_id = patient_id;
        this.patient_name = patient_name;
        this.gender = gender;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.blood_type = blood_type;
        this.patient_pw = patient_pw;
        this.patientLoginId = patientLoginId;
    }


}