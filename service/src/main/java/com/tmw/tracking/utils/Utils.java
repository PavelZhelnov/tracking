package com.tmw.tracking.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.tmw.tracking.DomainUtils;
import com.tmw.tracking.mail.MailSender;
import com.tmw.tracking.web.service.exceptions.ServiceException;
import com.tmw.tracking.web.service.util.error.ErrorCode;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;

public class Utils extends DomainUtils {

    private final static Logger logger = LoggerFactory.getLogger(Utils.class);

    private static final Configuration configuration = new Configuration();

    public static final String PERSIST_MODULE_NAME = "tracking-rest";
    private static final String[] WORKING_DAY_DATE_FORMAT = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ssZZ"};
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");

    public static final String ORDER_GARBAGE_CRON = "order.garbage.cron";

    // MDC logging keys...
    public static final String MDC_JOB_METHOD = "job-method";
    public static final String MDC_USER = "user";
    public static final String MDC_CALL_ID = "call-id";
    public static final String MDC_METHOD = "method";
    public static final String MDC_API_METHOD = "api-method";
    public static final String MDC_IP_ADDRESS = "ip_address";
    public static final String MDC_REQUEST_ID = "request_id";
    public static final String MDC_DURATION = "duration";

    private static final SimpleModule module = new SimpleModule("tracking", new Version(1, 0, 0, null, null, null));

    public static final String DEFAULT_PHONE_NUMBER = "999-999-9999";


    public final static HostnameVerifier LENIENT_HOSTNAME_VERIFIER = new javax.net.ssl.HostnameVerifier() {
        /**
         * {@inheritDoc}
         * @see HostnameVerifier#verify(String, javax.net.ssl.SSLSession)
         */
        @Override
        public boolean verify(final String hostname,
                              final javax.net.ssl.SSLSession sslSession) {

            logger.warn("Ignoring Hostname Verification for " + hostname);

            return true;
        }
    };

    static {
        configuration.setTemplateLoader(new ClassTemplateLoader(MailSender.class, "/"));
        module.addSerializer(new DateSerializer());
    }

    public static Properties getProperties() {
        Utils.initializeTrackingEnv();
        return getPropertiesHelper("tracking-"
                + System.getProperties().getProperty("tracking.env") + ".properties");
    }

    private static Properties getPropertiesHelper(String path) {
        final Properties applicationProperties = new Properties();
        try {
            InputStream propertiesInputStream = Utils.class.getClassLoader().getResourceAsStream(path);
            applicationProperties.load(propertiesInputStream);
        } catch (IOException e) {
            logger.error(errorToString(e));
        }
        return applicationProperties;
    }

    public static XMLGregorianCalendar getXMLGregorianCalendarImpl(final Date date) {
        final GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        try {
            final XMLGregorianCalendar calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
            calendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
            return calendar;
        } catch (DatatypeConfigurationException e) {
            return null;
        }
    }

    /**
     * Initialize the Environment
     */
    public static void initializeTrackingEnv() {
        // This allows developers to run this code locally without adding an environment variable
        if (StringUtils.isEmpty(System.getProperty("tracking.env"))) {
            System.setProperty("tracking.env", "dev");
            logger.warn("Setting tracking.env to "
                    + System.getProperty("tracking.env")
                    + ".  It's recommended to explicitly set this java command line parameter in a startup script/configuration. (-Dtracking.env=local)");
        }

    }

    // ------------------------------------------------------------------------

    public static String getIPAddress() {
        final StringBuilder address = new StringBuilder();
        try {
            for (final NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isLoopback()) continue;
                for (final java.net.InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    final java.net.InetAddress inetAddress = interfaceAddress.getAddress();
                    if (inetAddress instanceof java.net.Inet4Address) {
                        final java.net.Inet4Address inet4Address = (java.net.Inet4Address) inetAddress;
                        final byte[] ipAddress = inet4Address.getAddress().clone();
                        if (ipAddress.length != 4) continue;
                        for (final byte ipAddressElement : ipAddress) {
                            if (address.length() > 0) address.append('.');
                            address.append(Long.valueOf(String.format("%03d", ipAddressElement & 0xff)));
                        }
                        break;
                    }
                }
                if (address.length() != 0) break;
            }
        } catch (Exception e) {
            logger.error(errorToString(e));
            return null;
        }
        return address.toString();
    }

    public static String toJson(final Object object) {
        final StringWriter stringWriter = new StringWriter();
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(stringWriter, object);
        } catch (Exception e) {
            logger.error(errorToString(e));
        }
        return stringWriter.toString();
    }

    public static String toJsonWithDate(Object object) {   //TODO this is  hack for date, need refactor whole jersey in a right way
        final StringWriter stringWriter = new StringWriter();
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(module);
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(new JsonSerializer<Date>() {

                @Override
                public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                    if (date != null) jsonGenerator.writeString(sdf.format(date));
                }

                @Override
                public java.lang.Class<Date> handledType() {
                    return Date.class;
                }
            });
            mapper.registerModule(simpleModule);
            mapper.writeValue(stringWriter, object);
        } catch (Exception e) {
            logger.error(errorToString(e));
        }
        return stringWriter.toString();
    }

    public static Integer getColorCode(final String itemCode) {
        if (StringUtils.isBlank(itemCode) || itemCode.length() < 3 || !Utils.isLong(itemCode.substring(itemCode.length() - 2)))
            return null;
        return Integer.valueOf(itemCode.substring(itemCode.length() - 2));
    }

    public static Integer getCurrentDateInteger() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String month = ((calendar.get(Calendar.MONTH)+1) < 10 ? "0" : "") + (calendar.get(Calendar.MONTH)+1);
        String day = (calendar.get(Calendar.DATE) < 10 ? "0" : "") + calendar.get(Calendar.DATE);
        String date = calendar.get(Calendar.YEAR)+month+day;
        return new Integer(date);
    }

    public static ObjectMapper initObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
        return objectMapper;
    }

    public static Template getFreemarkerTemplate(final String templateURL) throws IOException {
        return configuration.getTemplate(templateURL);
    }

    public static Date parseDate(final String date) {
        return parseDate(date, "yyyy-MM-dd");
    }

    public static Date parseDate(final String date, String format) {
        if (StringUtils.isBlank(date)) return null;

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseDateWithoutTime(final String date) {
        if (StringUtils.isBlank(date)) return null;
        try {
            return DateUtils.parseDate(date.length() > 10 ? date.substring(0, 10) : date, new String[]{"yyyy-MM-dd"});
        } catch (ParseException e) {
            return null;
        }
    }

    public static String dateToString(final Date date, TimeZone timeZone) {
        if (date == null) return "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        if (timeZone != null) {
        	dateFormat.setTimeZone(timeZone);
        }
        return dateFormat.format(date);
    }
    
    
    /**
     * @return - current date with trunced time
     */
    public static Date getCurrentDate() {
        return DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
    }

    public static Properties getServerVersionProperties() {
        return getPropertiesHelper("server_version.properties");
    }

    public static Date getDateInTimeZone(Date date, String timeZoneId) {
        if (date == null || timeZoneId == null)
            return null;
        TimeZone tz = TimeZone.getTimeZone(timeZoneId);
        return getDateInTimeZone(date, tz);
    }

    public static Date getDateInTimeZone(Date date, TimeZone timeZone) {
        if (date == null || timeZone == null)
            return null;
        Calendar mbCal = new GregorianCalendar(timeZone);
        mbCal.setTimeInMillis(date.getTime());
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, mbCal.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, mbCal.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, mbCal.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, mbCal.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, mbCal.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, mbCal.get(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, mbCal.get(Calendar.MILLISECOND));
        return cal.getTime();
    }
    
    public static boolean isProdEnv() {
        return System.getProperties().getProperty("tracking.env").equalsIgnoreCase("prod");
    }

    public static String exceptionReason(Throwable e) {
        // NullPointerExceptions and some others do not have a Message, so just return the Exception name.
        return StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : e.getClass().getSimpleName();
    }


    public static Date getFirstDayOfMonth(Date currentDate) {
        Calendar calendar = Calendar.getInstance();   // this takes current date
        calendar.setTime(currentDate);
        clearTime(calendar);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static Date getFirstDayOfYear(Date currentDate) {
        Calendar calendar = Calendar.getInstance();   // this takes current date
        calendar.setTime(currentDate);
        clearTime(calendar);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    public static Date getFirstDayOfWeek(Date currentDate) {
        Calendar calendar = Calendar.getInstance();   // this takes current date
        calendar.setTime(currentDate);
        clearTime(calendar);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return calendar.getTime();
    }

    private static void clearTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
    }


    public static String formatDate(Date date, String format) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat(format, Locale.US);
        return formatter.format(date);
    }

    public static <T> List<Set<T>> splitSet(Set<T> original, int batchSize) {
        int count = (int)roundUp(original.size(), batchSize);
        ArrayList<Set<T>> partitionList = new ArrayList<Set<T>>(count);
        Iterator<T> it = original.iterator();
        for (int i = 0; i < count; i++) {
            HashSet<T> partition = new HashSet<T>(batchSize + 1);
            partitionList.add(partition);
            for (int j = 0; j < batchSize && it.hasNext(); j++) {
                partition.add(it.next());
            }
        }
        return partitionList;
    }

    private static long roundUp(long num, long divisor) {
        return (num + divisor - 1) / divisor;
    }

    public static String getImageStream(InputStream inputStream) {
        byte[] imgBytes = new byte[0];
        try {
            imgBytes = IOUtils.toByteArray(inputStream);
            byte[] imgBytesAsBase64 = Base64.encodeBase64(imgBytes);
            String imgDataAsBase64 = new String(imgBytesAsBase64);
            String imgAsBase64 = imgDataAsBase64;
            return imgAsBase64;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    public static Long createTransferDocumentNumber(Integer store, Integer ntsFriendlyStore) {
        String storeString = store + "0" + (ntsFriendlyStore<1000?"0":"") + ntsFriendlyStore;
        return new Long(storeString);
    }

    public static String encryptPassword(String password) {
        if (password == null) {
            return null;
        }
        String algorithm = "SHA";

        byte[] plainText = password.getBytes();

        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR);
        }
        md.reset();
        md.update(plainText);
        byte[] encodedPassword = md.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < encodedPassword.length; i++) {
            if ((encodedPassword[i] & 0xff) < 0x10) {
                sb.append("0");
            }
            sb.append(Long.toString(encodedPassword[i] & 0xff, 16));
        }
        return sb.toString();
    }

}
