package WELCOME.EMRSERVICE.Domain.Role;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Getter
@Setter
@Entity(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long role_id;
    private String role_name;


    @Builder
    public Role(Long role_id, String role_name) {
        this.role_id = role_id;
        this.role_name = role_name;
    }






}