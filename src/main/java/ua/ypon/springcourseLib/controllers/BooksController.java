package ua.ypon.springcourseLib.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.ypon.springcourseLib.dao.BookDAO;
import ua.ypon.springcourseLib.dao.PersonDAO;
import ua.ypon.springcourseLib.models.Book;
import ua.ypon.springcourseLib.models.Person;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    @Autowired
    public BooksController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model) {
        //Получим все книги из DAO и передадим на отображеник в представлениe
        model.addAttribute("books", bookDAO.index());
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        //Получим одну книгу по id из DAO и передадим на отображение в представление
        model.addAttribute("book", bookDAO.show(id));

        Optional<Person> bookOwner = bookDAO.getBookOwner(id);//якщо книга зайнята-показуємо ким,а як ні-список

        if(bookOwner.isPresent()) {
            model.addAttribute("owner", bookOwner.get());//якщо книга зайнята-показуємо ким
        } else {
            model.addAttribute("people", personDAO.index());//а як ні-список кому призначити
        }
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()//valid- проверка условий. BindingResult-объект для хранения ошибок при создании Person и введении неValid.
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {//с помощью @ModelAttribute создаем новую книгу(пустой объект класса Book)
        if(bindingResult.hasErrors())
            return "books/new";

        bookDAO.save(book);//затем эта же аннотация внедряет значение из формы в этот объект класса Book. Затем она этот объект добавляет в модель.
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookDAO.show(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if(bindingResult.hasErrors())
            return "books/edit";
        bookDAO.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookDAO.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {//метод для вивільнення книги
        bookDAO.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        // В selectedPerson призначено тільки одне поле id, інші - null
        bookDAO.assign(id, selectedPerson);//метод для присвоєння книги
        return "redirect:/books/" + id;
    }
}
