package com.tairanchina.csp.dew.example.web;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {

    @GetMapping("/example")
    public String example() {
        return "enjoy!";
    }

}
