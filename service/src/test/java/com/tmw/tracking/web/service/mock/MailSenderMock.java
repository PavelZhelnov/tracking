package com.tmw.tracking.web.service.mock;

import com.google.inject.Singleton;
import com.tmw.tracking.mail.MailSender;
import com.tmw.tracking.service.impl.ReportServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * @author dmikhalishin@provectus-it.com
 */
@Singleton
public class MailSenderMock extends MailSender {

    private final List<String> emailSubjects = new ArrayList<String>();

    public MailSenderMock() {
        super(null, null, null, Executors.newFixedThreadPool(2), new ReportServiceImpl(), null, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void sendEmail(final String from, final Collection<String> to, final String subject,String contentType, final String body, final List<String>attachmentFileNames,final List<String>contentTypes, final List<byte[]>attachments){
        emailSubjects.add(subject);
    }

    public List<String> getEmailSubjects() {
        return emailSubjects;
    }

    public void clear(){
        emailSubjects.clear();
    }
}