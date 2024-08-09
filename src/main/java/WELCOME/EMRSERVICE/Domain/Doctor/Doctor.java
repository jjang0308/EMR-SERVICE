package WELCOME.EMRSERVICE.Domain.Doctor;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity(name = "doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;
    private String doctorName;
    private String doctorLoginId;
    private String doctorPw;
    private String roles;


    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "dept_id")
    private Dept dept;

    @Builder
    public Doctor(Long doctorId, String doctorName, String doctorPw, String doctorLoginId, String roles, Dept dept) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.doctorPw = doctorPw;
        this.doctorLoginId = doctorLoginId;
        this.roles = roles;
        this.dept = dept;
    }

    public Doctor() {}

    public void updatePassword(String newPassword) {
        this.doctorPw = newPassword;
    }
}
