package com.dongwoo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dongwoo.entity.Item;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
               //JpaRepository 또는 CrudRepository 상속받기  //JpaRepository: 페이지 처리 가능. CrudRepository보다 기능이 많음.
public interface ItemRepository extends JpaRepository<Item, Long> ,
                        QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {     //클래스와 기본키 type을 써주기
    public List<Item> findByItemNm(String itemNM);

    //public List<Item> findByUser(String user);
    public List<Item> findByStockNumber(int stockNumber);

    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
}
