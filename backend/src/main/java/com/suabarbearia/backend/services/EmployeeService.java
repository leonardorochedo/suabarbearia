package com.suabarbearia.backend.services;

import com.suabarbearia.backend.dtos.CreateEmployeeDto;
import com.suabarbearia.backend.dtos.EditBarbershopDto;
import com.suabarbearia.backend.dtos.EditEmployeeDto;
import com.suabarbearia.backend.dtos.SigninEmployeeDto;
import com.suabarbearia.backend.entities.Barbershop;
import com.suabarbearia.backend.entities.Employee;
import com.suabarbearia.backend.exceptions.*;
import com.suabarbearia.backend.repositories.BarbershopRepository;
import com.suabarbearia.backend.repositories.EmployeeRepository;
import com.suabarbearia.backend.responses.ApiResponse;
import com.suabarbearia.backend.responses.ApiTokenResponse;
import com.suabarbearia.backend.responses.ErrorResponse;
import com.suabarbearia.backend.responses.TextResponse;
import com.suabarbearia.backend.utils.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

@org.springframework.stereotype.Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private BarbershopRepository barbershopRepository;

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
        Employee existEmployee = null;
        Set<Employee> employeesBarbershop = employeeRepository.findAllByBarbershop(barbershopToken);

        for(Employee employeeBarberShop : employeesBarbershop){
            if (employeeBarberShop == employeeId) {
                existEmployee = employeeBarberShop;
                break;
            }
        }
        if (existEmployee == null) throw new NoPermissionException("Colaborador não encontrado para barbearia!");

        // Verify new data
        if (!existEmployee.getUsername().equals(employee.getUsername())) {
            throw new ExistDataException("Usuário em uso!");
        }

        if (!employee.getPassword().equals(employee.getConfirmpassword())) {
            throw new PasswordDontMatchException("As senhas não batem!");
        }

        // Update barbershop
        existEmployee.setImage(image.getBytes());
        existEmployee.setName(employee.getName());
        existEmployee.setPassword(employee.getPassword());
        existEmployee.setUsername(employee.getUsername());
        existEmployee.setPhone(employee.getPhone());

        employeeRepository.save(existEmployee);

        ApiResponse<Employee> response = new ApiResponse<Employee>("Colaborador editado com sucesso!", existEmployee);

        return response;
    }

    public ApiResponse<?> edit(String authorizationHeader, Long id, EditEmployeeDto employee, MultipartFile image) throws SQLException, IOException, NoPermissionException, FieldsAreNullException {
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Employee employeeToken = employeeRepository.findByUsername(JwtUtil.getEmailFromToken(token));
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
        Employee existEmployee = null;
        Set<Employee> employeesBarbershop = employeeRepository.findAllByBarbershop(barbershopToken);

        for(Employee employeeBarberShop : employeesBarbershop){
            if (employeeBarberShop == employeeId) {
                existEmployee = employeeBarberShop;
                break;
            }
        }
        if (existEmployee == null) throw new NoPermissionException("Colaborador não encontrado para barbearia!");

        existEmployee.setName("Barbearia excluída!");
        existEmployee.setUsername("");
        existEmployee.setPhone("");
        existEmployee.setImage(null);
        existEmployee.setPassword("");

        employeeRepository.save(existEmployee);

        TextResponse response = new TextResponse("Colaborador deletado com sucesso!");

        return response;
    }

    public TextResponse delete(String authorizationHeader, Long id) {
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Employee employeeToken = employeeRepository.findByUsername(JwtUtil.getEmailFromToken(token));
        Employee employeeId = employeeRepository.findById(id).get();

        if (employeeToken == null) throw new NoPermissionException("Não autorizado!");

        if (employeeId == null) throw new FieldsAreNullException("Colaborador não encontrado!");

        if (employeeToken != employeeId) throw new NoPermissionException("Não autorizado!");

        employeeId.setName("Barbearia excluída!");
        employeeId.setUsername("");
        employeeId.setPhone("");
        employeeId.setImage(null);
        employeeId.setPassword("");

        employeeRepository.save(employeeId);

        TextResponse response = new TextResponse("Colaborador deletado com sucesso!");

        return response;
    }

}
