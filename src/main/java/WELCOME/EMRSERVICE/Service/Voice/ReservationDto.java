package WELCOME.EMRSERVICE.Service.Voice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class ReservationDto {
    private Long id;
    private String doctorName;
    private String deptName;
    private String patientName;
    private LocalDate date;
    private LocalTime time;

    public ReservationDto(Long id, String doctorName, String deptName,String patientName, LocalDate date, LocalTime time) {
        this.id = id;
        this.doctorName = doctorName;
        this.deptName=deptName;
        this.patientName = patientName;
        this.date = date;
        this.time = time;
    }

}
