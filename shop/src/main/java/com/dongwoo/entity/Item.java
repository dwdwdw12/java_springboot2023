package com.dongwoo.entity;

import com.dongwoo.constant.ItemSellStatus;
import com.dongwoo.dto.ItemFormDto;
import com.dongwoo.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
//@Table(name = "ShopItem")           //테이블 이름 변경
@Getter @Setter @ToString

@SequenceGenerator(
        name="item_seq_gen", //시퀀스 제너레이터 이름
        sequenceName="item_seq", //시퀀스 이름
        initialValue=1, //시작값
        allocationSize=1 //메모리를 통해 할당할 범위 사이즈
)

public class Item extends BaseEntity{
    @Id
    @Column(name = "shopId")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq_gen") //키값 생성 전략(기본키를 생성하는 방법)
    private Long id;

    @Column(nullable = false, length = 50)
    private String itemNm;

    @Column(nullable = false, name = "price")
    private int price;

    @Column(nullable = false)
    private int stockNumber;
    
    //@Lob        //많은 양의 데이터
    @Column(nullable = false, length = 4000)
    private String itemDetail;
    
    @Enumerated(EnumType.STRING)    //작성하지 않으면 자료를 저장할 때 에러 발생. 테이블 생성시, number(10,0)->varchar2(255 char)로 바뀜
    private ItemSellStatus itemSellStatus;
    
//    private LocalDateTime regTime;
//
//    private LocalDateTime updateTime;

    public void updateTime(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber){
        int restStock = this.stockNumber - stockNumber;
        if(restStock<0){
            throw new OutOfStockException("상품의 재고가 부족 합니다. (현재 재고 수량 : " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }

    public void addStock(int stockNumber){
        this.stockNumber += stockNumber;
    }

}
