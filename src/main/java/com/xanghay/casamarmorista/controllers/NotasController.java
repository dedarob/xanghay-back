package com.xanghay.casamarmorista.controllers;

import com.xanghay.casamarmorista.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/notas")
public class NotasController {
    @Autowired
    private ClienteService service;

    @GetMapping
}
