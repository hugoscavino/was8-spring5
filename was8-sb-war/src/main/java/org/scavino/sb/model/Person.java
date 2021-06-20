package org.scavino.sb.model;

import java.util.Objects;

/**
 * Arbitrary Person bean. This would be a lot easier with lombok
 *
 */
public class Person {

    private final String firstName;
    private final String lastName;
    private final String ssn;
    private final short age;

    public Person(String firstName, String lastName, String ssn, short age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public short getAge() {
        return age;
    }

    public String getSsn() {
        return ssn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return firstName.equals(person.firstName) && lastName.equals(person.lastName) && ssn.equals(person.ssn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, ssn);
    }
}
