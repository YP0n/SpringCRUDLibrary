package ua.ypon.springcourseLib.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Person {
    private int id;

   //@Pattern(regexp = "[А-Я]\\w+ [А-Я]\\w+ [А-Я]\\w+", message = "Треба писати Прізвище Ім'я по Батькові")
    @NotEmpty(message = "Ім'я не повинно бути пустим")
    @Size(min = 2, max = 100, message = "Ім'я повинне бути від 2 до 100 символів")
    private String name;

   @Min(value = 1900, message = "Рік народження повинен бути більшим за 1900")
    private int yearOfBirth;

    //конструктор за замовчування-потрібен для Spring(наприклад для @ModelAttribute)
    public Person() {
    }

    public Person(String name, int yearOfBirth) {
        this.name = name;
        this.yearOfBirth = yearOfBirth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }
}
