package jpabook.jpashop.service;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
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
public class ItemServiceTest {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemService itemService;

    @Test
    public void saveItem() throws Exception {
        // given
        Book book = new Book();
        book.setName("jm`s book");
        book.setStockQuantity(500);
        book.setPrice(50000);

        // when
        Long savedId = itemService.saveItem(book);

        System.out.println("savedId >>>>>>>>>>>>>>>> " + savedId);
        Item r = itemRepository.findOne(savedId);
        System.out.println(r.toString());

        // then
        assertEquals(book, itemRepository.findOne(savedId));

    }

    @Test
    public void findItem() {
    }

    @Test
    public void findOne() {
    }
}