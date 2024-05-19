package WELCOME.EMRSERVICE.Service.Registration;

import WELCOME.EMRSERVICE.Domain.Member.Member;
import WELCOME.EMRSERVICE.Domain.Registration.Registration;
import WELCOME.EMRSERVICE.Repository.Registration.RegistrationRepository;
import WELCOME.EMRSERVICE.Repository.Member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public RegistrationService(RegistrationRepository registrationRepository, MemberRepository memberRepository) {
        this.registrationRepository = registrationRepository;
        this.memberRepository = memberRepository;
    }

    public Registration bookAppointment(String patientLoginId, Long dept_id, String symptom, LocalDateTime treat_time) {
        Member patient = memberRepository.findByPatientLoginId(patientLoginId);

        Registration registration = Registration.builder()
                .patient(patient)
                .dept_id(dept_id)
                .symptom(symptom)
                .treat_time(treat_time)
                .registration_time(LocalDateTime.now())
                .build();

        return registrationRepository.save(registration);
    }


    public List<Registration> findAppointmentsByPatient(String patientLoginId) {
        Member patient = memberRepository.findByPatientLoginId(patientLoginId);
        return registrationRepository.findByPatient(patient);
    }

    public void cancelAppointment(Long registration_id) {
        registrationRepository.deleteById(registration_id);
    }
}
