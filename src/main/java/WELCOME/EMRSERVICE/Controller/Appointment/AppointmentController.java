package WELCOME.EMRSERVICE.Controller.Appointment;

import WELCOME.EMRSERVICE.Domain.Appointment.Appointment;
import WELCOME.EMRSERVICE.Service.Appointment.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/new")
    public String showAppointmentForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        return "/member/bookAppointment";
    }

    @PostMapping("/new")
    public String createAppointment(@RequestParam String patientId,
                                    @RequestParam String doctorId,
                                    @RequestParam String appointmentDate,
                                    Model model) {
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

    @PostMapping("/cancel/{id}")
    public String cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return "redirect:/appointments";
    }
}
