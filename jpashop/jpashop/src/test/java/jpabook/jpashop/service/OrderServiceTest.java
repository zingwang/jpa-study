package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
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
        //given
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강가","123-123"));
        em.persist(member);

        Book book = new Book();
        book.setName("시골 JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

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
    @Test
    public void 주문취소() throws  Exception{
        //given

        //when

        //then
    }
    @Test
    public void 상품주문_재고수량초과() throws  Exception{
        //given

        //when

        //then
    }

}