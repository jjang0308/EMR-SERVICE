package WELCOME.EMRSERVICE.entity.patient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

//@Entity
@RequiredArgsConstructor
@Setter
@Getter
public class Patient {
//  @Id
    private long id;

    private String name;
    private Integer age;
    private String height;
    private String weight;

}
