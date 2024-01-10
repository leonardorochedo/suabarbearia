package com.suabarbearia.backend.services;

import com.suabarbearia.backend.dtos.CreateEmployeeDto;
import com.suabarbearia.backend.dtos.EditEmployeeDto;
import com.suabarbearia.backend.dtos.SigninEmployeeDto;
import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.entities.Employee;
import com.suabarbearia.backend.entities.Scheduling;
import com.suabarbearia.backend.enums.Status;
import com.suabarbearia.backend.exceptions.*;
import com.suabarbearia.backend.repositories.BarbershopRepository;
import com.suabarbearia.backend.repositories.EmployeeRepository;
import com.suabarbearia.backend.repositories.SchedulingRepository;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.ApiTokenResponse;
import com.suabarbearia.backend.responses.TextResponse;
import com.suabarbearia.backend.utils.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@org.springframework.stereotype.Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private BarbershopRepository barbershopRepository;

    @Autowired
    private SchedulingRepository schedulingRepository;

    @Value("${fixedsalt}")
    private String fixedSalt;

    public Employee findById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);

        return employee.get();
    }

    public ApiResponse<Employee> create(String authorizationHeader, CreateEmployeeDto employee) {
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Employee employeeFinded = employeeRepository.findByUsername(employee.getUsername());

        Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));

        if (barbershop == null) throw new NoPermissionException("Não autorizado! Precisa ser barbearia!");

        // Check data
        if (employeeFinded != null && employeeFinded.getBarbershop().equals(barbershop)) {
            throw new ExistDataException("Funcionário existente!");
        }

        if (employee.getName() == null || employee.getUsername() == null || employee.getPassword() == null || employee.getConfirmpassword() == null || employee.getPhone() == null) {
            throw new FieldsAreNullException("Um ou mais campos obrigatórios não estão preenchidos!");
        }

        if (!employee.getPassword().equals(employee.getConfirmpassword())) {
            throw new PasswordDontMatchException("As senhas não batem!");
        }

        // Encypt and hash pass
        String hashedPassword = BCrypt.hashpw(employee.getPassword(), fixedSalt);
        employee.setPassword(hashedPassword);

        Employee newEmployee = employeeRepository.save(new Employee(null, employee.getUsername(), employee.getPassword(), employee.getName(), null, employee.getPhone(), barbershop));

        ApiResponse<Employee> response = new ApiResponse<Employee>("Funcionário criado com sucesso!", newEmployee);

        return response;
    }

    public ApiTokenResponse<Employee> signin(SigninEmployeeDto employee) {

        // Check data
        if (employee.getUsername().isEmpty() || employee.getPassword().isEmpty()) {
            throw new FieldsAreNullException("Usuário ou senha não exitente!");
        }

        Employee employeeFinded = employeeRepository.findByUsername(employee.getUsername());

        if (employeeFinded == null) {
            throw new ResourceNotFoundException("Funcionário não existente!");
        }


        if (!BCrypt.checkpw(employee.getPassword(), employeeFinded.getPassword())) {
            throw new InvalidDataException("Usuário ou senha inválidos!");
        }

        String token = JwtUtil.generateToken(employee.getUsername());

        // Create a response
        ApiTokenResponse<Employee> response = new ApiTokenResponse<Employee>("Funcionário logado com sucesso!", token, employeeFinded);

        return response;
    };

    public ApiResponse<?> barbershopEdit(String authorizationHeader, Long id, EditEmployeeDto employee, MultipartFile image) throws SQLException, IOException, NoPermissionException, FieldsAreNullException {
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Barbershop barbershopToken = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));
        Employee employeeId = employeeRepository.findById(id).get();

        if (barbershopToken == null) throw new NoPermissionException("Não autorizado!");

        // checking if barbershop have this employee
        if (!employeeId.getBarbershop().equals(barbershopToken)) throw new NoPermissionException("Colaborador não encontrado para barbearia!");

        // Verify new data
        if (!employeeId.getUsername().equals(employee.getUsername())) {
            throw new ExistDataException("Usuário em uso!");
        }

        if (!employee.getPassword().equals(employee.getConfirmpassword())) {
            throw new PasswordDontMatchException("As senhas não batem!");
        }

        // Update barbershop
        employeeId.setImage(image.getBytes());
        employeeId.setName(employee.getName());
        employeeId.setPassword(employee.getPassword());
        employeeId.setUsername(employee.getUsername());
        employeeId.setPhone(employee.getPhone());

        employeeRepository.save(employeeId);

        ApiResponse<Employee> response = new ApiResponse<Employee>("Colaborador editado com sucesso!", employeeId);

        return response;
    }

    public ApiResponse<?> edit(String authorizationHeader, Long id, EditEmployeeDto employee, MultipartFile image) throws SQLException, IOException, NoPermissionException, FieldsAreNullException {
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Employee employeeToken = employeeRepository.findByUsername(JwtUtil.getUsernameFromToken(token));
        Employee employeeId = employeeRepository.findById(id).get();

        if (employeeToken == null) throw new NoPermissionException("Não autorizado!");

        if (employeeId == null) throw new FieldsAreNullException("Colaborador não encontrado!");

        if (employeeToken != employeeId) throw new NoPermissionException("Não autorizado!");

        // Verify new data
        if (!employeeId.getUsername().equals(employee.getUsername())) {
            throw new ExistDataException("Usuário em uso!");
        }

        if (!employee.getPassword().equals(employee.getConfirmpassword())) {
            throw new PasswordDontMatchException("As senhas não batem!");
        }

        // Update barbershop
        employeeId.setImage(image.getBytes());
        employeeId.setName(employee.getName());
        employeeId.setPassword(employee.getPassword());
        employeeId.setUsername(employee.getUsername());
        employeeId.setPhone(employee.getPhone());

        employeeRepository.save(employeeId);

        ApiResponse<Employee> response = new ApiResponse<Employee>("Colaborador editado com sucesso!", employeeId);

        return response;
    }

    public TextResponse barbershopDelete(String authorizationHeader, Long id) {
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Barbershop barbershopToken = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));
        Employee employeeId = employeeRepository.findById(id).get();

        if (barbershopToken == null) throw new NoPermissionException("Não autorizado!");

        // checking if barbershop have this employee
        if (!employeeId.getBarbershop().equals(barbershopToken)) throw new NoPermissionException("Colaborador não encontrado para barbearia!");

        employeeId.setName("Colaborador excluído!");
        employeeId.setUsername("");
        employeeId.setPhone("");
        employeeId.setImage(null);
        employeeId.setPassword("");

        employeeRepository.save(employeeId);

        TextResponse response = new TextResponse("Colaborador deletado com sucesso!");

        return response;
    }

    public TextResponse delete(String authorizationHeader) {
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Employee employeeToken = employeeRepository.findByUsername(JwtUtil.getUsernameFromToken(token));

        if (employeeToken == null) throw new NoPermissionException("Não autorizado!");

        employeeToken.setName("Colaborador excluído!");
        employeeToken.setUsername("");
        employeeToken.setPhone("");
        employeeToken.setImage(null);
        employeeToken.setPassword("");

        employeeRepository.save(employeeToken);

        TextResponse response = new TextResponse("Colaborador deletado com sucesso!");

        return response;
    }

    public double getEarnings(String authorizationHeader) {
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Employee employee = employeeRepository.findByUsername(JwtUtil.getUsernameFromToken(token));

        Set<Scheduling> schedulings = schedulingRepository.findAllByEmployee(employee);

        double totalEarnings = 0.0;

        for (Scheduling scheduling : schedulings) {
            if (scheduling.getStatus() == Status.FINISHED) { // only done schedulings
                double servicePrice = scheduling.getService().getPrice();
                totalEarnings += servicePrice;
            }
        }

        return totalEarnings;
    }

    public double getEarningsWithDate(String authorizationHeader, LocalDate initialDate, LocalDate endDate) {
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Employee employee = employeeRepository.findByUsername(JwtUtil.getUsernameFromToken(token));

        Set<Scheduling> schedulings = schedulingRepository.findAllByEmployee(employee);

        double totalEarnings = 0.0;

        for (Scheduling scheduling : schedulings) {
            LocalDate schedulingDate = scheduling.getDate().toLocalDate();

            if (scheduling.getStatus() == Status.FINISHED && (schedulingDate.isEqual(initialDate) || schedulingDate.isAfter(initialDate)) &&
                    (schedulingDate.isEqual(endDate) || schedulingDate.isBefore(endDate.plusDays(1)))) { // only done schedulings and is btwn dates
                double servicePrice = scheduling.getService().getPrice();
                totalEarnings += servicePrice;
            }
        }

        return totalEarnings;
    }

    public double barbershopGetEarnings(String authorizationHeader, Long id) {
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));
        Employee employeeId = employeeRepository.findById(id).get();

        if (barbershop == null) throw new NoPermissionException("Não autorizado!");

        // checking if barbershop have this employee
        Employee existEmployee = null;
        Set<Employee> employeesBarbershop = employeeRepository.findAllByBarbershop(barbershop);

        for(Employee employeeBarberShop : employeesBarbershop){
            if (employeeBarberShop == employeeId) {
                existEmployee = employeeBarberShop;
                break;
            }
        }

        if (existEmployee == null) throw new NoPermissionException("Colaborador não encontrado para barbearia!");

        Set<Scheduling> schedulings = schedulingRepository.findAllByEmployee(existEmployee);

        double totalEarnings = 0.0;

        for (Scheduling scheduling : schedulings) {
            if (scheduling.getStatus() == Status.FINISHED) { // only done schedulings
                double servicePrice = scheduling.getService().getPrice();
                totalEarnings += servicePrice;
            }
        }

        double response = totalEarnings;

        return response;
    }

    public double barbershopGetEarningsWithDate(String authorizationHeader, LocalDate initialDate, LocalDate endDate, Long id) {
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));
        Employee employeeId = employeeRepository.findById(id).get();

        if (barbershop == null) throw new NoPermissionException("Não autorizado!");

        // checking if barbershop have this employee
        Employee existEmployee = null;
        Set<Employee> employeesBarbershop = employeeRepository.findAllByBarbershop(barbershop);

        for(Employee employeeBarberShop : employeesBarbershop){
            if (employeeBarberShop == employeeId) {
                existEmployee = employeeBarberShop;
                break;
            }
        }

        Set<Scheduling> schedulings = schedulingRepository.findAllByBarbershop(barbershop);

        double totalEarnings = 0.0;

        for (Scheduling scheduling : schedulings) {
            LocalDate schedulingDate = scheduling.getDate().toLocalDate();

            if (scheduling.getStatus() == Status.FINISHED && (schedulingDate.isEqual(initialDate) || schedulingDate.isAfter(initialDate)) &&
                    (schedulingDate.isEqual(endDate) || schedulingDate.isBefore(endDate.plusDays(1)))) { // only done schedulings and is btwn dates
                double servicePrice = scheduling.getService().getPrice();
                totalEarnings += servicePrice;
            }
        }

        double response = totalEarnings;

        return response;
    }

}
