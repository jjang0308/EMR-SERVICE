package WELCOME.EMRSERVICE.Service.Voice;

import WELCOME.EMRSERVICE.Domain.Doctor.Dept;
import WELCOME.EMRSERVICE.Domain.Doctor.Doctor;
import WELCOME.EMRSERVICE.Domain.Member.Member;
import WELCOME.EMRSERVICE.Repository.Doctor.DeptRepository;
import WELCOME.EMRSERVICE.Repository.Doctor.DoctorRepository;
import WELCOME.EMRSERVICE.Repository.Member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DeptRepository deptRepository;

    public boolean isAvailable(LocalDate date, LocalTime time) {
        return !reservationRepository.existsByDateAndTime(date, time);
    }

    public List<ReservationDto> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(reservation -> new ReservationDto(reservation.getId(), reservation.getDoctor().getDoctorName(), reservation.getDept().getDeptName(),reservation.getPatient().getPatientName(),reservation.getDate(),reservation.getTime()))
                .collect(Collectors.toList());
    }

    public ReservationDto createReservation(Long doctorId, Long patientId, LocalDate date, LocalTime time) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new IllegalArgumentException("Invalid doctor ID"));
        Member patient = memberRepository.findById(patientId).orElseThrow(() -> new IllegalArgumentException("Invalid patient ID"));
        Dept dept = doctor.getDept();

        Reservation reservation = Reservation.builder()
                .doctor(doctor)
                .patient(patient)
                .dept(dept)
                .date(date)
                .time(time)
                .build();

        reservation = reservationRepository.save(reservation);

        return new ReservationDto(reservation.getId(), reservation.getDoctor().getDoctorName(),reservation.getDept().getDeptName(), reservation.getPatient().getPatientName(), reservation.getDate(), reservation.getTime());
    }

    public List<LocalTime> getAvailableTimes(Long doctorId, LocalDate date) {
        List<Reservation> reservations = reservationRepository.findByDoctorDoctorIdAndDate(doctorId, date);
        List<LocalTime> bookedTimes = reservations.stream().map(Reservation::getTime).collect(Collectors.toList());

        List<LocalTime> allTimes = List.of(
                LocalTime.of(9, 0), LocalTime.of(9, 30),
                LocalTime.of(10, 0), LocalTime.of(10, 30),
                LocalTime.of(11, 0), LocalTime.of(11, 30),
                LocalTime.of(14, 0), LocalTime.of(14, 30),
                LocalTime.of(15, 0), LocalTime.of(15, 30),
                LocalTime.of(16, 0), LocalTime.of(16, 30)
        );

        return allTimes.stream().filter(time -> !bookedTimes.contains(time)).collect(Collectors.toList());
    }

    public List<LocalDate> getFullyBookedDates(Long doctorId) {
        List<Reservation> reservations = reservationRepository.findByDoctorDoctorId(doctorId);
        return reservations.stream().map(Reservation::getDate).distinct().collect(Collectors.toList());
    }

    public List<ReservationDto> getReservationsByPatientId(Long patientId) {
        List<Reservation> reservations = reservationRepository.findByPatientPatientId(patientId);
        return reservations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ReservationDto convertToDto(Reservation reservation) {
        return new ReservationDto(
                reservation.getId(),
                reservation.getDoctor().getDoctorName(),
                reservation.getDept().getDeptName(),
                reservation.getPatient().getPatientName(),
                reservation.getDate(),
                reservation.getTime()
        );
    }
}
