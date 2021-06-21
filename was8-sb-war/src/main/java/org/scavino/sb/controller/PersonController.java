package org.scavino.sb.controller;

import org.scavino.sb.model.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Manage Person. View only in this instance
 * see https://www.baeldung.com/spring-mvc-model-model-map-model-view
 * for an alternate implementation
 *
 */
@Controller
public class PersonController {

    /**
     * Creates an arbitrary person and adds person object to the Model with and attribute of
     * 'person'.
     *
     * @param model spring model object
     * @return resource name to eventually map to. In this case it is person or person.jsp page
     */
    @GetMapping(value = "/person")
    public String personHandler(Model model) {

        Person person = new Person("jane", "smith", "987-654-321", (short) 25);
        model.addAttribute("person", person);

        // You can of course use the ModelAndView as an alternative
        // https://www.baeldung.com/spring-mvc-model-model-map-model-view
        return "person";
    }

}
