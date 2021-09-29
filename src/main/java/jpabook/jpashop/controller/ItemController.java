package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
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
    public String list(Model model) {

        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    /**
     * 상품 수정 폼 이동 *
     * 1. 수정 버튼을 선택하면 /items/{itemId}/edit URL을 GET 방식으로 요청
     * 2. 그 결과로 updateItemForm() 메서드 실행
     * 이 메서드는 itemService.findOne(itemId)를 호출해서 수정할 상품 조회
     * 3. 조회 결과를 모델 객체에 담아 뷰(items/updateItemForm)에 전달
     */
    @GetMapping("items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {

        Book item = (Book) itemService.findOne(itemId);

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

    /**
     * 상품 수정 실행 *
     * 1. 상품 수정 폼에서 정보를 수정하고 submit 버튼 클릭
     * 2. /items/{itemId}/edit URL을 POST 방식으로 요청하고 updateItem() 메서드 실행
     * 3. 이때 컨트롤러에 파라미터로 넘어온 item 엔티티 인스턴스는 현재 준영속 상태.
     * 따라서 영속성 컨텍스트 지원을 받을 수 없고 데이터를 수정해도 변경 감지가 동작 XX
     *
     * 해결 방법 *
     * 컨트롤러에서 어설프게 엔티티 생성 X
     * 트랜잭션이 있는 서비스 계층에 식별자id 와 변경할 데이터를 명확하게 전달 (파라미터나 DTO)
     * 트랜잭션이 있는 서비스 계층에서 영속 상태의 엔티티를 조회하고, 엔티티의 데이터를 직접 변경
     * 트랜잭션 커밋 시점에 변경 감지가 실행됨!
     */
    @PostMapping("items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") BookForm form, @PathVariable String itemId) {

        itemService.updateItem(form.getId(), form.getName(), form.getPrice());
        return "redirect:/items";
    }
}
