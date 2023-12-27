package org.zerock.b01.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.QBoard;
import org.zerock.b01.domain.QReply;
import org.zerock.b01.dto.BoardImageDTO;
import org.zerock.b01.dto.BoardListAllDTO;
import org.zerock.b01.dto.BoardListReplyCountDTO;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
//검색, 페이지 처리 용도
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch{

    public BoardSearchImpl(/*Class<?> domainClass*/) {
        //super(domainClass);
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {

        QBoard board = QBoard.board; //Q 도메인 객체

        JPQLQuery<Board> query = from(board);   //select * from board 와 동일

        query.where(board.title.contains("1")); //where title like '%1%'

        //paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();

        Long count = query.fetchCount();

        log.info("count : " + count);
        list.forEach(board1->log.info(board1));

        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);   //select * from board

        if((types!=null && types.length>0) && keyword!=null){
            BooleanBuilder booleanBuilder = new BooleanBuilder();   //and가 or보다 먼저 연산. 우선순위를 괄호로 묶어주기 위해
                                                                    //조건을 append 하는 역할
            for(String type : types){
                switch (type){
                    case "t" :
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c" :
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w" :
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            } //end for
            query.where(booleanBuilder);
        } //end if

        //bno>0
        query.where(board.bno.gt(0L));  //greater than

        //paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();

        Long count = query.fetchCount();

//        log.info("count : " + count);
//        list.forEach(board1->log.info(board1));

        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> query = from(board);   //select * from board
        query.leftJoin(reply).on(reply.board.eq(board));    //left outer join

        query.groupBy(board);

        if((types!=null && types.length > 0 ) && keyword != null){
            BooleanBuilder booleanBuilder = new BooleanBuilder();   //and가 or보다 먼저 연산. 우선순위를 괄호로 묶어주기 위해
            //조건을 append 하는 역할
            for(String type : types){
                switch (type){
                    case "t" :
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c" :
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w" :
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            } //end for
            query.where(booleanBuilder);
        } // end if

        //bno>0
        query.where(board.bno.gt(0L));  //greater than

        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(
                            Projections.bean(BoardListReplyCountDTO.class,  //가져온 정보를 매핑시켜주는 역할. //JPQL의 결과를 바로 DTO로 처리
                                        board.bno,
                                        board.title,
                                        board.writer,
                                        board.regDate,
                                        reply.count().as("replyCount")
                                ));

        this.getQuerydsl().applyPagination(pageable, dtoQuery);

        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();

        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);

    }

    //게시글과 댓글 정보 검색해서 BoardListAllDTO로 전달
    @Override
    public Page</*BoardListReplyCountDTO*/BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> boardJPQLQuery = from(board);
        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board));   //left join

        //검색조건
        if((types!=null && types.length > 0 ) && keyword != null){
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for(String type : types){
                switch (type){
                    case "t" :
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c" :
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w" :
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            } //end for
            boardJPQLQuery.where(booleanBuilder);
        } // end if

        boardJPQLQuery.groupBy(board);

        getQuerydsl().applyPagination(pageable, boardJPQLQuery); //paging

        JPQLQuery<Tuple> tupleJPQLQuery = boardJPQLQuery.select(board, reply.countDistinct());  //댓글 갯수 조회

        List<Tuple> tupleList = tupleJPQLQuery.fetch(); //board의 정보를 담고 있음

        List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {
           Board board1 = (Board) tuple.get(board);
           long replyCount = tuple.get(1, Long.class);  //index에 대한 정보를 찍어줌(댓글의 갯수)

           BoardListAllDTO dto = BoardListAllDTO.builder()
                   .bno(board1.getBno())
                   .title(board1.getTitle())
                   .writer(board1.getWriter())
                   .regDate(board1.getRegDate())
                   .replyCount(replyCount)
                   .build();

           //BoardImage를 BoardImageDTO 처리할 부분
           List<BoardImageDTO> imageDTOS = board1.getImageSet().stream().sorted()
                   .map(boardImage -> BoardImageDTO.builder()
                           .uuid(boardImage.getUuid())
                           .fileName(boardImage.getFileName())
                           .ord(boardImage.getOrd())
                           .build()
                   ).collect(Collectors.toList());

           dto.setBoardImages(imageDTOS);   //처리된 BoardImageDTO들을 추가

           return dto;

        }).collect(Collectors.toList());

        long totalCount = boardJPQLQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, totalCount);

//        List<Board> boardList = boardJPQLQuery.fetch();
//
//        boardList.forEach(board1 -> {
//            System.out.println(board1.getBno());
//            System.out.println(board1.getImageSet());
//            System.out.println("--------------------------");
//        });
//        return null;
    }


}
