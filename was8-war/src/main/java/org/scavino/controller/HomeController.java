package org.scavino.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Manage Person. View only in this instance
 */
@Controller
public class HomeController {

    /**
     * Creates an arbitrary person and adds person object to the Model with and attribute of
     * 'person'.
     *
     * @param model spring model object
     * @return resource name to eventually map to. In this case it is index or the main page
     */
    @GetMapping(value = "/")
    public String handler(Model model) {
        return "index";
    }
}
