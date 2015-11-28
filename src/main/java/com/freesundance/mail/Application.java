package com.freesundance.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class Application {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    SimpleMailMessage mail;

    public void sendMail() {
        javaMailSender.send(mail);
    }

    public static void main(String args[]) throws Exception {
        ((Application) new ClassPathXmlApplicationContext("classpath:application-context.xml").getBean("application")).sendMail();
    }
}
