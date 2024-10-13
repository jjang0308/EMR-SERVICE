package WELCOME.EMRSERVICE.Dto.Doctor;

import WELCOME.EMRSERVICE.Domain.Doctor.Dept;
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
    private String newPassword;  // 새 비밀번호
    private String confirmPassword;  // 확인용 비밀번호
    private String roles;
    private Long deptId;
    private String deptName;

    public Doctor toEntity(Dept dept) {
        return Doctor.builder()
                .doctorId(doctorId)
                .doctorName(doctorName)
                .doctorPw(doctorPw)
                .doctorLoginId(doctorLoginId)
                .roles(roles)
                .dept(dept)  // 부서 설정
                .build();
    }

    @Builder
    public DoctorDto(Long doctorId, String doctorName, String doctorLoginId, String doctorPw, String roles, Long deptId, String deptName) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.doctorPw = doctorPw;
        this.doctorLoginId = doctorLoginId;
        this.roles = roles;
        this.deptId = deptId;
        this.deptName = deptName;
    }
}

