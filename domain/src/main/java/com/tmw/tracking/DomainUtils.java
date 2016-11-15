package com.tmw.tracking;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @author dmikhalishin@provectus-it.com
 */
public class DomainUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String DATE_FORMAT_WITH_TZ = "yyyy-MM-dd'T'HH:mm:ss.S";

    public static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("UTC");

    public static final List<String> MUST_BE_SELECTED_DIVISIONS = new ArrayList<String>(){{add("60");}{add("G0");}};

    public static String errorToString(final Throwable e){
        final Writer writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        return writer.toString();
    }

    public static boolean isLong(final String value){
        try {
            Long.valueOf(value);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public static boolean isInteger(final String value) {
        try {
            Integer.valueOf(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static <T> List<T> getLimitResult(EntityManager entityManager, Class<T> cls, final String sql, final Integer start, final Integer count) {
        final TypedQuery<T> query = entityManager.createQuery(sql, cls);
        if(count != null)
            query.setMaxResults(count);
        if(start != null)
            query.setFirstResult(start);
        return query.getResultList();
    }

    public static Integer getRowCount(EntityManager entityManager, Class entity, final String where) {
        final Query query = entityManager.createQuery("select count(*) from " + entity.getName() + (StringUtils.isBlank(where) ? "" : " " + where));
        try {
            return ((Number) query.getSingleResult()).intValue();
        } catch(NoResultException e) {
            return 0;
        }
    }


    public static String getTimeAsString(final Date date){
        if(date == null)return null;
        final Calendar current = Calendar.getInstance();
        current.setTime(new Date());
        Date transformed = DateUtils.setYears(date, current.get(Calendar.YEAR));
        transformed = DateUtils.setMonths(transformed, current.get(Calendar.MONTH));
        transformed = DateUtils.setDays(transformed, current.get(Calendar.DAY_OF_MONTH));
        final SimpleDateFormat timeDateFormat = new SimpleDateFormat("HH:mm");
        //if(store != null && StringUtils.isNotBlank(store.getTimeZone()))
        //  timeDateFormat.setTimeZone(TimeZone.getTimeZone(store.getTimeZone()));
        return timeDateFormat.format(transformed);
    }



}
