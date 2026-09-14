package com.metrio.Metrio.service;

import com.metrio.Metrio.dto.AgencyRequest;
import com.metrio.Metrio.models.Agency;
import com.metrio.Metrio.models.User;
import com.metrio.Metrio.repository.AgencyRepository;
import com.metrio.Metrio.repository.ClientRepository;
import com.metrio.Metrio.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private static AgencyRepository agencyRepository;
    private final ClientRepository clientRepository;
    private static UserRepository userRepository = null;

    public RegisterService(AgencyRepository agencyRepository, ClientRepository clientRepository, UserRepository userRepository) {
        this.agencyRepository = agencyRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    // Cadastrar agência nova
    public ResponseEntity<String> cadastrarAgencia(AgencyRequest request, HttpSession session){

        Long userId = (Long) session.getAttribute("userId");

        // Verificação de usuário logado
        if (userId == null){
            return ResponseEntity.badRequest().body("Cadastre-se para criar agência");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Agency agency = new Agency();

        agency.setAgencyName(request.agencyName());
        agency.setCreator(user);

        agencyRepository.save(agency);

        user.setAgency(agency);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Agência registrada!");
    }
}
