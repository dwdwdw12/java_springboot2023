package org.zerock.b01.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.b01.domain.Board;
import org.zerock.b01.repository.search.BoardSearch;

import java.util.List;
import java.util.Optional;

//ctrl+shift+t : test 생성
public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {   //JpaRepository<테이블, 기본키의 타입>
                                                                    //구현 클래스 생성 후 BoardSearch 인터페이스 추가하기
    List<Board> findBoardByWriter(String writer);       //쿼리메소드, find 엔티티이름 By 변수이름. 엔티티 이름은 생략가능
    //List<Board> findByWriter(String writer);

    List<Board> findByWriterAndTitle(String writer, String title);  //동시에 두 조건을 만족

    List<Board> findByTitleLike(String title);

    @Query(value = "select * from board where title like %:title% order by bno desc", nativeQuery = true)   //sql 구문 그대로 사용하는 방법. 잘 쓰지 않음
    List<Board> findByTitle2(@Param("title") String title);

    @Query("select i from Board i where i.writer = :writer")        //같은 것을 찾을 때는 :writer 사용
    List<Board> findByWriter2(@Param("writer") String writer);

    @Query("select b from Board b where b.title like %:title% order by b.bno desc")
    List<Board> findByTitle(@Param("title") String title);

    @Query("select b from Board b where b.title like concat('%',:keyword,'%')")
    Page<Board> findKeyword(@Param("keyword")String keyword, Pageable pageable);

    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    @EntityGraph(attributePaths = {"imageSet"}) // join이 걸림
    @Query("select b from Board b where b.bno=:bno")
    Optional<Board> findByIdWithImages(@Param("bno") Long bno);



}
