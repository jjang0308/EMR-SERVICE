package WELCOME.EMRSERVICE.Dto.Role;

import WELCOME.EMRSERVICE.Domain.Role.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleDto {
    private Long role_id;
    private String role_name;

    public Role toEntity() {
        return Role.builder()
                .role_id(role_id)
                .role_name(role_name)
                .build();
    }

    @Builder
    public RoleDto(Long role_id, String role_name) {
        this.role_id = role_id;
        this.role_name = role_name;

    }



}