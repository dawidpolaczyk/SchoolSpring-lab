package pl.edu.agh.ki.mwo.web.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.edu.agh.ki.mwo.model.School;
import pl.edu.agh.ki.mwo.model.SchoolClass;
import pl.edu.agh.ki.mwo.model.Student;
import pl.edu.agh.ki.mwo.persistence.DatabaseConnector;

@Controller
public class StudentsController {

	@RequestMapping(value = "/Students")
	public String listStudents(Model model, HttpSession session) {
		if (session.getAttribute("userLogin") == null)
			return "redirect:/Login";

		model.addAttribute("students", DatabaseConnector.getInstance().getStudents());

		return "studentsList";
	}

	@RequestMapping(value = "/AddStudent")
	public String displayAddSchoolClassForm(Model model, HttpSession session) {
		if (session.getAttribute("userLogin") == null)
			return "redirect:/Login";

		model.addAttribute("students", DatabaseConnector.getInstance().getStudents());

		return "studentForm";
	}

	@RequestMapping(value = "/CreateStudent", method = RequestMethod.POST)
	public String createStudent(@RequestParam(value = "studentName", required = false) String name,
			@RequestParam(value = "studentSurname", required = false) String surname,
			@RequestParam(value = "studentPesel", required = false) String pesel,
			@RequestParam(value = "studentClass_id", required = false) String class_id, Model model,
			HttpSession session) {
		if (session.getAttribute("userLogin") == null)
			return "redirect:/Login";

		Student student = new Student();
		student.setName(name);
		student.setSurname(surname);
		student.setPesel(pesel);
		student.setClass_id(Integer.valueOf(class_id));

		DatabaseConnector.getInstance().addStudent(student, class_id);
		model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
		model.addAttribute("message", "Nowy student został dodany");

		return "studentsList";
	}

	@RequestMapping(value = "/DeleteStudent", method = RequestMethod.POST)
	public String deleteStudent(@RequestParam(value = "studentId", required = false) String studentId,
			Model model, HttpSession session) {
		if (session.getAttribute("userLogin") == null)
			return "redirect:/Login";

		DatabaseConnector.getInstance().deleteStudent(studentId);
		model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
		model.addAttribute("message", "Student został usunięty");

		return "studentsList";
	}
	

    @RequestMapping(value = "/EditStudent")
    public String displayAddStudentForm(@RequestParam(value = "studentID", required = false) String studentID, Model model, HttpSession session) {
        if (session.getAttribute("userLogin") == null) {
            return "redirect:/Login";
        }
        model.addAttribute("schoolClass", DatabaseConnector.getInstance().getSchoolClasses());
        model.addAttribute("student", DatabaseConnector.getInstance().getStudentByID(studentID));
        model.addAttribute("studentClass", DatabaseConnector.getInstance().getStudentClassByID(studentID));
        
        return "studentFormEdit";
    }

    @RequestMapping(value = "/ConfirmEditStudent", method = RequestMethod.POST)
    public String createSchoolClass(@RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "surname", required = false) String surname,
            @RequestParam(value = "studentID", required = false) String studentID,
            @RequestParam(value = "pesel", required = false) String pesel,
            @RequestParam(value = "schoolClass", required = false) String schoolClasslId,
            Model model, HttpSession session) {
        if (session.getAttribute("userLogin") == null) {
            return "redirect:/Login";
        }

        Student student = DatabaseConnector.getInstance().getStudentByID(studentID);
        student.setName(name);
        student.setSurname(surname);
        student.setPesel(pesel);

        DatabaseConnector.getInstance().editStudents(studentID);
        model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
        model.addAttribute("message", "Student został zmodyfikowany");

        return "studentsList";
    }

}
