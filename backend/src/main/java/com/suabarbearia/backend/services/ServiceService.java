package com.suabarbearia.backend.services;

import com.suabarbearia.backend.dtos.CreateServiceDto;
import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.entities.Service;
import com.suabarbearia.backend.exceptions.ExistUserException;
import com.suabarbearia.backend.repositories.BarbershopRepository;
import com.suabarbearia.backend.repositories.ServiceRepository;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private BarbershopRepository barbershopRepository;

    public ApiResponse<Service> create(String authorizationHeader, CreateServiceDto service) {
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Service serviceFinded = serviceRepository.findByTitle(service.getTitle());

        // Check data
        if (serviceFinded != null) {
            throw new ExistUserException("Serviço existente!");
        }

        if (service.getTitle() == null || service.getPrice() == null) {
            throw new IllegalArgumentException("Um ou mais campos obrigatórios não estão preenchidos!");
        }

        Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));

        Service newService = serviceRepository.save(new Service(null, service.getTitle(), service.getPrice(), barbershop));

        ApiResponse<Service> response = new ApiResponse<Service>("Serviço criado com sucesso!", newService);

        return response;
    }
}
