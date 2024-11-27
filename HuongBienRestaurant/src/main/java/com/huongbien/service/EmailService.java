package com.huongbien.service;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Properties;

public class EmailService {

    private final String username;
    private final String appPassword;

    public EmailService(String username, String appPassword) {
        this.username = username;
        this.appPassword = appPassword;
    }

    public static boolean sendEmailWithOTP(String recipientEmail, String otp, String username, String appPassword) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, appPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Mã OTP của bạn");
            message.setText("Mã OTP để khôi phục mật khẩu là: " + otp);

            Transport.send(message);
            System.out.println("Email OTP đã được gửi thành công!");
            return true;

        } catch (MessagingException e) {
            System.err.println("Lỗi khi gửi email OTP: " + e.getMessage());
            return false;
        }
    }

    public boolean sendEmailWithQRCode(String recipientEmail, String attachmentPath) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, appPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("ĐÂY LÀ MÃ QRCODE CỦA BẠN!");

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("HÃY LƯU VỀ MÁY ĐỂ CÓ THỂ SỬ DỤNG KHI ĐẾN NHÀ HÀNG CHÚNG TÔI!");

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(attachmentPath);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);
            Transport.send(message);
            System.out.println("Email đã được gửi thành công!");
            return true;

        } catch (MessagingException | IOException e) {
            System.err.println("Lỗi khi gửi email: " + e.getMessage());
            return false;
        }
    }
}
