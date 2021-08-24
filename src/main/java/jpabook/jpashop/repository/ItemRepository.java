package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Item;
import jpabook.jpashop.domain.form.BookForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    // 상품 등록
    public Long save(Item item) {
        if(item.getId() == null){
            em.persist(item);
        }
        return item.getId();
    }

    // 상품 목록 조회
    public List<Item> findAll() {
        String query = "select i from Item i";
        List<Item> resultList = em.createQuery(query, Item.class).getResultList();
        return resultList;
    }

    // 상품 하나 조회
    public Item findOne(Long id){
        Item findItem = em.find(Item.class, id);
        return findItem;
    }

}
