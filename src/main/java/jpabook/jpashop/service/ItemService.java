package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public Long saveItem(Item item){
        itemRepository.save(item);
        return item.getId();
    }

    // 변경 감지 - dirty checking (merge 보다 나은 방법)
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        // 영속성으로 변경으로 자동으로 update 처리 (선별적인 데이터 set 가능)
        Item findItem = itemRepository.findOne(itemId);

        // setter를 사용하지 말고 method 로 묶어서 처리를 권장 (아래와 같은 식)
        // findItem.change(param.getPrice(), param.getName(), param.getStockQuantity());
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);
        // 필요한 항목만 추가
    }

    // merge (변경 감지와 동일하지만 자동으로 처리되며 영속성 처리된 item을 리턴함) - 하지만 자동으로 모든 필드의 데이터를 교체 - 특정 컬럼이 null 이 될 가능성이 있음
    @Transactional
    public Item updateItemMerge(Long itemId, Book param) {
        // 영속성으로 변경으로 자동으로 update 처리
        Item findItem = itemRepository.findOne(itemId);
//        findItem.setPrice(param.getPrice()); //==> 이와같은 경우 book.price = null; 이 되므로 가격정보가 날아간다. 선별적으로 업데이트 할 수 없음
        findItem.setName(param.getName());
        findItem.setStockQuantity(param.getStockQuantity());

        return findItem;
    }

    public List<Item> findItem() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
