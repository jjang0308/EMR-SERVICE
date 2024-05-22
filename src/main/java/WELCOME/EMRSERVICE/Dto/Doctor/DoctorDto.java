package WELCOME.EMRSERVICE.Dto.Doctor;

import WELCOME.EMRSERVICE.Domain.Doctor.Doctor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DoctorDto {
    private Long doctorId;
    private String doctorName;
    private String doctorLoginId;
    private String doctorPw;
    private String roles;

    public Doctor toEntity() {
        return Doctor.builder()
                .doctorId(doctorId)
                .doctorName(doctorName)
                .doctorPw(doctorPw)
                .doctorLoginId(doctorLoginId)
                .roles(roles)
                .build();
    }

    @Builder
    public DoctorDto(Long doctorId, String doctorName, String doctorLoginId, String doctorPw, String roles) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.doctorPw = doctorPw;
        this.doctorLoginId = doctorLoginId;
        this.roles = roles;
    }
}
