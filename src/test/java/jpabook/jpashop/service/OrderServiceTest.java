package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderService orderService;
    @Autowired
    EntityManager em;

    @Test
    public void 상품주문() throws Exception {
        // given
        Member member = createMember();
        Item item = createItem(10000, 100, "나태주");

        // when
        int count = 5;
        Long orderId = orderService.order(member.getId(), item.getId(), count);

        // then
        Order order = orderRepository.findOne(orderId);
        assertEquals(order.getStatus(), OrderStatus.ORDER, "상품 주문시 주문상태는 ORDER");
        assertEquals(order.getTotalPrice(), count * 10000, "주문 가격은 가격 * 수량이다");
        assertEquals(item.getStockQuantity(), 100 - count, "주문 수량 많큼 재고가 줄어야한다.");
        for (OrderItem orderItem : order.getOrderItems()) {
            assertEquals(orderItem.getItem(), item, "OrderItem에서 주문한 Item이 같은지 확인");
            assertEquals(orderItem.getOrder(), orderRepository.findOne(orderId), "OrderItem에서 주문한 Order가 같은지 확인");
        }
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {
        // given
        Member member = createMember();
        Item item = createItem(10000, 5, "나태주");

        // when
        int count = 10;
        Long orderId = 0L;
        try {
            orderId = orderService.order(member.getId(), item.getId(), count);
        } catch (NotEnoughStockException e) {
            return;
        }

        // then
        fail("재고 수량 부족 Exception이 발생했었어야 한다.");
    }

    @Test
    @Transactional
    public void 주문취소() throws Exception {
        // given
        Member member = createMember();
        Item item = createItem(10000, 100, "나태주");

        // when
        int count = 10;
        Long orderId = 0L;
        try {
            orderId = orderService.order(member.getId(), item.getId(), count);
        } catch (NotEnoughStockException e) {
            return;
        }
        // when
        orderService.cancelOrder(orderId);

        // then
        Order order = orderRepository.findOne(orderId);
        assertEquals(item.getStockQuantity(), 100, "주문이 취소되면 재고는 원래 개수대로 돌아와야 한다.");
        assertEquals(order.getStatus(), OrderStatus.CANCEL, "주문이 취소되면 주문 상태는 CANCEL 이어야 한다.");
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void 주문내역_조회() throws Exception {
        // given
        Member member = createMember();
        Item item = createItem(10000, 100, "나태주");

        // when
        int count = 10;
        Long orderId = 0L;
        try {
            orderId = orderService.order(member.getId(), item.getId(), count);
        } catch (NotEnoughStockException e) {
            return;
        }
        // when
        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setOrderStatus(OrderStatus.ORDER);
        orderSearch.setMemberName("정석우");

        List<Order> orders = orderService.findOrders(orderSearch);

        // then
        assertEquals(orders.size(), 1, "주문을 하나 했으니 주문내역 조회했을 때 주문개수도 1");
        assertEquals(orders.get(0), orderRepository.findOne(orderId), "하나의 주문내역의 객체는 일치");
    }

    private Item createItem(int price, int stockQuantity, String author) {
        Book item = new Book();
        item.setName("꽃을 보듯 너를 본다");
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
        item.setAuthor("나태주");
        item.setIsbn("12345");
        em.persist(item);

        return item;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("정석우");
        member.setAddress(new Address("성남시", "분당구", "430"));
        em.persist(member);
        return member;
    }
}