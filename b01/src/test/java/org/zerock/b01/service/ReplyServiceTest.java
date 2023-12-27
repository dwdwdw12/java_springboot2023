package org.zerock.b01.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.ReplyDTO;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class ReplyServiceTest {

    @Autowired
    private ReplyService replyService;

    @Test
    public void testRegister(){

        ReplyDTO replyDTO = ReplyDTO.builder()
                .replyText("ReplyDTO Text")
                .replyer("replyer")
                .bno(99L)
                .build();

        log.info(replyService.register(replyDTO));

    }

    @Test
    public void testRead(){

        log.info(replyService.read(6L));

    }

    @Test
    public void testModify(){

        ReplyDTO replyDTO = ReplyDTO.builder()
                .replyText("reply modify test")
                .rno(6L)
                .build();

        replyService.modify(replyDTO);

    }

    @Test
    public void testRemove(){

        replyService.remove(8L);

    }

    @Test
    public void testGetListOfBoard(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();
        PageResponseDTO<ReplyDTO> listOfBoard = replyService.getListOfBoard(100L, pageRequestDTO);
        log.info(listOfBoard);
    }

    
}