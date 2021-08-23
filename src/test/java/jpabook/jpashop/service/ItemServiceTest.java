package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemService itemService;

    @Test
    @Rollback(value = false)
    public void 아이템_조회() throws Exception {
        // given
        Book book = new Book();
        book.setName("꽃을 보든 너를 본다");
        book.setPrice(10000);

        // when
        itemService.saveItem(book);

        // then
        assertEquals(book, itemRepository.findOne(book.getId()));
    }

}