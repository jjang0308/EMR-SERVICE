package WELCOME.EMRSERVICE.Domain.Member;

import WELCOME.EMRSERVICE.Domain.Registration.Registration;
import WELCOME.EMRSERVICE.Dto.Member.MemberDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;

//@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Getter @Setter
@Entity(name = "patient")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;
    private String patientName;
    private String gender;
    private int age;
    private int weight;
    private int height;
    private String bloodType;
    private String patientPw;
    private String patientLoginId;
    private String roles;
    private String type="app";

    @Builder
    public Member(Long patientId, String patientName, String gender, int age, int weight, int height, String bloodType, String patientPw, String patientLoginId,String roles) {
        this.patientId = patientId;
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
    public Member(String patientLoginId,String type,String patientName,String gender){
        this.patientLoginId=patientLoginId;
        this.patientName=patientName;
        this.gender=gender;
        this.patientPw="password";
        this.type=type;
        this.roles="ROLE_MEMBER";
    }
    public  Member(){}
    @OneToMany(mappedBy = "patient")
    private List<Registration> registration;
    public void updatePassword(String newPassword) {
        this.patientPw = newPassword;
    }


}