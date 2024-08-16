package WELCOME.EMRSERVICE.Service.Prescription;

import WELCOME.EMRSERVICE.Domain.Member.Member;
import WELCOME.EMRSERVICE.Service.Reservation.ReservationDto;
import WELCOME.EMRSERVICE.Service.Reservation.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;
    @Autowired
    private ReservationService reservationService;

    @PostMapping("/create/{reservationId}")
    public ResponseEntity<PrescriptionDto> createPrescription(
            @PathVariable Long reservationId,
            @RequestParam String medication,
            @RequestParam String dosage,
            @RequestParam String instructions,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {

        if (reservationId == null) {
            throw new IllegalArgumentException("Reservation ID is required");
        }

        // 예약 ID로 예약 정보 가져오기
        ReservationDto reservationDto = reservationService.getReservationDtoById(reservationId);

        // 처방 생성
        PrescriptionDto prescriptionDto = prescriptionService.createPrescription(reservationDto, medication, dosage, instructions, date);
        return ResponseEntity.ok(prescriptionDto);
    }

    @GetMapping("/patient/{patientId}")
    public List<PrescriptionDto> getPrescriptionsByPatientId(@PathVariable Long patientId) {
        Member member = new Member();
        member.setPatientId(patientId); // Member 엔터티의 ID를 설정
        return prescriptionService.getPrescriptionsByMember(member);
    }

    // 예약 ID로 처방전 조회
    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<List<PrescriptionDto>> getPrescriptionsByReservationId(@PathVariable Long reservationId) {
        List<PrescriptionDto> prescriptions = prescriptionService.getPrescriptionsByReservationId(reservationId);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/status/{reservationId}")
    public ResponseEntity<Boolean> checkPrescriptionStatus(@PathVariable Long reservationId) {
        boolean isPrescribed = prescriptionService.isPrescriptionCompleted(reservationId);
        return ResponseEntity.ok(isPrescribed);
    }

    @PutMapping("/update/{reservationId}")
    public ResponseEntity<PrescriptionDto> updatePrescription(
            @PathVariable Long reservationId,
            @RequestBody PrescriptionDto prescriptionDto) {

        PrescriptionDto updatedPrescription = prescriptionService.updatePrescription(reservationId, prescriptionDto);
        if (updatedPrescription != null) {
            return ResponseEntity.ok(updatedPrescription);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}



