package WELCOME.EMRSERVICE.Service.Voice;

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
    private LocalDate date;
    private LocalTime time;
    private Long doctorId;
}
