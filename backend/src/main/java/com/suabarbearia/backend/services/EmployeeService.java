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
        Employee editedEmployee = employeeRepository.findById(id).get();
        Employee employeeNewUsername = employeeRepository.findByUsername(employee.getUsername());

        if (barbershopToken == null) throw new NoPermissionException("Não autorizado!");

        // checking if barbershop have this employee
        Employee existEmployee = null;
        Set<Employee> employeesBarbershop = employeeRepository.findAllByBarbershop(barbershopToken);

        for(Employee employeeBarberShop : employeesBarbershop){
            if (employeeBarberShop == editedEmployee) {
                existEmployee = employeeBarberShop;
                break;
            }
        }

        if (existEmployee == null) throw new NoPermissionException("Colaborador não encontrado para barbearia!");

        /* Verify new data
        if (employee.getUsername() == null | employee.getPassword() == null | employee.getPhone() == null | employee.getImage() == null | employee.get){
            throw new FieldsAreNullException("Um ou mais campos obrigatórios não estão preenchidos!");
        }

        if (!barbershopToken.getEmail().equals(barbershop.getEmail()) && barbershopNewEmail != null) {
            throw new ExistDataException("E-mail em uso!");
        }

        if (!BCrypt.checkpw(barbershop.getPassword(), editedBarbershop.getPassword())) {
            throw new PasswordDontMatchException("A senha não pode ser alterada aqui!");
        }

        if (!barbershop.getPassword().equals(barbershop.getConfirmpassword())) {
            throw new PasswordDontMatchException("As senhas não batem!");
        }

        // Update barbershop
        editedBarbershop.setImage(image.getBytes());
        editedBarbershop.setName(barbershop.getName());
        editedBarbershop.setEmail(barbershop.getEmail());
        editedBarbershop.setDocument(barbershop.getDocument());
        editedBarbershop.setBirth(barbershop.getBirth());
        editedBarbershop.setPhone(barbershop.getPhone());
        editedBarbershop.setAddress(barbershop.getAddress());
        editedBarbershop.setOpenTime(barbershop.getOpenTime());
        editedBarbershop.setCloseTime(barbershop.getCloseTime());

        barbershopRepository.save(editedBarbershop);*/

        ApiResponse<Employee> response = new ApiResponse<Employee>("Barbearia editada com sucesso!", editedEmployee);

        return response;
    }

}
