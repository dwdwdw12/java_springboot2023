package com.dongwoo;

import com.dongwoo.entity.Cart;
import com.dongwoo.entity.Member;
import com.dongwoo.repository.CartRepository;
import com.dongwoo.repository.MemberRepository;
import com.dongwoo.dto.MemberFormDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional  //테스트 실행 후 롤백
@TestPropertySource(locations = "classpath:application.properties")
class CartRepositoryTest {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    @Test
    public void notNullTest(){
        assertNotNull(em);
        assertNotNull(cartRepository);
        assertNotNull(memberRepository);
        assertNotNull(passwordEncoder);
    }

    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");

        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @Rollback(value = false)    //DB반영 후 롤백x
    public void findCartAndMemberTest(){
        Member member = createMember();
        Member saved = memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);

        Cart svc = cartRepository.save(cart);
        em.flush();         //영속성 컨텍스트에 있는 것을 적용해라(실제 테이블에 반영해라)
        em.clear();

        //Cart savedCart = cartRepository.findById(cart.getId())
        //                .orElseThrow(EntityNotFoundException::new);       // :: 메서드 참조 연산자
        Cart savedCart = cartRepository.findById(cart.getId())
                .orElseThrow(()-> new EntityNotFoundException());           //null이면 에러 처리
        assertEquals(savedCart.getMember().getId(), member.getId());

//        System.out.println("========================");
//        System.out.println(saved);
//        System.out.println(svc);
//        System.out.println("=========================");

    }


}