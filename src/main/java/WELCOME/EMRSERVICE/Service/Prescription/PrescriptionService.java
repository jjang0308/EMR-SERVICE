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
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private ReservationService reservationService;

    public PrescriptionDto createPrescription(ReservationDto reservationDto, String medication, String dosage, String instructions, LocalDateTime date) {
        try {
            // ReservationDto에서 예약 ID로 Reservation 엔티티를 가져옵니다.
            Reservation reservation = reservationService.getReservationEntityById(reservationDto.getReservationId());

            // Reservation이 존재하지 않으면 예외를 던집니다.
            if (reservation == null) {
                throw new IllegalArgumentException("예약 정보를 찾을 수 없습니다. 예약 ID: " + reservationDto.getReservationId());
            }

            // Reservation에서 Doctor와 Member 객체를 가져옵니다.
            Doctor doctor = reservation.getDoctor();
            Member member = reservation.getMember();

            // Doctor와 Member가 존재하지 않으면 예외를 던집니다.
            if (doctor == null) {
                throw new IllegalArgumentException("해당 예약에 할당된 의사가 존재하지 않습니다.");
            }
            if (member == null) {
                throw new IllegalArgumentException("해당 예약에 할당된 환자가 존재하지 않습니다.");
            }

            // Prescription 엔티티를 생성할 때 Doctor와 Member를 할당합니다.
            Prescription prescription = Prescription.builder()
                    .reservation(reservation)
                    .doctor(doctor)
                    .member(member)
                    .medication(medication)
                    .dosage(dosage)
                    .instructions(instructions)
                    .date(date)
                    .build();

            // 처방전을 저장합니다.
            Prescription savedPrescription = prescriptionRepository.save(prescription);

            return convertToDto(savedPrescription);
        } catch (IllegalArgumentException e) {
            // 잘못된 입력이나 논리적 오류 처리
            throw e; // 예외를 그대로 던져서 상위 레벨에서 처리하게 할 수 있음
        } catch (DataAccessException e) {
            // 데이터베이스 관련 예외 처리 (DataAccessException은 Spring의 예외 클래스)
            throw new RuntimeException("처방전을 생성하는 동안 데이터베이스 오류가 발생했습니다.", e);
        } catch (Exception e) {
            // 일반적인 예외 처리
            throw new RuntimeException("처방전을 생성하는 동안 예기치 않은 오류가 발생했습니다.", e);
        }
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

    public List<PrescriptionDto> getPrescriptionsByReservationId(Long reservationId) {
        List<Prescription> prescriptions = prescriptionRepository.findByReservationReservationId(reservationId);
        return prescriptions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public boolean isPrescriptionCompleted(Long reservationId) {
        // 예약 ID를 사용하여 해당 예약에 대한 처방전이 존재하는지 확인
        Optional<Prescription> prescription = prescriptionRepository.findByReservation_ReservationId(reservationId);
        return prescription.isPresent();
    }

    public PrescriptionDto updatePrescription(Long reservationId, PrescriptionDto prescriptionDto) {
        Optional<Prescription> prescriptionOptional = prescriptionRepository.findByReservation_ReservationId(reservationId);
        if (prescriptionOptional.isPresent()) {
            Prescription prescription = prescriptionOptional.get();

            // DTO에서 받은 데이터로 처방전 업데이트
            prescription.setMedication(prescriptionDto.getMedication());
            prescription.setDosage(prescriptionDto.getDosage());
            prescription.setInstructions(prescriptionDto.getInstructions());
            prescription.setDate(prescriptionDto.getDate()); // 여기서 시간도 업데이트

            // 업데이트된 처방전을 저장
            Prescription updatedPrescription = prescriptionRepository.save(prescription);

            // DTO로 변환하여 반환
            return convertToDto(updatedPrescription);
        }
        return null;  // 해당 예약에 대한 처방전이 없을 경우 null 반환
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
