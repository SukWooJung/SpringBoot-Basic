package jpabook.jpashop.service;

import jpabook.jpashop.domain.Item;
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

    // 상품 조회
    public Item findItem(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    // 상품 목록 조회
    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    // 재고 증가
    public void addQuantity(Long id, int plusQuantity){
        Item find = findItem(id);
        find.addStock(plusQuantity);
    }

    // 재고 감소
    public void minusQuantity(Long id, int minusQuantity){
        Item find = findItem(id);
        find.removeStock(minusQuantity);
    }
}
