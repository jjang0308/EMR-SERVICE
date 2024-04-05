package WELCOME.EMRSERVICE;

import WELCOME.EMRSERVICE.dto.RegisterRequestDto;
import WELCOME.EMRSERVICE.entity.Patient;
import WELCOME.EMRSERVICE.repository.PatientRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PatientLoginServiceTest {


    @Autowired
    private PatientRepository patientRepository;

    @Test
    void testRegister() {
        RegisterRequestDto rDto = new RegisterRequestDto(123L,"userA","F",12,120,100,"AB","Iss1d","0s1000");
        Patient patient = new Patient();
        patient.setPatient_id(rDto.getPatient_id());
        patient.setPatientLoginId(rDto.getPatient_login_id());
        patient.setPatientPw(rDto.getPatient_pw());
        patient.setPatientName(rDto.getPatient_name());
        patient.setAge(rDto.getAge());
        patient.setHeight(rDto.getHeight());
        patient.setWeight(rDto.getWeight());
        patient.setBloodType(rDto.getBlood_type());
        patient.setGender(rDto.getGender());
        patientRepository.save(patient);
        Assertions.assertThat(patient.getPatientLoginId()).isEqualTo(rDto.getPatient_login_id());
    }

}