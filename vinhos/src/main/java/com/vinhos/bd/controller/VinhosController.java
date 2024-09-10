package com.vinhos.bd.controller;

import com.vinhos.bd.model.Vinhos;
import com.vinhos.bd.repository.VinhosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vinhos")
public class VinhosController {
    @Autowired
    private VinhosRepository vinhosRepository;
    @PostMapping("/add")
    public ResponseEntity<String> addVinho(@RequestBody Vinhos vinho) {
        if (vinhosRepository.existsById(vinho.getId().getNome(), vinho.getId().getVinicula())) {
            return new ResponseEntity<>("Vinho já cadastrado", HttpStatus.CONFLICT);
        }
        vinhosRepository.save(vinho);
        return new ResponseEntity<>("Vinho adicionado com sucesso", HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{nome}/{vinicula}")
    public ResponseEntity<String> deleteVinho(@PathVariable String nome, @PathVariable String vinicula) {
        if (vinhosRepository.findById(nome, vinicula).isPresent()) {
            vinhosRepository.deleteById(nome, vinicula);
            return new ResponseEntity<>("Vinho deletado com sucesso", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("Vinho não encontrado", HttpStatus.NOT_FOUND);
        }
    }
}