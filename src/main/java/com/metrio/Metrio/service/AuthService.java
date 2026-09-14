package com.metrio.Metrio.service;

import com.metrio.Metrio.configuration.EncriptConfig;
import com.metrio.Metrio.configuration.UserCategories;
import com.metrio.Metrio.configuration.UserStatus;
import com.metrio.Metrio.dto.UserLoginRequest;
import com.metrio.Metrio.dto.UserRegisterRequest;
import com.metrio.Metrio.models.User;
import com.metrio.Metrio.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final EncriptConfig encriptConfig;

    public AuthService(UserRepository userRepository, EncriptConfig encriptConfig){
        this.userRepository = userRepository;
        this.encriptConfig = encriptConfig;
    }

    //Registro de usuário
    public void userRegister(UserRegisterRequest request){

        User user = new User();

        user.setUsername(request.username());
        user.setEmail(request.email());

        //Criptografia
        String hashPassword = encriptConfig.passwordEncoder().encode(request.password());
        user.setHashPassword(hashPassword);

        user.setUserType(String.valueOf(UserCategories.GERENTE));
        user.setUserStatus(String.valueOf(UserStatus.ACTIVE));

        userRepository.save(user);
    }

    //Login de usuário
    public void userLogin(UserLoginRequest request, HttpSession session){

        Optional<User> userOptional = userRepository.findByEmail(request.email());

        if (userOptional.isEmpty()){
            throw new RuntimeException("E-mail ou senha inválidos");
        }

        User user = userOptional.get();

        if (!encriptConfig.passwordEncoder().matches(request.password(), user.getHashPassword())){
            throw new RuntimeException("E-mail ou senha inválidos");
        }

        //Guardar ID em sessão
        session.setAttribute("userId", user.getId());
        if(user.getAgency() != null){
            session.setAttribute("agencyId", user.getAgency().getId());
        }
    }

}