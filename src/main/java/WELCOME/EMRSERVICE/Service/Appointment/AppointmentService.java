package WELCOME.EMRSERVICE.Service.Appointment;

import WELCOME.EMRSERVICE.Domain.Appointment.Appointment;
import WELCOME.EMRSERVICE.Domain.Doctor.Doctor;
import WELCOME.EMRSERVICE.Domain.Member.Member;
import WELCOME.EMRSERVICE.Repository.Appointment.AppointmentRepository;
import WELCOME.EMRSERVICE.Repository.Doctor.DoctorRepository;
import WELCOME.EMRSERVICE.Repository.Member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, MemberRepository memberRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.memberRepository = memberRepository;
    }

    public Appointment createAppointment(String patientId, String doctorId, LocalDateTime appointmentDate) {
        Member patient = memberRepository.findByPatientLoginId(patientId);
        Doctor doctor = doctorRepository.findByDoctorLoginId(doctorId);

        if (appointmentRepository.existsByDoctorAndAppointmentDate(doctor, appointmentDate)) {
            throw new IllegalArgumentException("이미 해당 시간에 예약이 있습니다.");
        }

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(appointmentDate)
                .build();

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointmentsByPatient(String patientId) {
        Member patient = memberRepository.findByPatientLoginId(patientId);
        return appointmentRepository.findByPatient(patient);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public void cancelAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
}
