//package WELCOME.EMRSERVICE;
//
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class PatientLoginServiceTest {
//
//
//    @Autowired
//    private PatientRepository patientRepository;
//
//    @Test
//    void testRegister() {
//        PatientDto rDto = new PatientDto(123L,"userA","F",12,120,100,"AB","Iss1d","0s1000");
//        Patient patient = new Patient();
//        patient.setPatient_id(rDto.getPatient_id());
//        patient.setPatient_login_id(rDto.getPatient_login_id());
//        patient.setPatient_pw(rDto.getPatient_pw());
//        patient.setPatientName(rDto.getPatient_name());
//        patient.setAge(rDto.getAge());
//        patient.setHeight(rDto.getHeight());
//        patient.setWeight(rDto.getWeight());
//        patient.setBloodType(rDto.getBlood_type());
//        patient.setGender(rDto.getGender());
//        patientRepository.save(patient);
//        Assertions.assertThat(patient.getPatient_login_id()).isEqualTo(rDto.getPatient_login_id());
//    }
//
//}