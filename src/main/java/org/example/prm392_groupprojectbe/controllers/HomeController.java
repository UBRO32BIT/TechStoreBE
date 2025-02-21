package org.example.prm392_groupprojectbe.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("**")
@RequestMapping("/home")
public class HomeController {
    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello World");
    }
}
