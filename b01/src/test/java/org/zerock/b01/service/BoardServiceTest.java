package org.zerock.b01.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister(){

        BoardDTO boardDTO = BoardDTO.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .writer("테스트 작성자")
                .build();
        Long bno = boardService.register(boardDTO);
        log.info("bno : " + bno);

    }

    @Test
    public void testReadOne(){

        BoardDTO boardDTO = boardService.readOne(101l);
        log.info("boardDTO : " + boardDTO);

    }


    //@Transactional
    @Test
    public void testModify(){

        //변경에 필요한 데이터만
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(98L)
                .title("Update...999L")
                .content("Update content 999L") //2개만 바꾸도록 설정해 놓았으므로
                .build();

        //첨부파일 하나 추가
        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID() + "_zzz.jpg"));

        boardService.modify(boardDTO);

    }

    @Test
    public void testRemove(){

        boardService.remove(101L);

    }

    @Test
    public void testList(){

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);

    }

    @Test
    public void testRegisterWithImages(){
        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("File. Sample Title...")
                .content("Sample Content...")
                .writer("user00")
                .build();

        boardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID() + "_aaa.jpg",
                        UUID.randomUUID() + "_bbb.jpg",
                        UUID.randomUUID() + "_bbb.jpg"
                )
        );

        Long bno = boardService.register(boardDTO);

        log.info("bno : " + bno);

    }

    @Test
    public void testReadAll(){

        Long bno = 101L;

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        for(String fileName : boardDTO.getFileNames()){
            log.info(fileName);
        }

    }

    @Test
    public void testRemoveAll(){
        Long bno = 98L;
        boardService.remove(bno);   //이미지와 게시글 모두 삭제(CascadeType.All, orphanRemoval true). Hibernate는 이미지부터 삭제
    }

    @Test
    public void testListWithAll(){

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);

        List<BoardListAllDTO> dtoList = responseDTO.getDtoList();

        dtoList.forEach(boardListAllDTO -> {
            log.info(boardListAllDTO.getBno() + " : " + boardListAllDTO.getTitle());

            if(boardListAllDTO.getBoardImages() != null){
                for(BoardImageDTO boardImage : boardListAllDTO.getBoardImages()){
                    log.info(boardImage);
                }
            }

            log.info("------------------------------------------------");
        });
    }

}