package org.tuxdevelop.spring.data.solr.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String init() {
        return "redirect:index";
    }
}
