package WELCOME.EMRSERVICE.Service.Prescription;

import WELCOME.EMRSERVICE.Domain.Doctor.Doctor;
import WELCOME.EMRSERVICE.Domain.Member.Member;
import WELCOME.EMRSERVICE.Repository.Doctor.DoctorRepository;
import WELCOME.EMRSERVICE.Repository.Member.MemberRepository;
import WELCOME.EMRSERVICE.Service.Prescription.Prescription;
import WELCOME.EMRSERVICE.Service.Prescription.PrescriptionDto;
import WELCOME.EMRSERVICE.Service.Prescription.PrescriptionRepository;
import WELCOME.EMRSERVICE.Service.Reservation.Reservation;
import WELCOME.EMRSERVICE.Service.Reservation.ReservationDto;
import WELCOME.EMRSERVICE.Service.Reservation.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private ReservationService reservationService;

    public PrescriptionDto createPrescription(ReservationDto reservationDto, String medication, String dosage, String instructions, LocalDateTime date) {
        // ReservationDto에서 예약 ID로 Reservation 엔티티를 가져옵니다.
        Reservation reservation = reservationService.getReservationEntityById(reservationDto.getReservationId());

        // Reservation에서 Doctor와 Patient 객체를 가져옵니다.
        Doctor doctor = reservation.getDoctor();
        Member member = reservation.getMember();

        // Prescription 엔티티를 생성할 때 Doctor와 Patient를 할당합니다.
        Prescription prescription = Prescription.builder()
                .reservation(reservation)
                .doctor(doctor)
                .member(member)
                .medication(medication)
                .dosage(dosage)
                .instructions(instructions)
                .date(date)
                .build();

        Prescription savedPrescription = prescriptionRepository.save(prescription);

        return convertToDto(savedPrescription);
    }



    public List<PrescriptionDto> getPrescriptionsByPatient(Member patient) {
        List<Prescription> prescriptions = prescriptionRepository.findByMember(patient);
        return prescriptions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PrescriptionDto> getPrescriptionsByMember(Member member) {
        List<Prescription> prescriptions = prescriptionRepository.findByMember(member);
        return prescriptions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private PrescriptionDto convertToDto(Prescription prescription) {
        return new PrescriptionDto(
                prescription.getPrescriptionId(),
                prescription.getDoctor().getDoctorId(),
                prescription.getMember().getPatientId(),
                prescription.getMedication(),
                prescription.getDosage(),
                prescription.getInstructions(),
                prescription.getDate()
        );
    }
}
