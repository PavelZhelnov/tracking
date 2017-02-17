package com.tmw.tracking.utils;

import junit.framework.TestCase;
import org.junit.Test;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UtilsTest extends TestCase {

    @Test
    public void testGetFirstDayOfTheYear() {
        Calendar calendar = Calendar.getInstance();
        Date fdy = Utils.getFirstDayOfYear(calendar.getTime());

        Calendar fdyCalendar = Calendar.getInstance();
        fdyCalendar.setTime(fdy);
        assertEquals(fdyCalendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR));
        assertEquals(fdyCalendar.get(Calendar.MONTH), Calendar.JANUARY);
        assertEquals(fdyCalendar.get(Calendar.DATE), 1);
    }

    @Test
    public void testGetFirstDayOfTheWeek() {
        Calendar calendar = Calendar.getInstance();
        Date fdw = Utils.getFirstDayOfWeek(calendar.getTime());

        Calendar fdwCalendar = Calendar.getInstance();
        fdwCalendar.setTime(fdw);
        assertEquals(fdwCalendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR));
        assertEquals(fdwCalendar.get(Calendar.MONTH), calendar.get(Calendar.MONTH));
        assertEquals(fdwCalendar.get(Calendar.DAY_OF_WEEK), Calendar.MONDAY);
    }

    @Test
    public void testGetFirstDayOfTheMonth() {
        Calendar calendar = Calendar.getInstance();
        Date fdm = Utils.getFirstDayOfMonth(calendar.getTime());

        Calendar fdmCalendar = Calendar.getInstance();
        fdmCalendar.setTime(fdm);
        assertEquals(fdmCalendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR));
        assertEquals(fdmCalendar.get(Calendar.MONTH), calendar.get(Calendar.MONTH));
        assertEquals(fdmCalendar.get(Calendar.DATE), 1);
    }

    @Test
    public void testParsingDate() {
        Date parsed = Utils.parseDate("2016-03-12");
        assertNotNull(parsed);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parsed);
        assertEquals(calendar.get(Calendar.MONTH), 2);
        assertEquals(calendar.get(Calendar.YEAR), 2016);
        assertEquals(calendar.get(Calendar.DATE), 12);
    }

    @Test
    public void testSplitSetForThreePartitions() {
        int count = 10;
        Set<Integer> haystack = new HashSet<Integer>();
        for (int i = 0; i <= 21; i++) {
            haystack.add(i);
        }
        List<Set<Integer>> partitioned = Utils.splitSet(haystack, count);
        assertNotNull(partitioned);
        assertEquals(3, partitioned.size());
        assertEquals(count, partitioned.get(0).size());
        for (int i = 0; i < count; i++) {
            assertTrue(partitioned.get(0).contains(i));
        }
        assertEquals(2, partitioned.get(2).size());
        assertTrue(partitioned.get(2).contains(20));
        assertTrue(partitioned.get(2).contains(21));
    }

    @Test
    public void testSplitSetForTwoEqualPartitions() {
        int count = 10;
        Set<Integer> haystack = new HashSet<Integer>();
        for (int i = 0; i < 20; i++) {
            haystack.add(i);
        }
        List<Set<Integer>> partitioned = Utils.splitSet(haystack, count);
        assertNotNull(partitioned);
        assertEquals(2, partitioned.size());
        assertEquals(count, partitioned.get(0).size());
        for (int i = 0; i < count; i++) {
            assertTrue(partitioned.get(0).contains(i));
        }
        assertEquals(count, partitioned.get(1).size());
    }

    @Test
    public void testGetCurrentDate() {
        Date date = Utils.getCurrentDate();
        assertNotNull(date);
    }


    @Test
    public void testGetCurrentDateInInteger() {
        Integer currentDate = Utils.getCurrentDateInteger();
        assertNotNull(currentDate);
        String curDateString = String.valueOf(currentDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        assertTrue(curDateString.contains(String.valueOf(calendar.get(Calendar.YEAR))));
        assertTrue(curDateString.contains(String.valueOf(calendar.get(Calendar.MONTH)+1)));
        assertTrue(curDateString.contains(String.valueOf(calendar.get(Calendar.DATE))));
    }

    @Test
    public void testCreateTransferDocumentNumber() {
        assertEquals(new Long(110600602), Utils.createTransferDocumentNumber(1106, 602));
        assertEquals(new Long(110601602), Utils.createTransferDocumentNumber(1106, 1602));
    }

    @Test
    public void testFormatDate() {
        Date date = new Date();
        String formatted = Utils.formatDate(date, "mm/dd/yyyy");
        System.out.println(formatted);
        assertNotNull(formatted);
    }

    @Test
    public void testConnection() {
        Connection conn =  new JavaMysqlConnection().getConnection();
        if(conn == null)
            System.out.println("Connection was not established");
        else
            System.out.println("Connection is successfully established");
    }

}
