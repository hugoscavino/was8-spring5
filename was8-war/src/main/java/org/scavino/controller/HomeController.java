package org.scavino.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Manage Home Page.
 */
@Controller
public class HomeController {

    /**
     * Load the index.jsp page
     *
     * @return Handler to the the main page index.jsp
     */
    @GetMapping(value = "/")
    public String handler() {
        return "index";
    }
}