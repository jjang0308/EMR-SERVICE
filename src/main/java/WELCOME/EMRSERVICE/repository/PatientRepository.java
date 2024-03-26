package WELCOME.EMRSERVICE.repository;

import WELCOME.EMRSERVICE.entity.patient.Patient;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PatientRepository {
    private static final Map<Long, Patient> store = new ConcurrentHashMap<>();
    private static long sequence = 0L;

    public Patient save(Patient patient){
        patient.setId(++sequence);
        store.put(patient.getId(),patient);
        return patient;
    }

}
