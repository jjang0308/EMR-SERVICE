package WELCOME.EMRSERVICE.Service.Appointment;

import WELCOME.EMRSERVICE.Domain.Appointment.Appointment;
import WELCOME.EMRSERVICE.Domain.Doctor.Doctor;
import WELCOME.EMRSERVICE.Domain.Member.Member;
import WELCOME.EMRSERVICE.Dto.Doctor.DoctorDto;
import WELCOME.EMRSERVICE.Repository.Appointment.AppointmentRepository;
import WELCOME.EMRSERVICE.Repository.Doctor.DeptRepository;
import WELCOME.EMRSERVICE.Repository.Doctor.DoctorRepository;
import WELCOME.EMRSERVICE.Repository.Member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final MemberRepository memberRepository;
    private final DeptRepository deptRepository;
    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, MemberRepository memberRepository, DeptRepository deptRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.memberRepository = memberRepository;
        this.deptRepository = deptRepository;
    }

    public Appointment createAppointment(String patientId, String doctorId, LocalDateTime appointmentDate) {
        Member patient = memberRepository.findByPatientLoginId(patientId);
        Doctor doctor = doctorRepository.findById(Long.parseLong(doctorId)).orElseThrow(() -> new RuntimeException("의사를 찾을 수 없습니다."));

        if (appointmentRepository.existsByDoctorAndAppointmentDate(doctor, appointmentDate)) {
            throw new RuntimeException("이미 해당 시간에 예약이 있습니다.");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(appointmentDate);

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointmentsByPatient(String patientId) {
        Member patient = memberRepository.findByPatientLoginId(patientId);
        return appointmentRepository.findByPatient(patient);
    }

    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new IllegalArgumentException("의사를 찾을 수 없습니다."));
        return appointmentRepository.findByDoctor(doctor);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public void cancelAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    private List<LocalDateTime> getBookedTimes(Long deptId) {
        // 여기에 데이터베이스에서 예약된 시간 목록을 가져오는 로직을 추가합니다.
        return List.of(
                LocalDateTime.of(2024, 5, 28, 9, 0),
                LocalDateTime.of(2024, 5, 28, 10, 0),
                LocalDateTime.of(2024, 5, 28, 13, 0)
        );
    }

    public List<DoctorDto> findDoctorsByDeptId(Long deptId) {
        return doctorRepository.findByDept_DeptId(deptId)
                .stream()
                .map(doctor -> new DoctorDto(
                        doctor.getDoctorId(),
                        doctor.getDoctorName(),
                        doctor.getDoctorLoginId(),
                        doctor.getDoctorPw(),
                        doctor.getRoles(),
                        doctor.getDept().getDeptId()
                ))
                .collect(Collectors.toList());
    }

    public List<String> findAvailableTimesByDeptId(Long deptId, LocalDate date) {
        List<LocalDateTime> bookedTimes = getBookedTimes(deptId);
        List<String> availableTimes = new ArrayList<>();

        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(17, 0);

        for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusHours(1)) {
            if (time.equals(LocalTime.of(12, 0))) {
                continue; // 점심 시간 제외
            }
            LocalDateTime dateTime = date.atTime(time);
            if (!bookedTimes.contains(dateTime)) {
                availableTimes.add(dateTime.toString() + " - available");
            } else {
                availableTimes.add(dateTime.toString() + " - unavailable");
            }
        }
        return availableTimes;
    }

}
