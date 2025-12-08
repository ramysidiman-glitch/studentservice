package com.example.demo.service;

import com.example.demo.model.Student;
import com.example.demo.model.University;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//yasmine
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UniversityRepository universityRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

public void associateCourse(Long studentId, Long courseId) {
    Map<String, Object> event = new HashMap<>();
    event.put("student_id", studentId);
    event.put("course_id", courseId);

    rabbitTemplate.convertAndSend(
            "student-events",
            "student.course.associate",
            event
    );
}


    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(int id) {
        return studentRepository.findById(id);
    }

    // Dans StudentService.java

    public Optional<Student> addStudent(Student student) {

        if (student.getUniversity() == null || student.getUniversity().getId() == 0) {
            return Optional.empty();
        }
        Optional<University> uniOpt = universityRepository.findById(student.getUniversity().getId());
        if (uniOpt.isPresent()) {
            student.setUniversity(uniOpt.get());
            try {
                return Optional.of(studentRepository.save(student));
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                // Loggez l'erreur de violation d'unicité (email) ici pour la voir dans la
                // console Java.
                System.err.println("Erreur de violation de données (Email déjà existant ?): " + e.getMessage());
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public Optional<Student> updateStudent(int id, Student updatedStudent) {
        return studentRepository.findById(id).map(student -> {
            if (updatedStudent.getEmail() != null) {
                student.setEmail(updatedStudent.getEmail());
            }
            if (updatedStudent.getFirstName() != null) {
                student.setFirstName(updatedStudent.getFirstName());
            }
            if (updatedStudent.getLastName() != null) {
                student.setLastName(updatedStudent.getLastName());
            }
            if (updatedStudent.getUniversity() != null) {
                universityRepository.findById(updatedStudent.getUniversity().getId())
                        .ifPresent(student::setUniversity);
            }
            return studentRepository.save(student);
        });
    }

    public boolean deleteStudent(int id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Student> searchStudents(String keyword) {
        return studentRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(keyword, keyword);
    }

    public List<Student> findStudentsByUniversity(String univName) {
        return studentRepository.findByUniversity_NameIgnoreCase(univName);
    }

    public Optional<Student> findStudentByEmail(String email) {
        return Optional.ofNullable(studentRepository.findStudentByEmail(email));
    }
}
