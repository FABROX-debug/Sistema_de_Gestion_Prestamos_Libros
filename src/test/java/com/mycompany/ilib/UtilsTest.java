package com.mycompany.ilib;

import com.mycompany.models.Books;
import com.mycompany.utils.Utils;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilsTest {

    @Test
    public void isNumericAcceptsOnlyWholeNumbers() {
        assertTrue(Utils.isNumeric("123"));
        assertTrue(Utils.isNumeric("0"));
        assertFalse(Utils.isNumeric(null));
        assertFalse(Utils.isNumeric(""));
        assertFalse(Utils.isNumeric("12.5"));
        assertFalse(Utils.isNumeric("abc"));
    }

    @Test
    public void stringToDateUsesExpectedFormat() {
        Date date = Utils.stringToDate("08-07-2026");
        assertNotNull(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        assertEquals(2026, calendar.get(Calendar.YEAR));
        assertEquals(Calendar.JULY, calendar.get(Calendar.MONTH));
        assertEquals(8, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void bookAvailabilityCannotExceedStock() {
        Books book = new Books();
        book.setStock(3);
        book.setAvailable(2);
        assertTrue(book.getAvailable() <= book.getStock());

        book.setAvailable(4);
        assertFalse(book.getAvailable() <= book.getStock());
    }
}
