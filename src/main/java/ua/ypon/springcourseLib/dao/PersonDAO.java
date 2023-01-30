package ua.ypon.springcourseLib.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.ypon.springcourseLib.models.Book;
import ua.ypon.springcourseLib.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;//використовуємо для створення CRUD додатку

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM Person",
                new BeanPropertyRowMapper<>(Person.class));//встроенный маппер
    }

    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE id=?", new Object[]{id},
                        new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO Person(name, yearOfBirth) VALUES(?, ?)",
                person.getName(),
                person.getYearOfBirth());
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE Person SET name=?, yearOfBirth=? WHERE id=?",
                updatedPerson.getName(),
                updatedPerson.getYearOfBirth(), id);

    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
    }

    public Optional<Person> getPersonByName (String name) {//обгортка над об"єктами які можуть бути, а можуть і не бути
        return jdbcTemplate.query("SELECT * FROM Person WHERE name=?", new Object[] {name},
                        new BeanPropertyRowMapper<>(Person.class))//людина яка прийшла в якості запроса - перетв.в людину
                .stream().findAny();//метод для пошуку співпадінь name
    }

    //join не потрібен бо людину отримали окремим методом show
    public List<Book> getBooksByPersonId(int id) {//сторінка людини із спискм книг
        return jdbcTemplate.query("SELECT * FROM Book WHERE id_person = ?", new Object[] {id},
        new BeanPropertyRowMapper<>(Book.class));
    }
}
