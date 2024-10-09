package WELCOME.EMRSERVICE.Service.Reservation;

import WELCOME.EMRSERVICE.Domain.Doctor.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkAvailability(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time) {
        if (date == null || time == null) {
            return ResponseEntity.badRequest().body(null);
        }

        boolean available = reservationService.isAvailable(date, time);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", available);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<List<ReservationDto>> getAllReservations() {
        List<ReservationDto> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @PostMapping("/reserve")
    public ResponseEntity<ReservationDto> createReservation(
            @RequestParam Long doctorId,
            @RequestParam Long patientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time) {
        if (doctorId == null || patientId == null || date == null || time == null) {
            return ResponseEntity.badRequest().body(null);
        }

        ReservationDto reservationDto = reservationService.createReservation(doctorId, patientId, date, time);
        return ResponseEntity.ok(reservationDto);
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<LocalTime>> getAvailableTimes(
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (doctorId == null || date == null) {
            return ResponseEntity.badRequest().body(null);
        }

        List<LocalTime> availableTimes = reservationService.getAvailableTimes(doctorId, date);
        return ResponseEntity.ok(availableTimes);
    }

    @GetMapping("/fully-booked-dates")
    public ResponseEntity<List<LocalDate>> getFullyBookedDates(@RequestParam Long doctorId) {
        List<LocalDate> fullyBookedDates = reservationService.getFullyBookedDates(doctorId);
        return ResponseEntity.ok(fullyBookedDates);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ReservationDto>> getReservationsByPatientId(@PathVariable Long patientId) {
        List<ReservationDto> reservations = reservationService.getReservationsByPatientId(patientId);
        return ResponseEntity.ok(reservations);
    }
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<ReservationDto>> getReservationsByDoctorId(@PathVariable Long doctorId) {
        List<ReservationDto> reservations = reservationService.getReservationsByDoctorId(doctorId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/doctor/confirm/{doctorId}")
    public List<Reservation> getReservationsFindByDoctorId(@PathVariable Long doctorId) {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(doctorId); // 의사 객체에 ID 설정
        return reservationService.getReservationsByDoctor(doctor);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable Long reservationId) {
        ReservationDto reservationDto = reservationService.getReservationById(reservationId);
        return ResponseEntity.ok(reservationDto);
    }

    @GetMapping("/doctor/{doctorId}/date")
    public ResponseEntity<List<ReservationDto>> getReservationsByDoctorAndDate(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        // 의사 ID와 날짜로 예약된 환자 목록 조회
        List<ReservationDto> reservations = reservationService.getReservationsByDoctorAndDate(doctorId, date);
        return ResponseEntity.ok(reservations);
    }

}
