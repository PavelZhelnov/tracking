package com.tmw.tracking.mail;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.utils.DynamicConfig;
import com.tmw.tracking.utils.Utils;
import com.tmw.tracking.web.hibernate.EntityManagerProvider;
import com.tmw.tracking.web.service.exceptions.ServiceException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

/**
 * Mail sender
 *
 * @author dmikhalishin@provectus-it.com
 */
@Singleton
public class MailSender {

    private final static Logger logger = LoggerFactory.getLogger(MailSender.class);
    private final static String FROM_LABEL = "Tracking System";
    private final String from;
    private final String user;
    private final String password;
    private final ExecutorService executorService;
    private EntityManagerProvider entityManagerProvider;
    private DynamicConfig dynamicConfig;

    @Inject
    public MailSender(@Named("mail.smtp.from") final String from, @Named("mail.user") final String user,
                      @Named("mail.password") final String password, @Named("default") final ExecutorService executorService,
                      DynamicConfig dynamicConfig, final EntityManagerProvider entityManagerProvider) {
        this.from = from;
        this.user = user;
        this.password = password;
        this.executorService = executorService;
        this.dynamicConfig = dynamicConfig;
        this.entityManagerProvider = entityManagerProvider;
    }

    public void sendEmailMessageForUsers(final List<User> users, final String subject, final String message) {
        if (users == null || users.size() == 0)
            throw new ServiceException("Email cannot be sent. No recipients.");
        // send email in separate thread
        final List<String> emails = new ArrayList<String>();
        for (final User user : users)
            emails.add(user.getEmail());
        sendEmailMessages(emails, subject, message);
    }

    public void sendEmailMessages(final List<String> emails, final String subject, final String message) {
        final String contentType = "text/plain; charset=utf-8";
        executorService.submit(new Runnable() {
            /**
             * {@inheritDoc}
             *
             * @see Runnable#run()
             */
            @Override
            public void run() {
                sendEmail(from, emails, subject, contentType, message, null, null, null);
            }
        });
    }

    /**
     */
    public boolean checkConnection() throws MessagingException {
        Session mailSession = getMailSession();
        boolean connected;
        Transport transport = null;
        try {
            transport = mailSession.getTransport();
            transport.connect();
            connected = transport.isConnected();
        } finally {
            if (transport != null) {
                transport.close();
            }
        }
        return connected;
    }

    /**
     * Send email
     *
     * @param from                    the from email address. Cannot be {@code null}
     * @param to                      the to email address. Cannot be {@code null}
     * @param subject                 the email subject. Cannot be {@code null}
     * @param body                    the email body. Cannot be {@code null}
     * @param attachmentFileNames     - collection of attachment filenames
     * @param attachmentsContentTypes - collection of attachment types
     * @param attachments             - collection of attachments
     */
    protected void sendEmail(final String from, final Collection<String> to, final String subject, String contentType,
                             final String body, final List<String> attachmentFileNames, final List<String> attachmentsContentTypes,
                             final List<byte[]> attachments) {
        if (!dynamicConfig.isAllowSendMail()) return;
        Session mailSession = getMailSession();
        final int id = new Object().hashCode(); // Just need a unique id to link
        // log messages.
        try {
            Message message = new MimeMessage(mailSession);
            Multipart multipart = new MimeMultipart();
            message.setFrom(new InternetAddress(from, FROM_LABEL));

            final InternetAddress[] internetAddresses = new InternetAddress[to.size()];
            final Iterator<String> it = to.iterator();
            int index = 0;

            while (it.hasNext()) {
                String email = it.next();
                if (email == null) {
                    logger.warn("[" + id + "] Cannot send email [" + subject + "] to: " + to + ". ");
                    continue;
                }
                internetAddresses[index] = new InternetAddress(email);
                index++;
            }

            if (internetAddresses.length == 0) {
                throw new ServiceException("[" + id + "] Email cannot be sent. for empty recipients");
            }

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            if (body != null) {
                if (contentType == null) {
                    contentType = "text/html; charset=utf-8";
                }
                messageBodyPart.setContent(body, contentType);
                multipart.addBodyPart(messageBodyPart);
            }

            message.addRecipients(Message.RecipientType.TO, internetAddresses);
            message.setSubject(subject);
            if (attachments != null) {
                if (attachments.size() != attachmentsContentTypes.size()
                        || attachments.size() != attachmentFileNames.size()) {
                    throw new IllegalArgumentException(
                            "attachments,attachmentsContentTypes,filenames lists should have the same size");
                }
                for (int i = 0; i < attachments.size(); i++) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    DataSource source = new ByteArrayDataSource(attachments.get(i), attachmentsContentTypes.get(i));
                    attachmentPart.setDataHandler(new DataHandler(source));
                    attachmentPart.setFileName(attachmentFileNames.get(i));
                    multipart.addBodyPart(attachmentPart);
                }
            }

            message.setContent(multipart);

            logger.info("[" + id + "] Sending email [" + subject + "] to " + to);
            Transport.send(message);
            logger.info(to.toString());
            logger.info("[" + id + "] Sent message successfully....");
        } catch (Exception e) {
            logger.error("[" + id + "] Cannot send email [" + subject + "] to: " + to + ". " + Utils.errorToString(e));
            throw new ServiceException("[" + id + "] Email cannot be sent.");
        }
    }

    private Session getMailSession() {
        final Properties properties = Utils.getProperties();
        return StringUtils.isBlank(user) ? Session.getDefaultInstance(properties) : Session.getDefaultInstance(
                properties, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, password);
                    }
                });
    }

}
