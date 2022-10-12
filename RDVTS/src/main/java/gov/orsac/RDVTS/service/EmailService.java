package gov.orsac.RDVTS.service;


import gov.orsac.RDVTS.dto.MailDto;

public interface EmailService {
    Boolean sendHtmlMail(MailDto mailDto);
}
