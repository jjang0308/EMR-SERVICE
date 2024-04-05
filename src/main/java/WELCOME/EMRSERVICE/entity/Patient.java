package WELCOME.EMRSERVICE.entity;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "patient")
@Data
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patient_id;

    private String patientName;
    private String gender;
    private int age;
    private int height;
    private int weight;
    private String bloodType;

    @Column(unique = true)
    private String patientLoginId;

    @Column(unique = true)
    private String patientPw;

    public Patient(){}
}
