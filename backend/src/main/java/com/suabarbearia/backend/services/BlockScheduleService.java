package com.suabarbearia.backend.services;

import com.suabarbearia.backend.dtos.BlockScheduleDto;
import com.suabarbearia.backend.entities.*;
import com.suabarbearia.backend.exceptions.*;
import com.suabarbearia.backend.repositories.*;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.TextResponse;
import com.suabarbearia.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

@org.springframework.stereotype.Service
public class BlockScheduleService {

    @Autowired
    private BarbershopRepository barbershopRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private BlockScheduleRepository blockScheduleRepository;

    @Autowired
    private SchedulingRepository schedulingRepository;

    public BlockSchedule findById(Long id) {
        Optional<BlockSchedule> blockSchedule = blockScheduleRepository.findById(id);

        return blockSchedule.get();
    }

    public ApiResponse<BlockSchedule> employeeBlock(String authorizationHeader, BlockScheduleDto blockSchedule) {
        // Entities
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Employee employeeToken = employeeRepository.findByUsername(JwtUtil.getUsernameFromToken(token));
        Barbershop barbershopFinded = barbershopRepository.findById(blockSchedule.getIdBarbershop()).get();
        Employee employeeFinded = employeeRepository.findById(blockSchedule.getIdEmployee()).get();

        // Validations
        if (!employeeToken.equals(employeeFinded)) {
            throw new NoPermissionException("Não autorizado!");
        }

        validateScheduling(blockSchedule, barbershopFinded, employeeFinded);

        // Create scheduling
        BlockSchedule newBlockSchedule = blockScheduleRepository.save(new BlockSchedule(barbershopFinded, employeeFinded, blockSchedule.getDate()));

        return new ApiResponse<BlockSchedule>("Agenda bloqueada com sucesso!", newBlockSchedule);
    }

    public ApiResponse<BlockSchedule> barbershopBlock(String authorizationHeader, BlockScheduleDto blockSchedule) {
        // Entities
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Barbershop barbershopToken = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));
        Barbershop barbershopFinded = barbershopRepository.findById(blockSchedule.getIdBarbershop()).get();
        Employee employeeFinded = employeeRepository.findById(blockSchedule.getIdEmployee()).get();

        // Validations
        if (!barbershopToken.equals(barbershopFinded) || !barbershopToken.equals(employeeFinded.getBarbershop())) {
            throw new NoPermissionException("Não autorizado!");
        }

        validateScheduling(blockSchedule, barbershopFinded, employeeFinded);

        // Create scheduling
        BlockSchedule newBlockSchedule = blockScheduleRepository.save(new BlockSchedule(barbershopFinded, employeeFinded, blockSchedule.getDate()));

        return new ApiResponse<BlockSchedule>("Agenda bloqueada com sucesso!", newBlockSchedule);
    }

    public TextResponse employeeBlockDelete(String authorizationHeader, Long id) {
        // Entities
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Employee employeeToken = employeeRepository.findByUsername(JwtUtil.getUsernameFromToken(token));
        BlockSchedule blockScheduleFinded = blockScheduleRepository.findById(id).get();

        // Validations
        if (!employeeToken.equals(blockScheduleFinded.getEmployee())){
            throw new NoPermissionException("Não autorizado!");
        }

        // Update date
        blockScheduleRepository.delete(blockScheduleFinded);

        TextResponse response = new TextResponse("Bloqueio removido com sucesso!");

        return response;
    }

    public TextResponse barbershopBlockDelete(String authorizationHeader, Long id) {
        // Entities
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Barbershop barbershopToken = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));
        BlockSchedule blockScheduleFinded = blockScheduleRepository.findById(id).get();

        // Validations
        if (!barbershopToken.equals(blockScheduleFinded.getBarbershop())){
            throw new NoPermissionException("Não autorizado!");
        }

        // Update date
        blockScheduleRepository.delete(blockScheduleFinded);

        TextResponse response = new TextResponse("Bloqueio removido com sucesso!");

        return response;
    }

    private boolean validateScheduling(BlockScheduleDto blockSchedule, Barbershop barbershop, Employee employee) {

        if (blockSchedule.getIdBarbershop() == null || blockSchedule.getIdEmployee() == null || blockSchedule.getDate() == null) {
            throw new FieldsAreNullException("Um ou mais campos obrigatórios não estão preenchidos!");
        }

        // Check date and hour
        if (blockSchedule.getDate().isBefore(LocalDateTime.now())) {
            throw new InvalidDataException("Data inválida!");
        }

        // Check if hour is within barbershop's opening hours
        LocalTime hourActual = blockSchedule.getDate().toLocalTime();

        if (hourActual.isBefore(barbershop.getOpenTime()) || hourActual.isAfter(barbershop.getCloseTime())) {
            throw new TimeException("O horário do bloqueio deve estar dentro do horário de funcionamento da barbearia!");
        }

        // Check employee schedulings
        Set<Scheduling> employeeSchedulings = schedulingRepository.findAllByEmployee(employee);

        for (Scheduling employeeScheduling : employeeSchedulings) {
            LocalDateTime employeeSchedulingStartTime = employeeScheduling.getDate();
            LocalDateTime employeeSchedulingEndTime = employeeSchedulingStartTime.plusMinutes(30);

            LocalDateTime schedulingStartTime = blockSchedule.getDate();
            LocalDateTime schedulingEndTime = schedulingStartTime.plusMinutes(30);

            // Verificar se o novo agendamento começa dentro de 30 minutos após um agendamento existente
            if ((schedulingStartTime.isEqual(employeeSchedulingStartTime) || schedulingStartTime.isAfter(employeeSchedulingStartTime)) &&
                    schedulingStartTime.isBefore(employeeSchedulingEndTime)) {
                throw new AlreadySelectedDataException("O barbeiro selecionado já possui um agendamento neste horário!");
            }

            if (schedulingStartTime.isBefore(employeeSchedulingEndTime) && schedulingEndTime.isAfter(employeeSchedulingEndTime)) {
                throw new AlreadySelectedDataException("O barbeiro selecionado já possui um agendamento neste horário!");
            }
        }

        // Check employee blockSchedule
        Set<BlockSchedule> employeeBlockSchedules = blockScheduleRepository.findAllByEmployee(employee);

        for (BlockSchedule employeeBlockSchedule : employeeBlockSchedules) {
            LocalDateTime employeeScheduleStartTime = employeeBlockSchedule.getDate();
            LocalDateTime employeeScheduleEndTime = employeeScheduleStartTime.plusMinutes(30);

            LocalDateTime blockScheduleStartTime = blockSchedule.getDate();
            LocalDateTime blockScheduleEndTime = blockScheduleStartTime.plusMinutes(30);

            // Verificar se o novo agendamento começa dentro de 30 minutos após um agendamento existente
            if ((blockScheduleStartTime.isEqual(employeeScheduleStartTime) || blockScheduleStartTime.isAfter(employeeScheduleStartTime)) &&
                    blockScheduleStartTime.isBefore(employeeScheduleEndTime)) {
                throw new AlreadySelectedDataException("O barbeiro selecionado já possui um bloqueio neste horário!");
            }

            if (blockScheduleStartTime.isBefore(employeeScheduleEndTime) && blockScheduleEndTime.isAfter(employeeScheduleEndTime)) {
                throw new AlreadySelectedDataException("O barbeiro selecionado já possui um bloqueio neste horário!");
            }
        }

        return true;
    }

}
