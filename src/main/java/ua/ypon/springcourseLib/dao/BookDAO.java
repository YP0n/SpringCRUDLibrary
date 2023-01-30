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
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM Book",
                new BeanPropertyRowMapper<>(Book.class));//встроенный маппер
    }

    public Book show(int id) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE id=?", new Object[]{id},
                        new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(null);
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO Book(title, author, year) VALUES(?, ?, ?)",
                book.getTitle(),
                book.getAuthor(),
                book.getYear());
    }

    public void update(int id, Book updatedBook) {
        jdbcTemplate.update("UPDATE Book SET title=?, author=?, year=? WHERE id=?",
                updatedBook.getTitle(), updatedBook.getAuthor(),
                updatedBook.getYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Book WHERE id=?", id);
    }


    // Join'им таблиці Book та Person і отримуємо людину, якій належить книга зі вказаним id
    public Optional<Person> getBookOwner(int id) {
        //Вибираємо віс стовбці таблиці Person із об'єднаної таблиці
        return jdbcTemplate.query("SELECT Person.* FROM Book JOIN Person ON Book.id_person = Person.id " +
                        "WHERE Book.id=?", new Object[] {id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny();
    }

    //Вивільняє книгу(коли повертають)
    public void release(int id) {
        jdbcTemplate.update("UPDATE Book SET id_person=NULL WHERE id=?", id);
    }

    //Призначають книгу людині(коли забирає)
    public void assign(int id, Person selectedPerson) {
        jdbcTemplate.update("UPDATE Book SET id_person=? WHERE id=?", selectedPerson.getId(), id);
    }
}
