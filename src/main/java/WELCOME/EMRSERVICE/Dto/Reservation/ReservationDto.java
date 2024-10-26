package WELCOME.EMRSERVICE.Dto.Reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ReservationDto {
    private Long reservationId;
    private Long doctorId;
    private Long patientId;
    private String doctorName;
    private String deptName;
    private String patientName;
    private LocalDate date;
    private LocalTime time;

    // 모든 필드를 포함한 생성자
    public ReservationDto(Long reservationId, Long doctorId, String doctorName, String deptName, Long patientId, String patientName, LocalDate date, LocalTime time) {
        this.reservationId = reservationId;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.deptName = deptName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.date = date;
        this.time = time;
    }
}
