package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.model.University;
import com.example.demo.service.StudentService;
import com.example.demo.service.UniversityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")

public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private UniversityService universityService;

    // Route pour récupérer toutes les universités
    @GetMapping("/universities")
    public List<University> getAllUniversities() {
        return universityService.getAllUniversities();
    }

    @PostMapping("/addUniv")
    public ResponseEntity<University> addUniversity(@RequestBody University university) {
        University savedUniversity = universityService.addUniversity(university);
        return ResponseEntity.ok(savedUniversity);
    }

    @PostMapping("/add")
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Optional<Student> result = studentService.addStudent(student);
        return result
                .map(s -> new ResponseEntity<>(s, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .build());
    }

    @GetMapping("/getAll")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/get/{id}")
    public Student getStudentById(@PathVariable int id) {
        return studentService.getStudentById(id).orElse(null);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable int id, @RequestBody Student student) {
        Optional<Student> updatedStudent = studentService.updateStudent(id, student);
        return updatedStudent
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public String deleteStudent(@PathVariable int id) {
        studentService.deleteStudent(id);
        return "Student deleted successfully";
    }

    @GetMapping("/search")
    public List<Student> searchStudents(@RequestParam String keyword) {
        return studentService.searchStudents(keyword);
    }

    @GetMapping("/findByUniversity")
    public List<Student> findStudentsByUniversity(@RequestParam String univName) {
        return studentService.findStudentsByUniversity(univName);
    }

    @GetMapping("/byEmail")
    public Student findStudentByEmail(@RequestParam String email) {
        return studentService.findStudentByEmail(email).orElse(null);
    }

}