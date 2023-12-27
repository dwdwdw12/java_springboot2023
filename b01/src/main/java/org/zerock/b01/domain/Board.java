package org.zerock.b01.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter                                         //setter 안 쓰는 이유 : DB에 손상을 줄 수 있으므로
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageSet")
public class Board extends BaseEntity {         //상속을 받으면, 생성 및 수정 시간이 추가됨

    @Id                 //테이블 생성시 필수
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 500, nullable = false) //칼럼 길이, null 허용 여부
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    public void change(String title, String content){
        this.title = title;
        this.content = content;
    }
                                    //조회만 가능
    @OneToMany(mappedBy = "board"  //BoardImage의 board 변수. 없으면 board 테이블과 boardImage 테이블 사이에 매핑 테이블이 생성됨.
            ,cascade = {CascadeType.ALL}    //상위 엔티티의 모든 변경 사항이 하위 엔티티에 반영
            ,fetch = FetchType.LAZY
            ,orphanRemoval = true   //없으면 삭제되지 않고, null 상태로 있음
    )
    @Builder.Default
    @BatchSize(size = 20)   //20개씩 모아서 처리
    private Set<BoardImage> imageSet = new HashSet<>();

    public void addImage(String uuid, String fileName){
        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size())
                .build();
        imageSet.add(boardImage);
    }

    public void clearImages(){
        imageSet.forEach(boardImage -> boardImage.changeBoard(null));

        this.imageSet.clear();
    }

}
