package ua.ypon.springcourseLib.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.ypon.springcourseLib.dao.PersonDAO;
import ua.ypon.springcourseLib.models.Person;
import ua.ypon.springcourseLib.util.PersonValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PersonDAO personDAO, PersonValidator personValidator) {
        this.personDAO = personDAO;
        this.personValidator = personValidator;
    }
    @GetMapping()
    public String index(Model model) {
        //Получим всех людей из DAO и передадим на отображеник в представлениe
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        //Получим одного человека по id из DAO и передадим на отображение в представление
        model.addAttribute("person", personDAO.show(id));
        model.addAttribute("books", personDAO.getBooksByPersonId(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()//valid- проверка условий. BindingResult-объект для хранения ошибок при создании Person и введении неValid.
    public String create(@ModelAttribute("person") @Valid Person person,//с помощью @ModelAttribute создаем нового человека(пустой объект класса Person)
                         BindingResult bindingResult) {//тут зберігаються помилки
       personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors())
            return "people/new";

        personDAO.save(person);//затем эта же аннотация внедряет значение из формы в этот объект класса Person. Затем она этот объект добавляет в модель.
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personDAO.show(id));
        return "people/edit";
    }
/*
BindingResult - для красивого представлення помилок на сторінках
 */
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if(bindingResult.hasErrors())
            return "people/edit";
        personDAO.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personDAO.delete(id);
        return "redirect:/people";
    }
}
