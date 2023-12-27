package org.zerock.b01.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.BoardListAllDTO;
import org.zerock.b01.dto.BoardListReplyCountDTO;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void testInsert(){
        for(int i=1; i<=100; i++) {
            Board board = Board.builder()
                    .title("title..." + i)
                    .content("content..." + i)
                    .writer("user" + (i%10))
                    .build();
            Board result = boardRepository.save(board);
            log.info("BNO : " + result.getBno());
        }
    }

    @Test
    public void testSelect(){
        Long bno = 100L;
        Optional<Board> result = boardRepository.findById(bno); //null값일 경우도 대비

        Board board = result.orElseThrow(); //null값이 아니면 Board에 집어넣음
        //Board board = boardRepository.findById(bno).orElseThrow();        //1줄로 나타냄

        log.info(board);
    }

    @Test
    public void testUpdate(){       //sql문이 3번 사용됨(select-select-update, findById에서 1번, update에서 2번)
        Long bno = 100L;

        Board board = boardRepository.findById(bno).orElseThrow();
        log.info("board1>>>" + board);

        board.change("update22...title 100", "update22 content 100");
        log.info("board2>>>" + board);

        boardRepository.save(board);
        log.info("board3>>>" + board);
    }

    @Test
    public void testDelete(){      //select로 찾고, delete로 지움
        boardRepository.deleteById(1L);
    }

    @Test
    public void testGetList(){
        boardRepository.findAll().forEach(board->log.info(board));
    }

    @Test
    public void testPaging(){
        //1 page order by bno desc
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.findAll(pageable);

        log.info("total count" + result.getTotalElements());
        log.info("total page" + result.getTotalPages());
        log.info("page number" + result.getNumber());
        log.info("page size" + result.getContent());

        result.getContent().forEach(board->log.info(board));
    }

    @Test
    public void testWriter(){
        boardRepository.findBoardByWriter("user1").forEach(board->log.info(board));
    }

    @Test
    public void testFindByWriterAndTitle(){
        boardRepository.findByWriterAndTitle("user9", "title...39").forEach(board->log.info(board));
    }

    @Test
    public void testFindByTitleLike(){
        boardRepository.findByTitleLike("%1%").forEach(board->log.info(board));
    }

    @Test
    public void testWriter2(){
        boardRepository.findByWriter2("user1").forEach(board->log.info(board));
    }

    @Test
    public void testFindByTitle(){
        boardRepository.findByTitle("1").forEach(board->log.info(board));
    }

    @Test
    public void testFindByTitle2(){
        boardRepository.findByTitle2("1").forEach(board->log.info(board));
    }

    @Test
    public void testFindKeyword(){
        Pageable pageable = PageRequest.of(1,10,Sort.by("bno").descending());   //0페이지 부터
        //boardRepository.findKeyword("1", pageable).forEach(board->log.info(board));

        Page<Board> result = boardRepository.findKeyword("1", pageable);
        log.info("total elements"+result.getTotalElements());
        log.info("total pages"+result.getTotalPages());
        result.getContent().forEach(board->log.info(board));
    }

    @Test
    public void testSearch1(){
        //2 page order by bno desc
        Pageable pageable = PageRequest.of(1,10,Sort.by("bno").descending());

        boardRepository.search1(pageable);
    }

    @Test
    public void testSearchAll(){

        String[] types = {"t", "c", "w"};
        String keyword = "1";

        Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        log.info("total element : " + result.getTotalElements());
        log.info("total pages : " + result.getTotalPages());
        log.info("size : " + result.getSize());
        log.info("number : " + result.getNumber());
        log.info("has next? : " + result.hasNext());    //<-> hasPrevious

    }

    @Test
    public void testSearchReplyCount(){

        String[] types = {"t", "c", "w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

        log.info(result.getTotalPages());

        log.info(result.getSize());

        log.info(result.getNumber()); //page number

        log.info(result.hasPrevious() + " : " + result.hasNext());

        result.getContent().forEach(board -> log.info(board)); //result.getContent().forEach(log::info); 메서드 참조로 고친 형태

    }

    @Test
    public void testInsertWithImages(){
        Board board = Board.builder()
                .title("Image Test")
                .content("첨부파일 테스트")
                .writer("tester")
                .build();

        for(int i=0; i<3; i++){
            board.addImage(UUID.randomUUID().toString(), "file" + i + ".jpg");
        }// end for

        boardRepository.save(board);
    }

    @Test
    //@Transactional
    public void testReadWithImages(){

        //존재하는 bno로 확인하기
        //Board board = boardRepository.findById(1L).orElseThrow();
        Board board = boardRepository.findByIdWithImages(1L).orElseThrow();

        log.info(board);
        log.info("--------------------------");
        log.info(board.getImageSet());      // no session. db의 연결이 끝난 상태이므로 결과를 얻을 수 없음.
                                            // -> Fetch 전략을 eager로 바꾸거나(비권장). @Transactioanl(@Test 아래), @EntityGraph(BoardRepository의 method) 사용
                                            //@EntityGraph(BoardRepository의 method) 사용 시, 조인 상태로 처리
    }

    @Transactional
    @Commit
    @Test
    public void testModifyImages(){
        Board board = boardRepository.findByIdWithImages(1L).orElseThrow();

        //기존 첨부파일 삭제
        board.clearImages();

        //새로운 첨부파일들
        for(int i=0; i<2; i++){
            board.addImage(UUID.randomUUID().toString(), "updatefile2" + i + ".jpg");
        }

        boardRepository.save(board);

    }

    @Test
    @Transactional
    @Commit
    public void testRemoveAll(){
        Long bno = 1L;

        replyRepository.deleteByBoard_Bno(bno); //외래키가 참조하고 있어서 삭제가 안 됨.

        boardRepository.deleteById(bno);

    }

    @Test
    public void testInserAll(){
        IntStream.rangeClosed(1, 100).forEach(i ->{
            Board board = Board.builder()
                    .title("Title..." +  i)
                    .content(("Content..." + i))
                    .writer("writer..." + i)
                    .build();


        for(int j=1; j<3; j++){
            if(i%5==0){
                continue;
            }
            board.addImage(UUID.randomUUID().toString(), i + "file" + j + ".jpg");
        }
        boardRepository.save(board);

        });//end  for
    }

    @Transactional
    @Test
    public void testSearchImageReplyCount(){

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        //boardRepository.searchWithAll(null,null, pageable); //@BatchSize 미설정시, 각 게시글마다 board_image에 대한 쿼리가 실행됨.

        Page<BoardListAllDTO> result = boardRepository.searchWithAll(null, null, pageable);

        log.info("==================================");
        log.info(result.getTotalElements());

        result.getContent().forEach(boardListAllDTO -> log.info(boardListAllDTO));

    }

}