package com.example;

import com.example.domain.MemberVO;
import com.example.domain.UserDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@SpringBootApplication
public class SpringDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDemoApplication.class, args);
	}

	@GetMapping(value = "/string")
	public String getString(){
		System.out.println("====================");
		return "Welcome";
	}
	@GetMapping(value = "/member")
	public MemberVO getMember(){
		System.out.println("====================");
		return new MemberVO();
	}

	@GetMapping(value = "/list")
	public List<MemberVO> getMemberList(){
		System.out.println("====================");
		List<MemberVO> lists = new ArrayList<>();
		lists.add(new MemberVO("usertwo", "twopass", "user@two.com"));
		lists.add(new MemberVO("userthree", "threepass", "user@three.com"));
		lists.add(new MemberVO("userfour", "fourpass", "user@four.com"));
		return lists;
	}

	@GetMapping(value = "/user")
	public UserDTO getUser(){
		System.out.println("======/user======");
		return new UserDTO();
	}
}
