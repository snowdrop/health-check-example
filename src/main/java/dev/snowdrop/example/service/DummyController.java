package dev.snowdrop.example.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Workaround for Dekorate to expose routes at path `/`. Otherwise, it will use the path from `FruitController`: /api/fruits.
 * Without this workaround, the static resources like `index.html` cannot be accessed via the route when deployed in OCP.
 * It should be fixed using Dekorate 2.7.
 */
@RestController
@RequestMapping(value = "/dummy")
public class DummyController {
    @GetMapping
    public String get() {
        return "Hello";
    }
}
