package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    public void testCategory () throws Exception {
        // given
        Category parent = new Category();
        parent.setName("부모");

        Category child1 = new Category();
        child1.setName("자식1");
        Category child2 = new Category();
        child2.setName("자식2");

        parent.addChildCategory(child1);
        parent.addChildCategory(child2);

        categoryRepository.save(child1);
        categoryRepository.save(child2);
        categoryRepository.save(parent);

        // when
        Category find = categoryRepository.find(parent.getId());

        // then
        for (Category child : find.getChild()) {
            System.out.println("child.getName() = " + child.getName());
        }
    }

}