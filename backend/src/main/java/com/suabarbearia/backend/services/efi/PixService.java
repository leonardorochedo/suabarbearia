package com.suabarbearia.backend.services.efi;

import com.suabarbearia.backend.config.Credentials;
import com.suabarbearia.backend.efipay.EfiPix;
import com.suabarbearia.backend.entities.Scheduling;
import com.suabarbearia.backend.entities.User;
import com.suabarbearia.backend.enums.Status;
import com.suabarbearia.backend.exceptions.efi.InsufficientMoneyException;
import com.suabarbearia.backend.exceptions.efi.InvalidStatusException;
import com.suabarbearia.backend.repositories.SchedulingRepository;
import com.suabarbearia.backend.utils.EmailService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PixService {

    @Autowired
    private Credentials credentials;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SchedulingRepository schedulingRepository;

    @Value("${pix.key}")
    private String pixKey;

    public JSONObject generatePix(Long id) throws Exception {

        EfiPix payment = new EfiPix();

        Scheduling scheduling = schedulingRepository.findById(id).get();
        User user = scheduling.getUser();

        if (scheduling.getStatus() == Status.PENDING || scheduling.getStatus() == Status.FOUL || scheduling.getStatus() == Status.CANCELED || scheduling.getStatus() == Status.FINISHED) {
            throw new InvalidStatusException("O agendamento está em outros processos!");
        }

        // Generate PIX
        JSONObject pix = payment.generatePix(credentials, user.getName(), user.getCpf(), pixKey, scheduling.getService().getPrice().toString(), scheduling.getService().getTitle());

        // Get attributes
        String paymentId = pix.getJSONObject("loc").get("id").toString();
        String paymentTXID = pix.get("txid").toString();
        String paymentCharge = pix.getJSONObject("valor").get("original").toString();

        // Generate QRCode
        JSONObject qrcode = payment.generateQRCode(credentials, paymentId);

        // Add in scheduling
        scheduling.setDateGeneratePayment(LocalDateTime.now());
        scheduling.setPaymentTXID(paymentTXID);
        scheduling.setPaymentId(paymentId);
        scheduling.setPaymentCharge(paymentCharge);

        schedulingRepository.save(scheduling);

        // Juntando os dois JSON
        JSONObject response = new JSONObject();

        response.put("pix", pix);
        response.put("qrcode", qrcode);

        // Send email
        String linkPayment = qrcode.get("linkVisualizacao").toString();
        String subject = "Pagamento Gerado - Sua Barbearia";
        String bodyEmail = String.format(
                "Olá %s,\n\n"
                        + "Recebemos uma solicitação de pagamento para o seu agendamento na Sua Barbearia. "
                        + "Se você não fez essa solicitação, ignore este e-mail. Caso contrário, clique no link abaixo para "
                        + "realizar o pagamento via Pix:\n\n"
                        + "Link para pagamento Pix: %s\n\n"
                        + "Este link é válido por 1 hora.\n\n"
                        + "Atenciosamente,\n"
                        + "Equipe Sua Barbearia",
                user.getName(), linkPayment);

        emailService.sendEmail(user.getEmail(), subject, bodyEmail);

        return response;
    }

    public JSONObject detailPix(String txid) throws Exception {
        EfiPix payment = new EfiPix();

        return payment.detailPix(credentials, txid);
    }

    public JSONObject refundPix(Long id) throws Exception {

        EfiPix payment = new EfiPix();

        Scheduling scheduling = schedulingRepository.findById(id).get();
        User user = scheduling.getUser();

        // Validations
        if (scheduling.getStatus() == Status.WAITING_PAYMENT) {
            throw new InvalidStatusException("O valor do agendamento ainda não foi debitado!");
        }

        if (Double.parseDouble(scheduling.getPaymentCharge()) < 5.00) {
            throw new InsufficientMoneyException("O valor do reembolso é menor do que a multa!");
        }

         Double chargeFinalValue = Double.parseDouble(scheduling.getPaymentCharge()) - 5.00;

        JSONObject response = payment.refundPix(credentials, scheduling.getPaymentE2eId(), scheduling.getPaymentId(), chargeFinalValue.toString());

        // Send email
        String subject = "Pagamento Reembolsado - Sua Barbearia";
        String bodyEmail = String.format(
                "Olá %s,\n\n"
                        + "Reembolsamos o valor solicitado para a sua conta bancária.\n"
                        + "Este reembolso pode levar até 24 horas para ser processado. Se você tiver alguma dúvida ou "
                        + "não reconhecer essa transação, entre em contato conosco imediatamente.\n\n"
                        + "Atenciosamente,\n"
                        + "Equipe Sua Barbearia",
                user.getName());

        emailService.sendEmail(user.getEmail(), subject, bodyEmail);

        return response;
    }

    // Withdraw money must be in here, after we made this CNPJ account

}
