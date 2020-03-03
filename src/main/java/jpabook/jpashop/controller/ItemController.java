package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form) {

        Book book = new Book(); // 나중에 setter 없애기
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItem();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    // 수정 중요 (**변경감지**냐 병합(merge)이냐) 차이를 꼭 알아야함 *****
    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId);

        // entity 대신 book form 보내기
        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@PathVariable("itemId") Long itemId, @ModelAttribute("form") BookForm form) {

//        // id 값이 있는 경우- 식별자 존재 (인스턴스만 새겻 데이터는 이미 db에 갔다왔음) => 준영속성 컨텍스트 - 더이상 관리 하지 않음
        // 컨트롤러에서 Entity를 생성하지 말것
//        Book book = new Book();
//
//        book.setId(form.getId());
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setIsbn(form.getIsbn());
//
//        // 1. 변경감지(dirty checking)
//        // 2. 병합(merge)
//
//        itemService.saveItem(book);
        // ==>> 나은 방법
        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        return "redirect:/items";
    }
}
