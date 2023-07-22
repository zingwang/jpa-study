package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Order;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class,id);
    }

}


