package de.dps.springbatch.model;

import de.dps.springbatch.adapter.GenderXmlAdapter;
import de.dps.springbatch.adapter.IntegerXmlAdapter;
import de.dps.springbatch.adapter.LocalDateXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="person")
public class Person {

    @XmlJavaTypeAdapter(type = int.class, value = IntegerXmlAdapter.class)
    private Integer id;
    private String firstName;
    private String lastName;

    @XmlJavaTypeAdapter(type = Gender.class, value = GenderXmlAdapter.class)
    private Gender gender;

    @XmlJavaTypeAdapter(type = LocalDate.class, value = LocalDateXmlAdapter.class)
    private LocalDate birthday;

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", birthday=" + birthday +
                '}';
    }
}
