package com.security.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.security.modal.Student;

@RestController
public class StudentController {
	private List<Student> students = new ArrayList<>(
			List.of(
					new Student(1, "Navin", 60),
                    new Student(2, "Kiran", 65)
                    )
			);
	@GetMapping("/students")
	public List<Student> getStudents(){
		System.out.println("call");
		return students;
	}
	@PostMapping("/students") 
    public Student addStudent(@RequestBody Student student) {
        students.add(student);
        return student;
    }
}
