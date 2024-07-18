package WELCOME.EMRSERVICE.Domain.Doctor;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@Entity(name = "dept")
public class Dept {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deptId;
    private String deptName;

    @Builder
    public Dept(Long deptId, String deptName) {
        this.deptId = deptId;
        this.deptName = deptName;
    }

    @JsonManagedReference
    @OneToMany(mappedBy = "dept")
    private List<Doctor> doctors;
}
