package org.zerock.b01.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
public class BoardImage implements Comparable<BoardImage> { //연관관계 주인

    @Id
    private String uuid;

    private String fileName;

    private int ord;

    @ManyToOne
    private Board board;    //board의 기본키로 바뀜. board_bno

    @Override
    public int compareTo(BoardImage other) {
        return this.ord-other.ord;
    }

    public void changeBoard(Board board){   // 게시글 삭제시, 파일 처리. 아마 null값으로 처리할 예정.
        this.board = board;
    }

}
