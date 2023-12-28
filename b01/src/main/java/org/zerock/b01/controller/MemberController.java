package org.zerock.b01.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.dto.MemberJoinDTO;
import org.zerock.b01.repository.MemberRepository;
import org.zerock.b01.service.MemberService;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public void loginGET(@RequestParam(value = "error", required = false) String error,
                         @RequestParam(value = "logout", required = false) String logout){
        log.info("login get...");
        log.info("logout : " + logout);

        if(logout != null){
            log.info("user logout...");
        }
    }

    @GetMapping("/join")
    public void joinGET(){
        log.info("join get...");
    }

    @PostMapping("/join")
    public String joinPOST(MemberJoinDTO memberJoinDTO, RedirectAttributes redirectAttributes){
        log.info("join post...");
        log.info(memberJoinDTO);

        try {
            memberService.join(memberJoinDTO);
        } catch (MemberService.MidExistException e){
            redirectAttributes.addFlashAttribute("error", "mid");
            return "redirect:/member/join";
        }

        redirectAttributes.addFlashAttribute("result", "success");

        //return "redirect:/board/list";
        return "redirect:/member/login";    //회원 가입 후 로그인
    }

    @GetMapping("/modify")
    public void modifyGET(Model model){
        log.info("modify get...");

        //model.addAttribute(m)
    }

    @PostMapping("/modify")
    public String modifyPOST(MemberJoinDTO memberJoinDTO, RedirectAttributes redirectAttributes){
        log.info("modify post...");
        log.info(memberJoinDTO);

        try {
            memberRepository.updatePassword(passwordEncoder.encode(memberJoinDTO.getMpw()), memberJoinDTO.getMid());
        } catch (Exception e){
            //redirectAttributes.addFlashAttribute("error", "mid");
            return "redirect:/member/modify";
        }

        redirectAttributes.addFlashAttribute("result", "success");

        //return "redirect:/board/list";
        return "redirect:/member/login";    //회원정보 수정 후 로그인
    }

}
