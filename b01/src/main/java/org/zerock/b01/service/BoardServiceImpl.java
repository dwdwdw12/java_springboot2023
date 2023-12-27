package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.*;
import org.zerock.b01.repository.BoardRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional      //추가. 지연 로딩된 엔티티나 컬렉션을 초기화할 때 필요한 데이터베이스 연결을 해결.
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;

    private final ModelMapper modelMapper;

    @Override
    public Long register(BoardDTO boardDTO){

    //    Board board = modelMapper.map(boardDTO, Board.class);   //RootConfig가 동작, 왼쪽의 내용을 오른쪽으로 바꿔줌
        Board board = dtoToEntity(boardDTO);

        Long bno = boardRepository.save(board).getBno();

        return bno;

    }

    @Override
    public BoardDTO readOne(Long bno) {
        Board board = boardRepository.findById(bno).orElseThrow();
        //Board board = boardRepository.findByIdWithImages(bno).orElseThrow();

        //BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
        BoardDTO boardDTO = entityToDTO(board);

        return boardDTO;
    }

    @Override
    public void modify(BoardDTO boardDTO) {

        //Board board = boardRepository.findById(boardDTO.getBno()).orElseThrow();
        Board board = boardRepository.findByIdWithImages(boardDTO.getBno()).orElseThrow();

        board.change(boardDTO.getTitle(), boardDTO.getContent());

        //첨부파일의 처리. 모두 지운 후에 새로 추가
        board.clearImages();

        if(boardDTO.getFileNames() != null){
            for(String fileName : boardDTO.getFileNames()){
                //String[] arr = fileName.split("_");
                //String[] arr = fileName.split("_", 2); //이런 문자열이 기입이 되면 첫번째_에서만 문자열을 분리함.
                //board.addImage(arr[0], arr[1]);

                int i = fileName.indexOf("_");
                board.addImage(fileName.substring(0,i), fileName.substring(i+1));   //파일 이름에 언더바(_)가 있을 때. uuid, original file name(확장자 포함)
            }
        }

        boardRepository.save(board);

    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);
    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

//        result.getTotalElements();
//        result.getTotalPages();
//        result.getContent().forEach(board->log.info(board));

        List<BoardDTO> dtoList = result.getContent().stream().map(board->modelMapper.map(board, BoardDTO.class)).collect(Collectors.toList());

        return PageResponseDTO.<BoardDTO>withAll()          //PageResponseDTO에서 builderMethodName으로 withAll을 지정해 주었음
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

        return PageResponseDTO.<BoardListReplyCountDTO>withAll()          //PageResponseDTO에서 builderMethodName으로 withAll을 지정해 주었음
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO) {    //게시글, 이미지

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListAllDTO> result = boardRepository.searchWithAll(types, keyword, pageable);

        return PageResponseDTO.<BoardListAllDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }


//    @Override
//    public Long register(BoardDTO boardDTO) {
//        Board board = boardDTOEntity(boardDTO); //2.변환과정이 필요
//
//        //boardRepository.save(boardDTO);   //1.바로 넣어줄 수 없음. db 손상 위험 회피
//        Long bno = boardRepository.save(board).getBno();
//        return bno;
//    }

//    @Override
//    public BoardDTO readOne(Long bno) {
//        Board board = boardRepository.findById(bno).orElseThrow();
//
//        BoardDTO boardDTO = entityToBoardDTO(board);
//
//        return boardDTO;
//    }


}
