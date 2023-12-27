package org.zerock.b01.service;

import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    //댓글의 숫자까지 처리
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

    //게시글의 이미지와 댓글의 숫자까지 처리
    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);
        //(반환타입=response)

    default Board dtoToEntity(BoardDTO boardDTO){
        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())
                .build();

        if(boardDTO.getFileNames() != null){
            boardDTO.getFileNames().forEach(fileName -> {
                //String[] arr = fileName.split("_");
                //String[] arr = fileName.split("_", 2); //이런 문자열이 기입이 되면 첫번째_에서만 문자열을 분리함.
                //board.addImage(arr[0], arr[1]); //arr[0] : uuid, arr[1] : original file name

                int i = fileName.indexOf("_");
                board.addImage(fileName.substring(0,i), fileName.substring(i+1));   //파일 이름에 언더바(_)가 있을 때. uuid, original file name(확장자 포함)

            });
        }

        return board;
    }

    default BoardDTO entityToDTO(Board board){
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        List<String> fileNames = board.getImageSet().stream().sorted().map(boardImage ->
                boardImage.getUuid() + "_" + boardImage.getFileName()).collect(Collectors.toList());

        boardDTO.setFileNames(fileNames);

        return boardDTO;
    }


    default Board boardDTOEntity(BoardDTO boardDTO){ //private->public 변경. default 붙이면 하위 클래스에서 선택적으로 구현
        //인터페이스에 새로운 메서드를 추가할 때, 모든 하위클래스에서 생성하는 일을 피하고 싶음 -> default : 선택적으로 구현

        Board board = Board.builder()                  //또는 mapper 사용(463p)
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())
                .build();
        return board;
    }

    default BoardDTO entityToBoardDTO(Board board){
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        return boardDTO;
    }

}
