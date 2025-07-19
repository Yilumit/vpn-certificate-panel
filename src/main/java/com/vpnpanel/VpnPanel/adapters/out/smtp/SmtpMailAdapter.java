package com.vpnpanel.VpnPanel.adapters.out.smtp;

import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.vpnpanel.VpnPanel.adapters.exceptions.EmailSendingException;
import com.vpnpanel.VpnPanel.application.ports.out.MailPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmtpMailAdapter implements MailPort {

    private final JavaMailSender javaMailSender;

    @Override
    public void send(String recipient, String subject, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(recipient);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            javaMailSender.send(mailMessage);

            log.info("E-mail enviado para {}", recipient);
        } catch (MailException e) {
            if (e instanceof MailAuthenticationException) {
                log.error("Falha na autenticação SMTP ao enviar o e-mail para {}: {}", recipient, e.getMessage());
                throw new EmailSendingException("Falha na autenticação no servidor de e-mail", e);

            } else if (e instanceof MailSendException) {
                log.error("Erro no envio do e-mail para {}: {}", recipient, e.getMessage());
                throw new EmailSendingException("Erro ao enviar o e-mail: falha na comunicação com o servidor", e);

            } else if (e instanceof MailParseException) {
                log.error("Erro na estruturação do e-mail para {}: {}", recipient, e.getMessage());
                throw new EmailSendingException("Erro na estrutura do e-mail. Verifique os campos.", e);

            } else {
                log.error("Erro inesperado ao enviar o e-mail para {}: {}", recipient, e.getMessage());
                throw new EmailSendingException("Erro inesperado ao enviar o e-mail", e);
            }
        }
    }

    @Override
    public String createFinalizationEmail(String name, String url) {
        return """
                Prezado %s,

                Sua conta no Painel de VPNs foi criada e é necessário definir uma senha para ativá-la, para continuar o procedimento,
                copie o link abaixo e cole-o no navegador de internet:

                %s

                Qualquer dúvida, entre em contato com um administrador.
                """
                .formatted(name, url);
    }

    @Override
    public String createResetEmail(String name, String link) {
        return """
                Prezado %s,

                Foi solicitado uma redefinição de senha para sua conta no Painel de VPNs, para concluir a redefinição,
                copie o link abaixo e cole-o em seu navegador de internet:

                %s

                Caso desconheça essa solicitação ou tenha alguma dúvida, entre em contato com um administrador.
                """
                .formatted(name, link);
    }

}
