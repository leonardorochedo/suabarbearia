package com.suabarbearia.backend.services;

import com.suabarbearia.backend.dtos.ServiceDto;
import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.entities.Service;
import com.suabarbearia.backend.exceptions.ExistUserException;
import com.suabarbearia.backend.repositories.BarbershopRepository;
import com.suabarbearia.backend.repositories.ServiceRepository;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.TextResponse;
import com.suabarbearia.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private BarbershopRepository barbershopRepository;

    public Service findById(Long id) {
        Optional<Service> service = serviceRepository.findById(id);

        return service.get();
    }

    public ApiResponse<Service> create(String authorizationHeader, ServiceDto service) {
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Service serviceFinded = serviceRepository.findByTitle(service.getTitle());

        Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));

        // Check data
        if (serviceFinded != null && serviceFinded.getBarbershop().equals(barbershop)) {
            throw new ExistUserException("Serviço existente!");
        }

        if (service.getTitle() == null || service.getPrice() == null) {
            throw new IllegalArgumentException("Um ou mais campos obrigatórios não estão preenchidos!");
        }

        Service newService = serviceRepository.save(new Service(null, service.getTitle(), service.getPrice(), true, barbershop));

        ApiResponse<Service> response = new ApiResponse<Service>("Serviço criado com sucesso!", newService);

        return response;
    }

    public ApiResponse<Service> edit(String authorizationHeader, Long id, ServiceDto service) {
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Service editedService = serviceRepository.findById(id).get();

        Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));

        // Check data
        if (!editedService.getBarbershop().equals(barbershop)) {
            throw new RuntimeException("Token inválido para esta barbearia!");
        }

        if (service.getTitle() == null || service.getPrice() == null) {
            throw new IllegalArgumentException("Um ou mais campos obrigatórios não estão preenchidos!");
        }

        editedService.setTitle(service.getTitle());
        editedService.setPrice(service.getPrice());

        serviceRepository.save(editedService);

        ApiResponse<Service> response = new ApiResponse<>("Serviço editado com sucesso!", editedService);

        return response;
    }

    public TextResponse enable(String authorizationHeader, Long id) {
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Service findedService = serviceRepository.findById(id).get();

        Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));

        // Check data
        if (!findedService.getBarbershop().equals(barbershop)) {
            throw new RuntimeException("Token inválido para esta barbearia!");
        }

        if (findedService.isEnabled()) {
            throw new IllegalArgumentException("Serviço " + findedService.getTitle() + " já está habilitado!");
        }

        findedService.setEnabled(true);

        serviceRepository.save(findedService);

        TextResponse response = new TextResponse("Serviço " + findedService.getTitle() + " habilitado!");

        return response;
    }

    public TextResponse disable(String authorizationHeader, Long id) {
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Service findedService = serviceRepository.findById(id).get();

        Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));

        // Check data
        if (!findedService.getBarbershop().equals(barbershop)) {
            throw new RuntimeException("Token inválido para esta barbearia!");
        }

        if (!findedService.isEnabled()) {
            throw new IllegalArgumentException("Serviço " + findedService.getTitle() + " já está desabilitado!");
        }

        findedService.setEnabled(false);

        serviceRepository.save(findedService);

        TextResponse response = new TextResponse("Serviço " + findedService.getTitle() + " desabilitado!");

        return response;
    }

}
