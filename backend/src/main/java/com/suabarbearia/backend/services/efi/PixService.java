package com.suabarbearia.backend.services.efi;

import com.suabarbearia.backend.config.Credentials;
import com.suabarbearia.backend.dtos.efi.GeneratePixBody;
import com.suabarbearia.backend.dtos.efi.RefundPixBody;
import com.suabarbearia.backend.efipay.EfiPix;
import com.suabarbearia.backend.exceptions.efi.InsufficientMoneyException;
import com.suabarbearia.backend.utils.EmailService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PixService {

    @Autowired
    private Credentials credentials;

    @Autowired
    private EmailService emailService;

    @Value("${pix.key}")
    private String pixKey;

    public JSONObject generatePix(GeneratePixBody body) throws Exception {
        EfiPix payment = new EfiPix();

        // Generate PIX
        JSONObject pix = payment.generatePix(credentials, body.getDebtorName(), body.getDebtorCPF(), pixKey, body.getChargeAmount(), body.getDescription());

        // Generate QRCode
        JSONObject getLoc = pix.getJSONObject("loc");
        JSONObject qrcode = payment.generateQRCode(credentials, getLoc.get("id").toString());

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
                body.getDebtorName(), linkPayment);

        emailService.sendEmail(body.getEmail(), subject, bodyEmail);

        return response;
    }

    public JSONObject detailPix(String txid) throws Exception {
        EfiPix payment = new EfiPix();

        return payment.detailPix(credentials, txid);
    }

    public JSONObject refundPix(RefundPixBody body) throws Exception {
        EfiPix payment = new EfiPix();

        // Validation money
        if (Double.parseDouble(body.getChargeAmount()) < 5.00) {
            throw new InsufficientMoneyException("O valor do reembolso é menor do que a multa!");
        }

        Double chargeFinalValue = Double.parseDouble(body.getChargeAmount()) - 5.00;

        JSONObject response = payment.refundPix(credentials, body.getE2eId(), body.getId(), chargeFinalValue.toString());

        // Send email
        String subject = "Pagamento Gerado - Sua Barbearia";
        String bodyEmail = String.format(
                "Olá %s,\n\n"
                        + "Reembolsamos o valor solicitado para a sua conta bancária.\n"
                        + "Este reembolso pode levar até 24 horas para ser processado. Se você tiver alguma dúvida ou "
                        + "não reconhecer essa transação, entre em contato conosco imediatamente.\n\n"
                        + "Atenciosamente,\n"
                        + "Equipe Sua Barbearia",
                body.getName());

        emailService.sendEmail(body.getEmail(), subject, bodyEmail);

        return response;
    }

    // Withdraw money must be in here, after we made this CNPJ account

}
