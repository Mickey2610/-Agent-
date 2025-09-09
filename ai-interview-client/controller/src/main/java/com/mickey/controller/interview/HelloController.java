package com.mickey.controller.interview;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    /**
     *
     * @return
     */
    @GetMapping("hello")
    public Object hello() {
        return "Hello World";
    }
}
