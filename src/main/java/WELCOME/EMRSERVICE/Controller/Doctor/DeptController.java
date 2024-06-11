package WELCOME.EMRSERVICE.Controller.Doctor;

import WELCOME.EMRSERVICE.Domain.Doctor.Dept;
import WELCOME.EMRSERVICE.Repository.Doctor.DeptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DeptController {

    @Autowired
    private DeptRepository deptRepository;

    @GetMapping("/departments")
    public List<Dept> getDepartments() {
        return deptRepository.findAll();
    }
}
