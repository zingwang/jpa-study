package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Test
    public void 상품주문() throws  Exception{
        Member member = createMember();

        Book book = createBook("시골 JPA",10000,10);

        //when
        int orderCount=2;
       Long orderId= orderService.order(member.getId(), book.getId(),orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        Assert.assertEquals("상품주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        Assert.assertEquals("상품주문시 종류수는 정확", 1, getOrder.getOrderItems().size());
        Assert.assertEquals("주문 가격은 가격*수량 ", 10000* orderCount, getOrder.getTotalPrice());

        Assert.assertEquals("주문 수량만큼 재고가 줄어야함 ", 8, book.getStockQuantity());
        //Assert.assertEquals("주문 수량만큼 재고가 줄어야함 오류확인", 6, book.getStockQuantity());
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        //given
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강가","123-123"));
        em.persist(member);
        return member;
    }
    @Test(expected =  NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws  Exception{

        //given
        Member member = createMember();
        Item item = createBook("시골 JPA",10000,10);
        int orderCount=11;
        //when
        orderService.order(member.getId(), item.getId(), orderCount);

        //then
        fail("재고수량 부족 예외 발생");
    }
    @Test
    public void 주문취소() throws  Exception{
        //given

        //when

        //then
    }


}