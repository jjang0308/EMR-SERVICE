package WELCOME.EMRSERVICE.service;

import WELCOME.EMRSERVICE.dto.RegisterRequestDto;
import WELCOME.EMRSERVICE.entity.Patient;
import WELCOME.EMRSERVICE.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientLoginService {

    @Autowired
    private PatientRepository patientRepository;

    public void register(RegisterRequestDto registerRequestDto) {
        // 데이터 가공 조작 비즈니스 로직

        // DTO 받아서 Entity로 만들고
        // -> repo에 넘겨주면 repo가 저장을 하든 업뎃을 하든 삭제를 하든
        Patient patient = new Patient();

        // 이건 사용자한테 입력 받은거
        patient.setPatient_id(registerRequestDto.getPatient_id());
        patient.setPatientLoginId(registerRequestDto.getPatient_login_id());
        patient.setPatientPw(registerRequestDto.getPatient_pw());
        patient.setPatientName(registerRequestDto.getPatient_name());
        patient.setAge(registerRequestDto.getAge());
        patient.setHeight(registerRequestDto.getHeight());
        patient.setWeight(registerRequestDto.getWeight());
        patient.setAge(registerRequestDto.getAge());
        patient.setBloodType(registerRequestDto.getBlood_type());
        patientRepository.save(patient);
    }


}