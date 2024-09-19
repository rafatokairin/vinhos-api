//package com.vinhos.bd.controller;
//
//import com.vinhos.bd.model.MyAppUser;
//import com.vinhos.bd.repository.MyAppUserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//import com.vinhos.bd.dao.MyAppUserDAO;
//
//import java.util.Optional;
//
//@RestController
//public class RegistrationController {
//    @Autowired
//    private MyAppUserRepository myAppUserRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @PostMapping("/signup")
//    public ResponseEntity<String> createUser(@RequestBody MyAppUser user) {
//        if (myAppUserRepository.existsById(user.getUsername())) {
//            return new ResponseEntity<>("Email já cadastrado", HttpStatus.CONFLICT);
//        }
//
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        myAppUserRepository.save(user);
//        return new ResponseEntity<>("Usuário registrado com sucesso", HttpStatus.CREATED);
//    }
//
//    @PostMapping("/update/{email}")
//    public ResponseEntity<?> updateUser(@PathVariable String email, @RequestBody MyAppUser userUpdates) {
//        Optional<MyAppUser> existingUser = myAppUserRepository.findById(email);
//        if (existingUser.isEmpty()) {
//            return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);
//        }
//        MyAppUser user = existingUser.get();
//        if (userUpdates.getNome() != null) {
//            user.setNome(userUpdates.getNome());
//        }
//        if (userUpdates.getPassword() != null) {
//            user.setPassword(passwordEncoder.encode(userUpdates.getPassword()));
//        }
//        myAppUserRepository.update(user);
//        return new ResponseEntity<>(user, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/delete/{email}")
//    public ResponseEntity<String> deleteUser(@PathVariable String email) {
//        if (myAppUserRepository.existsById(email)) {
//            myAppUserRepository.deleteById(email);
//            return new ResponseEntity<>("Usuário deletado com sucesso", HttpStatus.NO_CONTENT);
//        } else {
//            return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);
//        }
//    }
//}
