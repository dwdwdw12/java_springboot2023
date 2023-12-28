package com.dongwoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShopApplication {

	public static void main(String[] args) {

		SpringApplication.run(ShopApplication.class, args);
//		ApplicationContext context = GetContext.getAppContext();
//		System.out.println("===============================");
//
//		for(String con : context.getBeanDefinitionNames()){		//빈 목록 조회
//			System.out.println(con);
//		}
//		System.out.println("===============================");

		//ItemSellStatus aa = ItemSellStatus.SELL;

	}

}
