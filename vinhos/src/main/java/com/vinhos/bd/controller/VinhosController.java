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
    @PostMapping("/tinto")
    // Passa estilo como parâmetro request /tinto?estilo=Seco
    public ResponseEntity<String> addTinto(@RequestBody Vinhos vinho, @RequestParam String estilo) {
        if (vinhosRepository.existsById(vinho.getId().getNome(), vinho.getId().getVinicula())) {
            return new ResponseEntity<>("Vinho já cadastrado", HttpStatus.CONFLICT);
        }
        // Chama o método para salvar o vinho tinto com o estilo fornecido
        vinhosRepository.saveTinto(vinho, estilo);
        return new ResponseEntity<>("Vinho tinto adicionado com sucesso", HttpStatus.CREATED);
    }

    @PostMapping("/branco")
    public ResponseEntity<String> addBranco(@RequestBody Vinhos vinho, @RequestParam String estilo) {
        if (vinhosRepository.existsById(vinho.getId().getNome(), vinho.getId().getVinicula())) {
            return new ResponseEntity<>("Vinho já cadastrado", HttpStatus.CONFLICT);
        }
        vinhosRepository.saveBranco(vinho, estilo);
        return new ResponseEntity<>("Vinho branco adicionado com sucesso", HttpStatus.CREATED);
    }

    @PostMapping("/rose")
    public ResponseEntity<String> addRose(@RequestBody Vinhos vinho, @RequestParam String estilo) {
        if (vinhosRepository.existsById(vinho.getId().getNome(), vinho.getId().getVinicula())) {
            return new ResponseEntity<>("Vinho já cadastrado", HttpStatus.CONFLICT);
        }
        vinhosRepository.saveRose(vinho, estilo);
        return new ResponseEntity<>("Vinho rose adicionado com sucesso", HttpStatus.CREATED);
    }

    @PostMapping("/espumante")
    public ResponseEntity<String> addEspumante(@RequestBody Vinhos vinho, @RequestParam String estilo) {
        if (vinhosRepository.existsById(vinho.getId().getNome(), vinho.getId().getVinicula())) {
            return new ResponseEntity<>("Vinho já cadastrado", HttpStatus.CONFLICT);
        }
        vinhosRepository.saveEspumante(vinho, estilo);
        return new ResponseEntity<>("Vinho espumante adicionado com sucesso", HttpStatus.CREATED);
    }

    @PostMapping("/sobremesa")
    public ResponseEntity<String> addSobremesa(@RequestBody Vinhos vinho, @RequestParam String estilo) {
        if (vinhosRepository.existsById(vinho.getId().getNome(), vinho.getId().getVinicula())) {
            return new ResponseEntity<>("Vinho já cadastrado", HttpStatus.CONFLICT);
        }
        vinhosRepository.saveSobremesa(vinho, estilo);
        return new ResponseEntity<>("Vinho sobremesa adicionado com sucesso", HttpStatus.CREATED);
    }

    @PostMapping("/fortificado")
    public ResponseEntity<String> addFortificado(@RequestBody Vinhos vinho, @RequestParam String estilo) {
        if (vinhosRepository.existsById(vinho.getId().getNome(), vinho.getId().getVinicula())) {
            return new ResponseEntity<>("Vinho já cadastrado", HttpStatus.CONFLICT);
        }
        vinhosRepository.saveFortificado(vinho, estilo);
        return new ResponseEntity<>("Vinho fortificado adicionado com sucesso", HttpStatus.CREATED);
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