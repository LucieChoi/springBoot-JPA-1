package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /**
     * 상품 등록 폼에서 데이터를 입력하고 submit 버튼을 클릭하면
     * /items/new를 POST 방식으로 요청
     */
    @GetMapping("/items/new")
    public String createForm(Model model) {
                                                // 빈 폼을 넘겨주어 추적이 가능하게 함
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    /**
     * 상품 저장이 끝나면 상품 저장 목록 화면 redirect:/items로 리다이렉트
     */
    @PostMapping("/items/new")
    public String create(BookForm form) {

        // 직접 엔티티 쓰지 않음
        Book book = new Book();
        // setter은 가능한 한 쓰지 않는 게 좋음
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/items";
    }

    /**
     * model에 담아둔 상품 목록인 items를 꺼내서 상품 정보 출력
     */
    @GetMapping("/items")
    public String list(Model model){

        List<Item>items=itemService.findItems();
        model.addAttribute("items",items);
        return "items/itemList";
    }
}
