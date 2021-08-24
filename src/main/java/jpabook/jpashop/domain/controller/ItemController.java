package jpabook.jpashop.domain.controller;

import jpabook.jpashop.domain.dto.BookDTO;
import jpabook.jpashop.domain.form.BookForm;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // 상품 등록
    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "/items/createItemForm";
    }

    @PostMapping("/items/new")
    public String finishedForm(BookForm bookForm) {
        Book book = new Book();
        book.setIsbn(bookForm.getIsbn());
        book.setName(bookForm.getName());
        book.setAuthor(bookForm.getAuthor());
        book.setPrice(bookForm.getPrice());
        book.setStockQuantity(bookForm.getStockQuantity());

        itemService.saveItem(book);
        return "redirect:/";
    }

    // 상품 목록
    @GetMapping("/items")
    public String findItems(Model model) {
        model.addAttribute("items", itemService.findItems());
        return "/items/itemList";
    }

    // 상품 수정
    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findItem(itemId);

        BookForm bookForm = new BookForm();
        bookForm.setId(itemId);
        bookForm.setName(item.getName());
        bookForm.setPrice(item.getPrice());
        bookForm.setIsbn(item.getIsbn());
        bookForm.setAuthor(item.getAuthor());
        bookForm.setStockQuantity(item.getStockQuantity());

        model.addAttribute("form", bookForm);
        return "/items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    public String updateItem(BookForm form) {
        itemService.updateItem(form.getId(),
                new BookDTO(form.getName(), form.getPrice(),
                        form.getStockQuantity(), form.getAuthor(), form.getIsbn()));
        return "redirect:/items";
    }
}
