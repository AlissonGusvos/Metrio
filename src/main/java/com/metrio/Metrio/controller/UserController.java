package com.metrio.Metrio.controller;

import com.metrio.Metrio.dto.AgencyRequest;
import com.metrio.Metrio.service.RegisterService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final RegisterService registerService;

    public UserController(RegisterService registerService) {
        this.registerService = registerService;
    }

    //Cadastrar agencia
    @PostMapping("/cad-agency")
    public ResponseEntity<String> cadColab(@RequestBody AgencyRequest request, HttpSession session){
        return registerService.cadastrarAgencia(request, session);
    }

}