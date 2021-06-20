package org.scavino.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.scavino.model.Person;
import org.springframework.ui.Model;

/**
 * Manage Person. View only in this instance
 */
@Controller
public class PersonController {

    /**
     * Creates an arbitrary person and adds person object to the Model with and attribute of
     * 'person'.
     *
     * @param model spring model object
     * @return resource name to eventually map to. In this case it is index or the main page
     */
    @GetMapping(value = "/person")
    public String personHandler(Model model) {

        Person person = new Person("john", "doe", "123-123-123", (short) 50);
        model.addAttribute("person", person);
        return "person";
    }
}
