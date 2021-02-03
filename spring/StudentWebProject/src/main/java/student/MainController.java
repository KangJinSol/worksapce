package student;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import student.dto.StudentDTO;
import student.service.StudentService;

@Controller
public class MainController {
	private StudentService studentService;
	public MainController(StudentService studentService) {
		super();
		this.studentService = studentService;
	}
	
	@RequestMapping("/")
	public String student_search(HttpServletRequest request) {
		return selectAllStudent(request);
	
	}
	
	@RequestMapping("/main.do")
	public String selectAllStudent (HttpServletRequest request) {
		List<StudentDTO> list = studentService.selectAllStudent();
		request.setAttribute("list", list);
		return "student_search";
	}
	
	@RequestMapping("/studentSearch.do")
	public String selectSearchStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String kind = request.getParameter("kind");
		String search = request.getParameter("search");
		List<StudentDTO> list = studentService.selectSearchStudent(kind, search);
		response.setContentType("text/html;charset=utf-8");
		JSONArray array = new JSONArray(list);
		JSONObject obj = new JSONObject();
		obj.put("result", array);
		if(list.size()>0) {
			obj.put("responseCode", 200);
			obj.put("responseMessage", "정상적으로 조회되었습니다.");
		}
		else {
			obj.put("responseCode", 500);
			obj.put("responseMessage", "조회된 데이터가 없습니다.");
		}
			
		try {
			response.getWriter().write(obj.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	@RequestMapping("sendLog.do")
	public String sendLog(HttpServletRequest request, HttpServletResponse response) {
		String log_date = request.getParameter("log_date");
		int code_number = Integer.parseInt(request.getParameter("code_number"));
		String message = request.getParameter("message");
		System.out.println(log_date + " " + code_number + " " + message);
		int count = studentService.insertLog(log_date,code_number,message);
		System.out.println(count);
		try {
			response.getWriter().write("add count + "+count);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}