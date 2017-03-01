package com.tmw.tracking.web;

import com.google.inject.Injector;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.tmw.tracking.dao.UserDao;
import com.tmw.tracking.domain.LoginRequest;
import com.tmw.tracking.domain.LoginResponse;
import com.tmw.tracking.filter.TrackingAuthenticationToken;
import com.tmw.tracking.filter.TrackingCredentialsMatcher;
import com.tmw.tracking.filter.TrackingSecurityRealm;
import com.tmw.tracking.utils.Utils;
import com.tmw.tracking.web.hibernate.EntityManagerProvider;
import com.tmw.tracking.web.service.auth.AuthenticationResource;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;
import java.util.TimeZone;

public abstract class TrackingBaseUnitTest {
    private static final Logger logger = LoggerFactory.getLogger(TrackingBaseUnitTest.class);
    private static final String DATA_XML = "test-data.xml";
    public static final String DEFAULT_USER_ID = "test@example.com";
    public static final String DEFAULT_USER_PASS = "Admin123";

    protected static Injector injector;
    private UserDao userDao;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        if (StringUtils.isEmpty(System.getProperty("tracking.env"))) {
            System.setProperty("tracking.env", "tests");
            addJNDI();
            logger.warn("Setting tracking.env to " + System.getProperty("tracking.env")
                    + ".  It's recommended to explicitly set this java command line parameter in a startup script/configuration. (-Dtracking.env=local)");
        }

        if(injector == null) {
            injector = GuiceInstanceHolderTest.getInjector();
        }
    }

    @Before
    public void setUp() throws Exception {
        initDBEnv();
    }

    @After
    public  void tearDown() throws Exception {
        final EntityManagerProvider entityManagerProvider = injector.getInstance(EntityManagerProvider.class);
        entityManagerProvider.close();
    }


    public static void initDBEnv() throws Exception {
        final Properties properties = Utils.getProperties();

        final EntityManagerProvider entityManagerProvider = injector.getInstance(EntityManagerProvider.class);
        final AuthenticationResource authenticationResource = injector.getInstance(AuthenticationResource.class);

        final TrackingSecurityRealm trackingSecurityRealm = new TestTrackingSecurityRealm();
        final TrackingCredentialsMatcher matcher = new TrackingCredentialsMatcher();
        matcher.setHashAlgorithmName("sha-256");
        trackingSecurityRealm.setCredentialsMatcher(matcher);
        SecurityUtils.setSecurityManager(new DefaultSecurityManager(trackingSecurityRealm));

        entityManagerProvider.create();
        entityManagerProvider.applyGlobalFilters();

        LoginRequest loginRequest = createLoginRequest(properties);
        loginUser(authenticationResource, loginRequest);


    }

    private static LoginRequest createLoginRequest(Properties properties) {
        LoginRequest loginRequest = new LoginRequest();
        String userId = properties.getProperty("auth.user.id");
        String userPass = properties.getProperty("auth.user.password");
        loginRequest.setUserId(userId!=null?userId:DEFAULT_USER_ID);
        loginRequest.setPassword(userPass!=null?userPass:DEFAULT_USER_PASS);
        return loginRequest;
    }

    private static Subject loginUser(final AuthenticationResource authenticationResource, LoginRequest loginRequest ) throws Exception {
        int count = 0;
        while (count < 5) {
            try {
                ensureUserIsLoggedOut();
                Subject subject = getSubject();
                LoginResponse response = authenticationResource.login(loginRequest);

                logger.debug("Login with userid:" + loginRequest.getUserId());
                subject.login(new TrackingAuthenticationToken(response.getToken()));
                return subject;
            } catch (Exception e) {
                logger.error("Error Login with token:" + e.getMessage());
                Thread.sleep(1000L);
                count ++;
            }
        }

        return getSubject();
    }

    private static Subject getSubject() {
        Subject currentUser = ThreadContext.getSubject();// SecurityUtils.getSubject();

        if (currentUser == null) {
            currentUser = SecurityUtils.getSubject();
        }

        return currentUser;
    }

    private static void ensureUserIsLoggedOut() {
        try {
            // Get the user if one is logged in.
            Subject currentUser = getSubject();
            if (currentUser == null)
                return;

            // Log the user out and kill their session if possible.
            currentUser.logout();
            org.apache.shiro.session.Session session = currentUser.getSession(false);
            if (session == null)
                return;

            session.stop();
        }
        catch (Exception e) {
            logger.error("Logout error:"+e.getMessage());
        }
    }

    // ========================================================================
    private static  void addJNDI(){
        try {
            // Create initial context
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.naming.java.javaURLContextFactory");
            System.setProperty(Context.URL_PKG_PREFIXES,
                    "org.apache.naming");
            InitialContext ic = new InitialContext();

            ic.createSubcontext("java:");
            ic.createSubcontext("java:comp");
            ic.createSubcontext("java:comp/env");
            ic.createSubcontext("java:comp/env/jdbc");

            // Construct DataSource
            final Properties properties = Utils.getProperties();
            final MysqlDataSource ds = new MysqlDataSource();
            ds.setURL("jdbc:mysql://" + properties.get("hibernate.connection.host")
                    + ":" + properties.get("hibernate.connection.port") +"/" + properties.get("hibernate.connection.db"));
            ds.setUser((String)properties.get("hibernate.connection.username"));
            ds.setPassword((String)properties.get("hibernate.connection.password"));

            ic.bind("java:comp/env/jdbc/tracking", ds);
        } catch (Exception ex) {
            logger.error(Utils.errorToString(ex));
        }
    }

}
