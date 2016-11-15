package com.tmw.tracking.service.impl;

import com.tmw.tracking.DomainUtils;
import com.tmw.tracking.domain.MonitoringType;
import com.tmw.tracking.domain.TestResult;
import com.tmw.tracking.mail.MailSender;
import com.tmw.tracking.service.AuthenticationService;
import com.tmw.tracking.service.MonitoringService;
import com.tmw.tracking.web.hibernate.EntityManagerProvider;
import com.tmw.tracking.web.support.logstash.NamedThreadFactory;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * User: enedzvetsky@provectus-it.com
 */
public class MonitoringServiceImpl implements MonitoringService {
    private final static Logger logger = LoggerFactory.getLogger(MonitoringServiceImpl.class);
    private final EntityManager entityManager;
    private EntityManagerProvider entityManagerProvider;
    private AuthenticationService authenticationLogic;
    private final MailSender mailSender;
    private final String userName;
    private Integer monitoringPeriod;
    private static List<TestResult> lastResults;
    private ScheduledExecutorService executor;

    @Inject
    public MonitoringServiceImpl(
                                 final EntityManager entityManager,
                                 final EntityManagerProvider entityManagerProvider,
                                 final AuthenticationService authenticationLogic,
                                 final MailSender mailSender,
                                 @Named("job.user") final String userName,
                                 @Named("monitoring.period") final Integer monitoringPeriod) {
        this.entityManager = entityManager;
        this.entityManagerProvider = entityManagerProvider;
        this.authenticationLogic = authenticationLogic;
        this.mailSender = mailSender;
        this.userName = userName;
        this.monitoringPeriod = monitoringPeriod;
    }

    @Override
    public List<TestResult> startTests(List<MonitoringType> monitoringTypes) {
        List<TestResult> results = new ArrayList<TestResult>();
        for(MonitoringType monitoringType : monitoringTypes) {
            results.add(startTest(monitoringType));
        }
        return results;
    }

    @Override
    public TestResult startTest(MonitoringType test) {
        TestResult tr;
        switch (test) {
            case SEND_EMAIL: tr = testSendEmail(); break;
            case LOGIN: tr = testLogin(); break;
            default:
                tr = new TestResult(test);
                tr.setTestResult("Test wasn't implemented: " + test.getDesc());
                break;
        }
        return tr;
    }

    private TestResult testLogin() {
        TestResult tr = new TestResult(MonitoringType.LOGIN);
        try {
            authenticationLogic.login(userName,null);
            tr.setTestResult(TestResult.ALL_WORKING_NORMALLY);
            tr.setSuccess(true);
        } catch(Exception e) {
            tr.setTestResult(e.getMessage());
            logger.error("Error during execution " + this.getClass().getName() + " " + e.getMessage(),e);
        }
        return tr;
    }

    private TestResult testSendEmail() {
        final TestResult tr = new TestResult(MonitoringType.SEND_EMAIL);
        try {
            if(mailSender.checkConnection()) {
                tr.setSuccess(true);
                tr.setTestResult(TestResult.ALL_WORKING_NORMALLY);
            } else {
                tr.setTestResult("Can't connect to SMTP server");
            }
        } catch(MessagingException e) {
            tr.setTestResult(e.getMessage());
            logger.error("Error during execution " + this.getClass().getName() + " " + e.getMessage(),e);
        }
        return tr;
    }

    @Override
    public List<TestResult> getLastMonitoring() {
        return lastResults;
    }

    @Override
    public void startMonitoring() {
        if (executor == null) executor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("GeneralMonitoringService", true));
        executor.scheduleWithFixedDelay(new MonitoringTask(), 3, monitoringPeriod, TimeUnit.MINUTES);

    }

    private TestResult testDb() {
        final TestResult tr = new TestResult(MonitoringType.TRACKING_DATABASE);
        final Session session = entityManager.unwrap(Session.class);
        session.doWork(new Work() {
                           public void execute(final Connection jdbcConnection) throws SQLException {
                               PreparedStatement preparedStatement = null;
                               ResultSet resultSet = null;
                               try {
                                   preparedStatement = jdbcConnection.prepareStatement("SELECT 1 FROM alteration_order WHERE rownum=1");
                                   preparedStatement.execute();
                                   resultSet = preparedStatement.getResultSet();
                                   if(resultSet.next()) {
                                       tr.setSuccess(true);
                                       tr.setTestResult(TestResult.ALL_WORKING_NORMALLY);
                                   }
                               } catch(Exception e) {
                                   logger.error(DomainUtils.errorToString(e));
                                   tr.setTestResult(e.getMessage());
                               } finally {
                                   if(resultSet != null)
                                       try {
                                           resultSet.close();
                                       } catch(SQLException ignored) {
                                       }
                                   if(preparedStatement != null)
                                       try {
                                           preparedStatement.close();
                                       } catch(SQLException ignored) {
                                       }
                               }
                           }
                       }
        );
        return tr;
    }

    class MonitoringTask implements Runnable {

        private List<MonitoringType> tasks;

        public MonitoringTask() {
            tasks = new ArrayList<MonitoringType>();
            for (MonitoringType type: MonitoringType.values()) {
                if (!type.isSeparate()) {
                    tasks.add(type);
                }
            }
        }

        public MonitoringTask(List<MonitoringType> tasks) {
            this.tasks = tasks;
        }

        @Override
        public void run() {
            try {
                entityManagerProvider.create();
                try {
                    lastResults = startTests(tasks);
                } finally {
                    entityManagerProvider.close();
                }
            } catch(Exception e) {
                logger.error("Error during execution " + this.getClass().getName() + " " + e.getMessage(), e);
            }

        }

        /*private void sendEmailOnFault() {
            //String smtpAppenderTo = (String)MDC.get(Utils.MDC_SMTP_APPENDER_EMAIL_TO);
            if (!lastResults.isEmpty() && smtpAppenderTo != null && !smtpAppenderTo.isEmpty()) {
                for (TestResult result: lastResults) {
                    if (MonitoringType.ACTIVE_PRINTERS_STATE == result.getMonitoringType() && !result.isSuccess()) {
                        List<String> emails = new ArrayList<String>();
                        String[] emailArray = smtpAppenderTo.split(",");
                        emails.addAll(Arrays.asList(emailArray));

                        mailSender.sendEmailMessages(emails, "Monitoring services fault", result.getTestResult());
                    }
                }
            }
        }*/
    }

}
