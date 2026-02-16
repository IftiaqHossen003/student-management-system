package com.example.webapp.controller;

import com.example.webapp.dto.StudentDTO;
import com.example.webapp.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String getStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "students";
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addStudent(Model model) {
        model.addAttribute("student", new StudentDTO());
        return "student-form";
    }

    @PostMapping("/store")
    @PreAuthorize("hasRole('ADMIN')")
    public String storeStudent(@ModelAttribute("student") StudentDTO studentDTO) {
        studentService.saveStudent(studentDTO);
        return "redirect:/students";
    }
    
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }
}
