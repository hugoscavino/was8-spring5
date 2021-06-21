package org.scavino.sb.controller;

import org.scavino.sb.model.Person;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Manage a REST (JSON) Person. View only in this instance
 */
@RestController
public class PersonRestController {

    /**
     * Creates an arbitrary JSON Person
     * @see Person
     * @return Person hard coded to be tom jones
     */
    @GetMapping(value = "/api/person")
    public Person restPerson() {

        return new Person("tom", "jones", "000-111-333", (short) 34);
    }
}
