package com.example.campus_nest_backend.utils;

public class EmailTemplate {
    public static String getVerificationEmailTemplate(String name, String verificationLink) {
        return "<html>\n" +
                "<body>\n" +
                "<h1>Welcome to Campus Nest, " + name + "!</h1>\n" +
                "<p>Thank you for registering with us. Please click the link below to verify your email address:</p>\n" +
                "<a href=\"" + verificationLink + "\">Verify Email</a>\n" +
                "<p>If you did not register, please ignore this email.</p>\n" +
                "</body>\n" +
                "</html>";
    }

    public static String getWelcomeEmailTemplate(String name) {
        return "<html>\n" +
                "<body>\n" +
                "<h1>Welcome to Campus Nest, " + name + "!</h1>\n" +
                "<p>We are excited to have you on board. Explore our platform and connect with your campus community.</p>\n" +
                "</body>\n" +
                "</html>";
    }

}
