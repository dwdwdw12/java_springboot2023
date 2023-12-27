package org.zerock.b01.domain;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})    //시간 변동시 catch
@Getter
public class BaseEntity {

    @CreatedDate
    @Column(name = "regdate", updatable = false)    //업데이트 불가
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column(name = "moddate")       //없으면 mod_date의 형태로 칼럼명 생성
    private LocalDateTime modDate;

}
