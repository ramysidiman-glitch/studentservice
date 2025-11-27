package com.example.demo.repository;
import com.example.demo.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

   
    List<Student>findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String first_name, String last_name);

    
    List<Student> findByUniversity_NameIgnoreCase(@Param("param") String univName);

    
    Student findStudentByEmail(String email);
}