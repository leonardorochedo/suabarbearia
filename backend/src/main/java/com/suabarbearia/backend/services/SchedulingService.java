package com.suabarbearia.backend.services;

import com.suabarbearia.backend.dtos.SchedulingDto;
import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.entities.Scheduling;
import com.suabarbearia.backend.entities.Service;
import com.suabarbearia.backend.entities.User;
import com.suabarbearia.backend.repositories.BarbershopRepository;
import com.suabarbearia.backend.repositories.SchedulingRepository;
import com.suabarbearia.backend.repositories.ServiceRepository;
import com.suabarbearia.backend.repositories.UserRepository;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

@org.springframework.stereotype.Service
public class SchedulingService {

    @Autowired
    private SchedulingRepository schedulingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BarbershopRepository barbershopRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    public Scheduling findById(Long id) {
        Optional<Scheduling> scheduling = schedulingRepository.findById(id);

        return scheduling.get();
    }

    public ApiResponse<Scheduling> create(String authorizationHeader, SchedulingDto scheduling) {
        // Entities
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        User userFinded = userRepository.findByEmail(JwtUtil.getEmailFromToken(token));
        Service serviceFinded = serviceRepository.findById(scheduling.getServiceId()).get();
        Barbershop barbershopFinded = barbershopRepository.findById(scheduling.getBarbershopId()).get();

        // Check data
        if (scheduling.getBarbershopId() == null || scheduling.getServiceId() == null || scheduling.getDate() == null) {
            throw new IllegalArgumentException("Um ou mais campos obrigatórios não estão preenchidos!");
        }

        // Check date and hour
        if (scheduling.getDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Data inválida!");
        }

        // Check if hour is within barbershop's opening hours
        LocalTime hourActual = scheduling.getDate().toLocalTime();
        LocalTime openingHour = LocalTime.of(8, 0);
        LocalTime closingHour = LocalTime.of(18, 0);

        if (hourActual.isBefore(openingHour) || hourActual.isAfter(closingHour)) {
            throw new IllegalArgumentException("O horário do agendamento deve estar dentro do horário de funcionamento da barbearia!");
        }

        // Check user schedulings
        Set<Scheduling> userSchedulings = schedulingRepository.findAllByUser(userFinded);

        for (Scheduling userScheduling : userSchedulings) {
            if (userScheduling.getDate().withHour(0).isEqual(scheduling.getDate().withHour(0))) {
                throw new IllegalArgumentException("O usuário já possui um agendamento para este dia!");
            }
        }

        // Check barbershop schedulings
        Set<Scheduling> barbershopSchedulings = schedulingRepository.findAllByBarbershop(barbershopFinded);

        for (Scheduling barbershopScheduling : barbershopSchedulings) {
            if (barbershopScheduling.getDate().isEqual(scheduling.getDate())) {
                throw new IllegalArgumentException("A barbearia já possui um agendamento neste horário!");
            }
        }

        // Create scheduling
        Scheduling newScheduling = schedulingRepository.save(new Scheduling(userFinded, barbershopFinded, serviceFinded, scheduling.getDate(), false));

        ApiResponse<Scheduling> response = new ApiResponse<Scheduling>("Agendamento realizado com sucesso!", newScheduling);

        return response;
    }

}
