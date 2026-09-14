package com.metrio.Metrio.service;

import com.metrio.Metrio.configuration.EncriptConfig;
import com.metrio.Metrio.configuration.UserStatus;
import com.metrio.Metrio.dto.AgencyRequest;
import com.metrio.Metrio.dto.ClientRequest;
import com.metrio.Metrio.models.Agency;
import com.metrio.Metrio.models.Client;
import com.metrio.Metrio.models.User;
import com.metrio.Metrio.repository.AgencyRepository;
import com.metrio.Metrio.repository.ClientRepository;
import com.metrio.Metrio.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

        //Guardar ID da agência em sessão
        session.setAttribute("agencyId", agency.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Agência registrada!");
    }

    // Cadastrar cliente
    public ResponseEntity<String> cadastrarCliente(ClientRequest request, HttpSession session){

        // Verificar se agência existe e está logada
        Long agencyId = (Long) session.getAttribute("agencyId");
        if(agencyId == null){
            return ResponseEntity.badRequest().body("Agência não encontrada");
        }

        Optional<Agency> agencyOptional = agencyRepository.findById(agencyId);
        if(agencyOptional.isEmpty()){
            return ResponseEntity.badRequest().body("Agência não encontrada");
        }

        if(clientRepository.findByLogin(request.clientLogin()).isPresent()){
            return ResponseEntity.badRequest().body("Login já existente");
        }

        Agency agency = agencyOptional.get();
        Client client = new Client();

        String hashedClientPassword = EncriptConfig.passwordEncoder().encode(request.clientPassword());

        client.setAgency(agency);

        client.setClientName(request.clientName());
        client.setClientLogin(request.clientLogin());
        client.setClientPass(hashedClientPassword);
        client.setClientStatus(String.valueOf(UserStatus.ACTIVE));

        clientRepository.save(client);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Cliente cadastrado!");
    }
}
