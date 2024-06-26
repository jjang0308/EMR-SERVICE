package WELCOME.EMRSERVICE.Controller.Registration;

import WELCOME.EMRSERVICE.Domain.Registration.Registration;
import WELCOME.EMRSERVICE.Repository.Doctor.DeptRepository;
import WELCOME.EMRSERVICE.Repository.Doctor.DoctorRepository;
import WELCOME.EMRSERVICE.Service.Doctor.DeptService;
import WELCOME.EMRSERVICE.Service.Registration.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    private final DeptRepository deptRepository;
    private final DoctorRepository doctorRepository;

    @GetMapping("/member/book")
    public String showBookingPage(Model model) {
        List<WELCOME.EMRSERVICE.Domain.Doctor.Dept> depts = deptRepository.findAll();
        model.addAttribute("depts", depts);
        List<WELCOME.EMRSERVICE.Domain.Doctor.Doctor> doctors = doctorRepository.findAll(); // 모든 의사 정보를 가져옵니다.
        model.addAttribute("doctors", doctors);
        return "/member/bookAppointment";
    }

    @PostMapping("/member/book")
    public String bookAppointment(Authentication authentication,
                                  @RequestParam("dept_id") Long dept_id,
                                  @RequestParam("symptom") String symptom,
                                  @RequestParam("treat_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime treat_time) {
        String patientLoginId = authentication.getName();

        registrationService.bookAppointment(patientLoginId, dept_id, symptom, treat_time);
        return "redirect:/home/dashboard";
    }


    @GetMapping
    public String viewAppointments(Authentication authentication, Model model) {
        String loginId = authentication.getName();
        List<Registration> appointments = registrationService.findAppointmentsByPatient(loginId);
        model.addAttribute("appointments", appointments);

        return "/member/appointments";
    }

    @PostMapping("/member/cancel")
    public String cancelAppointment(@RequestParam("appointmentId") Long appointmentId) {
        registrationService.cancelAppointment(appointmentId);
        return "redirect:/member/appointments";
    }

}
