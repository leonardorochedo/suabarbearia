//package com.suabarbearia.backend.services;
//
//import com.suabarbearia.backend.dtos.CreateEmployeeDto;
//import com.suabarbearia.backend.dtos.SigninEmployeeDto;
//import com.suabarbearia.backend.entities.Barbershop;
//import com.suabarbearia.backend.entities.Employee;
//import com.suabarbearia.backend.exceptions.*;
//import com.suabarbearia.backend.repositories.BarbershopRepository;
//import com.suabarbearia.backend.repositories.EmployeeRepository;
//import com.suabarbearia.backend.responses.ApiResponse;
//import com.suabarbearia.backend.responses.ApiTokenResponse;
//import com.suabarbearia.backend.utils.JwtUtil;
//import org.mindrot.jbcrypt.BCrypt;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//
//import java.util.Optional;
//
//@org.springframework.stereotype.Service
//public class EmployeeService {
//
//    @Autowired
//    private EmployeeRepository employeeRepository;
//
//    @Autowired
//    private BarbershopRepository barbershopRepository;
//
//    @Value("${fixedsalt}")
//    private String fixedSalt;
//
//    public Employee findById(Long id) {
//        Optional<Employee> employee = employeeRepository.findById(id);
//
//        return employee.get();
//    }
//
//    public ApiResponse<Employee> create(String authorizationHeader, CreateEmployeeDto employee) {
//        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);
//
//        Employee employeeFinded = employeeRepository.findByUsername(employee.getUsername());
//
//        Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));
//
//        if (barbershop == null) throw new NoPermissionException("Não autorizado! Precisa ser barbearia!");
//
//        // Check data
//        if (employeeFinded != null && employeeFinded.getBarbershop().equals(barbershop)) {
//            throw new ExistDataException("Funcionário existente!");
//        }
//
//        if (employee.getName() == null || employee.getUsername() == null || employee.getPassword() == null || employee.getConfirmpassword() == null || employee.getPhone() == null) {
//            throw new FieldsAreNullException("Um ou mais campos obrigatórios não estão preenchidos!");
//        }
//
//        if (!employee.getPassword().equals(employee.getConfirmpassword())) {
//            throw new PasswordDontMatchException("As senhas não batem!");
//        }
//
//        // Encypt and hash pass
//        String hashedPassword = BCrypt.hashpw(employee.getPassword(), fixedSalt);
//        employee.setPassword(hashedPassword);
//
//        Employee newEmployee = employeeRepository.save(new Employee(null, employee.getUsername(), employee.getPassword(), employee.getName(), null, employee.getPhone(), barbershop));
//
//        ApiResponse<Employee> response = new ApiResponse<Employee>("Funcionário criado com sucesso!", newEmployee);
//
//        return response;
//    }
//
//    public ApiTokenResponse<Employee> signin(SigninEmployeeDto employee) {
//        if (employee.getUsername().isEmpty() || employee.getPassword().isEmpty()) {
//            throw new FieldsAreNullException("Usuário ou senha não exitente!");
//        }
//
//        Employee employeeFinded = employeeRepository.findByUsername(employee.getUsername());
//
//        // Check data
//        if (employeeFinded == null) {
//            throw new ResourceNotFoundException("Funcionário não existente!");
//        }
//
//
//        if (!BCrypt.checkpw(employee.getPassword(), employeeFinded.getPassword())) {
//            throw new InvalidDataException("Usuário ou senha inválidos!");
//        }
//
//        String token = JwtUtil.generateToken(employee.getUsername());
//
//        // Create a response
//        ApiTokenResponse<Employee> response = new ApiTokenResponse<Employee>("Funcionário logado com sucesso!", token, employeeFinded);
//
//        return response;
//    }
//
//}
