package WELCOME.EMRSERVICE.Service.Prescription;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PrescriptionDto {

    private Long prescriptionId;
    private Long doctorId;
    private Long patientId;
    private String medication;
    private String dosage;
    private String instructions;
    private LocalDateTime date;

    // 모든 필드를 포함한 생성자
    public PrescriptionDto(Long id, Long doctorId, Long patientId, String medication, String dosage, String instructions, LocalDateTime date) {
        this.prescriptionId = id;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.medication = medication;
        this.dosage = dosage;
        this.instructions = instructions;
        this.date = date;
    }
}
