package com.vinhos.bd.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api")
public class ProdutoController {

    @GetMapping(value="/helloworld")
    public String helloworld(){
        return "Hello World";
    }
}
