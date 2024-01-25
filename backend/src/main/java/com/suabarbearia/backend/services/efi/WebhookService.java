package com.suabarbearia.backend.services.efi;

import com.suabarbearia.backend.config.Credentials;
import com.suabarbearia.backend.efipay.EfiPix;
import com.suabarbearia.backend.entities.Scheduling;
import com.suabarbearia.backend.enums.Status;
import com.suabarbearia.backend.exceptions.InvalidDataException;
import com.suabarbearia.backend.repositories.SchedulingRepository;
import com.suabarbearia.backend.utils.EmailService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class WebhookService {

    @Autowired
    private Credentials credentials;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SchedulingRepository schedulingRepository;

    @Value("${pix.key}")
    private String pixKey;

    public JSONObject configureWebhook() throws Exception {

        EfiPix efiPix = new EfiPix();

        JSONObject response = efiPix.configureWebhook(credentials, pixKey, "https://suabarbearia-render.onrender.com/webhook");

        System.out.println(response);

        return response;
    }

    public JSONObject webhookRecieved(String body) {

        JSONObject response = new JSONObject(body);

        System.out.println(response);

        JSONArray pixArray = response.getJSONArray("pix");

        System.out.println(pixArray);

        JSONObject pixObject = pixArray.getJSONObject(0);

        System.out.println(pixObject);

        String paymentTXID = pixObject.getString("txid");

        System.out.println(paymentTXID);

        // Att scheduling
        Scheduling scheduling = schedulingRepository.findByPaymentTXID(paymentTXID);

        if (scheduling != null && scheduling.getStatus() == Status.WAITING_PAYMENT) {
            scheduling.setStatus(Status.PENDING);
            scheduling.setPaymentE2eId(pixObject.getString("endToEndId"));
            schedulingRepository.save(scheduling);
            System.out.println("Pagamento recebido com sucesso para o agendamento com ID: " + scheduling.getId());

            // Send email
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String formattedDate = scheduling.getDate().format(formatter);

            String subject = "Pagamento Realizado - Sua Barbearia";
            String bodyEmail = String.format(
                    "Olá %s,\n\n"
                            + "Recebemos o pagamento para o seu agendamento na Sua Barbearia. "
                            + "Seu agendamento está confirmado e aguardamos ansiosamente a sua visita.\n\n"
                            + "Detalhes do Agendamento:\n"
                            + "- Data: %s\n"
                            + "- Serviço: %s\n\n"
                            + "Se você tiver alguma dúvida ou precisar fazer alterações, entre em contato conosco.\n\n"
                            + "Atenciosamente,\n"
                            + "Equipe Sua Barbearia",
                    scheduling.getUser().getName(), formattedDate, scheduling.getService().getTitle());

            emailService.sendEmail(scheduling.getUser().getEmail(), subject, bodyEmail);
        } else {
            System.out.println("Agendamento já foi processado para o paymentId: " + paymentTXID);
        }

        return response;
    }

}
