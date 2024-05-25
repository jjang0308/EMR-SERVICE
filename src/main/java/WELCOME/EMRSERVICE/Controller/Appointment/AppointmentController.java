package WELCOME.EMRSERVICE.Controller.Appointment;

import WELCOME.EMRSERVICE.Domain.Appointment.Appointment;
import WELCOME.EMRSERVICE.Domain.Doctor.Doctor;
import WELCOME.EMRSERVICE.Dto.Doctor.DoctorDto;
import WELCOME.EMRSERVICE.Repository.Doctor.DeptRepository;
import WELCOME.EMRSERVICE.Repository.Doctor.DoctorRepository;
import WELCOME.EMRSERVICE.Service.Appointment.AppointmentService;
import WELCOME.EMRSERVICE.Service.Doctor.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorRepository doctorRepository;
    private final DeptRepository deptRepository;

    private final DeptService deptService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, DoctorRepository doctorRepository, DeptRepository deptRepository, DeptService deptService) {
        this.appointmentService = appointmentService;
        this.doctorRepository = doctorRepository;
        this.deptRepository = deptRepository;
        this.deptService = deptService;

    }

    @GetMapping("/new")
    public String showAppointmentForm(Model model, Authentication authentication) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("patientId", authentication.getName());
        List<WELCOME.EMRSERVICE.Domain.Doctor.Dept> departments = deptService.getAllDepts();
        model.addAttribute("depts", departments);
        List<Doctor>doctors = doctorRepository.findAll();
        model.addAttribute("doctors", doctors);
        return "/member/bookAppointment";
    }



    @PostMapping("/new")
    public String createAppointment(@RequestParam String doctorId,
                                    @RequestParam String appointmentDate,
                                    Authentication authentication,
                                    Model model) {
        String patientId = authentication.getName();
        LocalDateTime appointmentDateTime = LocalDateTime.parse(appointmentDate);
        try {
            appointmentService.createAppointment(patientId, doctorId, appointmentDateTime);
            return "redirect:/home/dashboard";  // 예약 후 대시보드로 리디렉션
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "/member/bookAppointment";
        }
    }

    @GetMapping("/check")
    public String listAppointments(@RequestParam(required = false) String patientId, Model model) {
        List<Appointment> appointments;
        if (patientId != null) {
            appointments = appointmentService.getAppointmentsByPatient(patientId);
        } else {
            appointments = appointmentService.getAllAppointments(); // 모든 예약 가져오기
        }
        model.addAttribute("appointments", appointments);
        return "/member/listAppointments";
    }

    @GetMapping("/my")
    public String listMyAppointments(Authentication authentication, Model model) {
        String patientId = authentication.getName();
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patientId);
        model.addAttribute("appointments", appointments);
        return "/member/listMyAppointments";
    }

    @GetMapping("/doctor")
    public String listDoctorAppointments(Authentication authentication, Model model) {
        String doctorLoginId = authentication.getName();
        Doctor doctor = doctorRepository.findByDoctorLoginId(doctorLoginId);
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctor(doctor.getDoctorId());
        model.addAttribute("appointments", appointments);
        return "/doctor/listDoctorAppointments";
    }

    @PostMapping("/cancel/{id}")
    public String cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return "redirect:/home/dashboard";
    }
}
