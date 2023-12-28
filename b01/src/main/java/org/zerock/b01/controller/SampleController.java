package org.zerock.b01.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.dto.BoardDTO;

import java.util.List;

@Controller
@Log4j2
public class SampleController {

    @GetMapping("/hello")
    public void hello(Model model){         //resources>templates 경로
        log.info("hello...");
        model.addAttribute("msg", "Hello, World");
    }

    @GetMapping("/ex/ex3")
    public void ex3(Model model){
        model.addAttribute("arr", new String[]{"AAA", "BBB", "CCC"});
    }

    @GetMapping("/")
    public String homeToList(){
        return "redirect:/board/list";
    }


}
