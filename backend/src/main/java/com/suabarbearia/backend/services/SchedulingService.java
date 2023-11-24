package com.suabarbearia.backend.services;

import com.suabarbearia.backend.dtos.SchedulingDto;
import com.suabarbearia.backend.entities.*;
import com.suabarbearia.backend.repositories.*;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
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
    private EmployeeRepository employeeRepository;

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
        Employee employeeFinded = employeeRepository.findById(scheduling.getEmployeeId()).get();

        // Validations
        validateScheduling(scheduling, employeeFinded, userFinded);

        List<Scheduling> lastUserScheduling = schedulingRepository.findTopByUserAndBarbershopOrderByDateDesc(userFinded, barbershopFinded);

        if (!lastUserScheduling.isEmpty()) { // verify if las user scdlng is done
            Scheduling lastScheduling = lastUserScheduling.get(0);

            if (!lastScheduling.isDone()) {
                throw new IllegalArgumentException("O último agendamento do usuário ainda não foi concluído!");
            }
        }

        // Create scheduling
        Scheduling newScheduling = schedulingRepository.save(new Scheduling(userFinded, barbershopFinded, employeeFinded, serviceFinded, scheduling.getDate(), false));

        ApiResponse<Scheduling> response = new ApiResponse<Scheduling>("Agendamento realizado com sucesso!", newScheduling);

        return response;
    }

    public ApiResponse<Scheduling> edit(String authorizationHeader, Long id, SchedulingDto scheduling) {
        // Entities
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        User userFinded = userRepository.findByEmail(JwtUtil.getEmailFromToken(token));
        Barbershop barbershopFinded = barbershopRepository.findById(scheduling.getBarbershopId()).get();
        Scheduling schedulingFinded = schedulingRepository.findById(id).get();
        Employee employeeFinded = employeeRepository.findById(scheduling.getEmployeeId()).get();

        // Validations
        validateScheduling(scheduling, employeeFinded, userFinded);

        if (!schedulingFinded.getUser().equals(userFinded)) {
            throw new RuntimeException("Token inválido para este usuário!");
        }

        if (scheduling.getServiceId() != schedulingFinded.getService().getId() || scheduling.getBarbershopId() != schedulingFinded.getBarbershop().getId()) {
            throw new IllegalArgumentException("Não é possível alterar este campo!");
        }

        // Update data
        schedulingFinded.setDate(scheduling.getDate());
        schedulingFinded.setEmployee(employeeFinded);

        schedulingRepository.save(schedulingFinded);

        ApiResponse<Scheduling> response = new ApiResponse<Scheduling>("Agendamento alterado com sucesso!", schedulingFinded);

        return response;
    }

    // Helper
    private boolean validateScheduling(SchedulingDto scheduling, Employee employee, User user) {

        if (scheduling.getBarbershopId() == null || scheduling.getServiceId() == null || scheduling.getDate() == null) {
            throw new IllegalArgumentException("Um ou mais campos obrigatórios não estão preenchidos!");
        }

        // Check service
        Service service = serviceRepository.findById(scheduling.getServiceId()).get();

        if (!service.isEnabled()) {
            throw new IllegalArgumentException("Serviço " + service.getTitle() + " está desabilitado!");
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
        Set<Scheduling> userSchedulings = schedulingRepository.findAllByUser(user);

        for (Scheduling userScheduling : userSchedulings) {
            if (userScheduling.getDate().withHour(0).isEqual(scheduling.getDate().withHour(0))) {
                throw new IllegalArgumentException("O usuário já possui um agendamento para este dia!");
            }
        }

        // Check employee schedulings
        Set<Scheduling> employeeSchedulings = schedulingRepository.findAllByEmployee(employee);

        for (Scheduling employeeScheduling : employeeSchedulings) {
            LocalDateTime employeeSchedulingStartTime = employeeScheduling.getDate();
            LocalDateTime employeeSchedulingEndTime = employeeSchedulingStartTime.plusMinutes(30);

            LocalDateTime schedulingStartTime = scheduling.getDate();
            LocalDateTime schedulingEndTime = schedulingStartTime.plusMinutes(30);

            // Verificar se o novo agendamento começa dentro de 30 minutos após um agendamento existente
            if ((schedulingStartTime.isEqual(employeeSchedulingStartTime) || schedulingStartTime.isAfter(employeeSchedulingStartTime)) &&
                    schedulingStartTime.isBefore(employeeSchedulingEndTime)) {
                throw new IllegalArgumentException("O barbeiro selecionado já possui um agendamento neste horário!");
            }

            if (schedulingStartTime.isBefore(employeeSchedulingEndTime) && schedulingEndTime.isAfter(employeeSchedulingEndTime)) {
                throw new IllegalArgumentException("O barbeiro selecionado já possui um agendamento neste horário!");
            }
        }

        return true;
    }

}
