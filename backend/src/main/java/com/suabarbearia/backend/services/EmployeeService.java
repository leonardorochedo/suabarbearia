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
import com.suabarbearia.backend.utils.EmailService;
import com.suabarbearia.backend.utils.ImageUploader;
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

    @Autowired
    private EmailService emailService;

    @Autowired
    private ImageUploader imageUploader;

    @Value("${fixedsalt}")
    private String fixedSalt;

    public Employee findById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);

        return employee.get();
    }

    public ApiResponse<Employee> profile(String authorizationHeader) {
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Employee employee = employeeRepository.findByUsername(JwtUtil.getUsernameFromToken(token));

        if (employee == null) {
            throw new InvalidTokenException("Token inválido!");
        }

        ApiResponse<Employee> response = new ApiResponse<Employee>("Perfil carregado!", employee);

        return response;
    }

    public ApiResponse<Employee> create(String authorizationHeader, CreateEmployeeDto employee) {
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Employee employeeFinded = employeeRepository.findByUsername(employee.getUsername());
        Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));

        if (barbershop == null) throw new NoPermissionException("Não autorizado! Precisa ser barbearia!");

        // Check data
        if (employeeFinded != null) {
            throw new ExistDataException("Usuário existente!");
        }

        if (employee.getName() == null || employee.getUsername() == null || employee.getPassword() == null || employee.getConfirmpassword() == null || employee.getPhone() == null) {
            throw new FieldsAreNullException("Um ou mais campos obrigatórios não estão preenchidos!");
        }

        if (!employee.getPassword().equals(employee.getConfirmpassword())) {
            throw new PasswordDontMatchException("As senhas não batem!");
        }

        // Encypt and hash pass
        String hashedPassword = BCrypt.hashpw(employee.getPassword(), fixedSalt);

        Employee newEmployee = employeeRepository.save(new Employee(null, employee.getUsername(), hashedPassword, employee.getName(), null, employee.getPhone(), barbershop));

        // Send email
        String subject = "Novo Funcionário - Sua Barbearia";
        String body = String.format(
                "Olá %s,\n\n"
                + "Gostaríamos de informar que um novo funcionário foi adicionado à sua equipe:\n\n"
                + "Nome: %s\n"
                + "Usuário: %s\n"
                + "Senha: %s\n\n"
                + "O novo membro da equipe agora tem acesso ao sistema. Certifique-se de fornecer as informações necessárias "
                + "e orientações para facilitar a integração.\n\n"
                + "Se houver alguma dúvida ou necessidade de suporte, não hesite em entrar em contato.\n\n"
                + "Atenciosamente,\n"
                + "Equipe Sua Barbearia",
                barbershop.getName(), employee.getName(), employee.getUsername(), employee.getPassword());

        emailService.sendEmail(barbershop.getEmail(), subject, body);

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

    public ApiResponse<Employee> barbershopEdit(String authorizationHeader, Long id, EditEmployeeDto employee, MultipartFile image) throws SQLException, IOException {
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Barbershop barbershopToken = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));
        Employee employeeId = employeeRepository.findById(id).get();
        Employee employeeNewName = employeeRepository.findByUsername(employee.getUsername());

        // Checking if barbershop have this employee
        if (barbershopToken == null) {
            throw new InvalidTokenException("Token inválido!");
        }

        if (!employeeId.getBarbershop().equals(barbershopToken)) {
            throw new NoPermissionException("Colaborador não encontrado para barbearia!");
        }

        // Verify data
        if (employee.getUsername() == null || employee.getName() == null || employee.getPhone() == null || employee.getPassword() == null || employee.getConfirmpassword() == null) {
            throw new FieldsAreNullException("Um ou mais campos obrigatórios não estão preenchidos!");
        }

        if (!employeeId.getUsername().equals(employee.getUsername()) && employeeNewName != null) {
            throw new ExistDataException("Usuário em uso!");
        }

        if (!employee.getPassword().equals(employee.getConfirmpassword())) {
            throw new PasswordDontMatchException("As senhas não batem!");
        }

        // Encypt and hash pass
        String hashedPassword = BCrypt.hashpw(employee.getPassword(), fixedSalt);

        // Update barbershop
        if(image != null && !image.isEmpty()) {
            String imageUrl = imageUploader.uploadFile(image, id.toString(), "employees");
            System.out.println(imageUrl);
        }
        employeeId.setName(employee.getName());
        employee.setPassword(hashedPassword);
        employeeId.setUsername(employee.getUsername());
        employeeId.setPhone(employee.getPhone());

        employeeRepository.save(employeeId);

        ApiResponse<Employee> response = new ApiResponse<Employee>("Colaborador editado com sucesso!", employeeId);

        return response;
    }

    public ApiResponse<Employee> edit(String authorizationHeader, Long id, EditEmployeeDto employee, MultipartFile image) throws SQLException, IOException {
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Employee employeeToken = employeeRepository.findByUsername(JwtUtil.getUsernameFromToken(token));
        Employee employeeId = employeeRepository.findById(id).get();
        Employee employeeNewName = employeeRepository.findByUsername(employee.getUsername());

        // Check employee
        if (employeeToken == null) {
            throw new InvalidTokenException("Token inválido!");
        }

        if (!employeeToken.equals(employeeId)) {
            throw new InvalidTokenException("Token inválido para este colaborador!");
        }

        // Verify data
        if (employee.getUsername() == null || employee.getName() == null || employee.getPhone() == null || employee.getPassword() == null || employee.getConfirmpassword() == null) {
            throw new FieldsAreNullException("Um ou mais campos obrigatórios não estão preenchidos!");
        }

        if (employeeId == null) {
            throw new FieldsAreNullException("Colaborador não encontrado!");
        }

        if (!employeeId.getUsername().equals(employee.getUsername()) && employeeNewName != null) {
            throw new ExistDataException("Usuário em uso!");
        }

        if (!employee.getPassword().equals(employee.getConfirmpassword())) {
            throw new PasswordDontMatchException("As senhas não batem!");
        }

        if (!BCrypt.checkpw(employee.getPassword(), employeeToken.getPassword())) {
            throw new InvalidDataException("Senha inválida!");
        }

        // Update employee
        if(!image.isEmpty()) {
            String imageUrl = imageUploader.uploadFile(image, id.toString(), "employees");
            employeeId.setImage(imageUrl);
        }
        employeeId.setName(employee.getName());
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

        String empName = employeeId.getName();
        String empUsername = employeeId.getUsername();

        employeeId.setName("Colaborador excluído!");
        employeeId.setUsername("");
        employeeId.setPhone("");
        employeeId.setImage(null);
        employeeId.setPassword("");

        employeeRepository.save(employeeId);

        // Send email
        String subject = "Funcionário Removido - Sua Barbearia";
        String body = String.format(
                "Olá %s,\n\n"
                + "Gostaríamos de informar que um funcionário foi removido da sua equipe:\n\n"
                + "Nome: %s\n"
                + "Usuário: %s\n\n"
                + "O funcionário não terá mais acesso ao sistema. Certifique-se de ajustar as responsabilidades e "
                + "informações necessárias para manter a operação suave.\n\n"
                + "Se houver alguma dúvida ou necessidade de suporte, não hesite em entrar em contato.\n\n"
                + "Atenciosamente,\n"
                + "Equipe Sua Barbearia",
                barbershopToken.getName(), empName, empUsername);

        emailService.sendEmail(barbershopToken.getEmail(), subject, body);

        TextResponse response = new TextResponse("Colaborador deletado com sucesso!");

        return response;
    }

    public TextResponse delete(String authorizationHeader) {
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        System.out.println(JwtUtil.getUsernameFromToken(token));

        Employee employeeToken = employeeRepository.findByUsername(JwtUtil.getUsernameFromToken(token));

        if (employeeToken == null) throw new NoPermissionException("Não autorizado!");

        String barName = employeeToken.getBarbershop().getName();
        String empName = employeeToken.getName();
        String empUsername = employeeToken.getUsername();
        String barEmail = employeeToken.getBarbershop().getEmail();

        employeeToken.setName("Colaborador excluído!");
        employeeToken.setUsername("");
        employeeToken.setPhone("");
        employeeToken.setImage(null);
        employeeToken.setPassword("");

        employeeRepository.save(employeeToken);

        // Send email
        String subject = "Funcionário Removido - Sua Barbearia";
        String body = String.format(
                "Olá %s,\n\n"
                + "Gostaríamos de informar que um funcionário foi removido da sua equipe:\n\n"
                + "Nome: %s\n"
                + "Usuário: %s\n\n"
                + "O funcionário não terá mais acesso ao sistema. Certifique-se de ajustar as responsabilidades e "
                + "informações necessárias para manter a operação suave.\n\n"
                + "Se houver alguma dúvida ou necessidade de suporte, não hesite em entrar em contato.\n\n"
                + "Atenciosamente,\n"
                + "Equipe Sua Barbearia",
                barName, empName, empUsername);

        emailService.sendEmail(barEmail, subject, body);

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
        if (!employeeId.getBarbershop().equals(barbershop)) throw new NoPermissionException("Colaborador não encontrado para barbearia!");

        Set<Scheduling> schedulings = schedulingRepository.findAllByEmployee(employeeId);

        double totalEarnings = 0.0;

        for (Scheduling scheduling : schedulings) {
            if (scheduling.getStatus() == Status.FINISHED) { // only done schedulings
                double servicePrice = scheduling.getService().getPrice();
                totalEarnings += servicePrice;
            }
        }

        return totalEarnings;
    }

    public double barbershopGetEarningsWithDate(String authorizationHeader, LocalDate initialDate, LocalDate endDate, Long id) {
        String token = JwtUtil.verifyTokenWithAuthorizationHeader(authorizationHeader);

        Barbershop barbershop = barbershopRepository.findByEmail(JwtUtil.getEmailFromToken(token));
        Employee employeeId = employeeRepository.findById(id).get();

        if (barbershop == null) throw new NoPermissionException("Não autorizado!");

        // checking if barbershop have this employee
        if (!employeeId.getBarbershop().equals(barbershop)) throw new NoPermissionException("Colaborador não encontrado para barbearia!");

        Set<Scheduling> schedulings = schedulingRepository.findAllByEmployee(employeeId);

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

}
