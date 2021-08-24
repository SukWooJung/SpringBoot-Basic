package jpabook.jpashop.service;

import jpabook.jpashop.domain.Item;
import jpabook.jpashop.domain.dto.BookDTO;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    // 상품 등록
    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    // 상품 변경
    @Transactional
    public void updateItem(Long itemId, BookDTO bookDTO){
        Book book = (Book) itemRepository.findOne(itemId);
        book.setName(bookDTO.getName());
        book.setPrice(bookDTO.getPrice());
        book.setStockQuantity(bookDTO.getStockQuantity());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());
    }

    // 상품 조회
    public Item findItem(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    // 상품 목록 조회
    public List<Item> findItems() {
        return itemRepository.findAll();
    }
}
