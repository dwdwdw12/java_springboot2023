package com.dongwoo;

import com.dongwoo.constant.ItemSellStatus;
import com.dongwoo.entity.Item;
import com.dongwoo.entity.QItem;
import com.dongwoo.repository.ItemRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("상품 저장 테스트")    //선택
    public void createItemTest(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item); //update 역할
        System.out.println(savedItem.toString());

    }

    @Test
    @DisplayName("상품 조회 테스트")    //선택
    public void findItemTest(){
        createItemTest();
        List<Item> item = itemRepository.findAll();
        System.out.println("=====================");
        System.out.println(item.get(0).toString());

        //Item item = itemRepository.findOne(Long );
        List<Item> item2 = itemRepository.findByItemNm("테스트 상품");
        System.out.println("=====================");
        System.out.println(item2.get(0).toString());

        List<Item> item3 = itemRepository.findByStockNumber(100);
        System.out.println("=====================");
        System.out.println(item3.get(0).toString());

        System.out.println("전체 자료의 개수 : " + itemRepository.count());
    }

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest(){
        this.createItemTest();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());
        List<Item> itemList = query.fetch();

        for(Item item : itemList){
            System.out.println("=====================");
            System.out.println(item.toString());
            System.out.println("=====================");
        }
    }

    @BeforeTestClass
    public void createItemList2(){
        for(int i=1;i<11;i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000+i);
            item.setItemDetail("테스트 상품 상세 설명"+i);
            item.setItemSellStatus( i<6 ? ItemSellStatus.SELL:ItemSellStatus.SOLD_OUT);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품 Querydsl 조회 테스트 2")
    public void queryDslTest2(){
        this.createItemList2();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;
        String itemDetail = "테스트 상품 상세 설명";
        int price = 10003;
        String itemSellStat = "SELL";

        booleanBuilder.and(item.itemDetail.like("%"+itemDetail+"%"));
        booleanBuilder.and(item.price.gt(price));

        if(StringUtils.equals(itemSellStat, ItemSellStatus.SELL)){
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0,5);
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements : " + itemPagingResult.getTotalElements());

        List<Item> resultItemList = itemPagingResult.getContent();
        for(Item resultItem: resultItemList){
            System.out.println(resultItem.toString());
        }
    }


    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemNmOrItemDetail(){
        this.createItemList2();
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1","테스트 상품 상세 설명5");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }


}
