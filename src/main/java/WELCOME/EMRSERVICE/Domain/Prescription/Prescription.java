package WELCOME.EMRSERVICE.Domain.Prescription;

import WELCOME.EMRSERVICE.Domain.Doctor.Doctor;
import WELCOME.EMRSERVICE.Domain.Member.Member;
import WELCOME.EMRSERVICE.Domain.Reservation.Reservation;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prescriptionId;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    private String medication;
    private String dosage;
    private String instructions;
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Member member;

    public Prescription(Long id, Doctor doctor, Member member, Reservation reservation, String medication, String dosage, String instructions, LocalDateTime date) {
        this.prescriptionId = id;
        this.doctor = doctor;
        this.member = member;
        this.reservation = reservation;
        this.medication = medication;
        this.dosage = dosage;
        this.instructions = instructions;
        this.date = date;
    }
}
