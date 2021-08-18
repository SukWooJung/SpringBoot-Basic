package jpabook.jpashop.domain;

import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @OneToMany(mappedBy = "item")
    private List<ItemCategory> categories = new ArrayList<>();

    public void addStock(int quantity){
        stockQuantity += quantity;
    }

    public void removeStock(int quantity){
        if(stockQuantity - quantity < 0){
            throw new NotEnoughStockException("need more stock");
        }
        stockQuantity -= quantity;
    }

}
