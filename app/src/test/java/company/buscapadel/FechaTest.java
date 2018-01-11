package company.buscapadel;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import company.buscapadel.ComprobarDatos;

import static org.junit.Assert.assertEquals;

/**
 * Created by ivansantamaria on 1/12/17.
 */

public class FechaTest {
    public static ComprobarDatos comprobarDatos = new ComprobarDatos();

    @Test
    public void analizarDatosTipo1() throws Exception {
        assertEquals(1, comprobarDatos.analizarDatos("", "2:15", "a"));
    }
    @Test
    public void analizarDatosTipo2() throws Exception {
        assertEquals(2, comprobarDatos.analizarDatos("96-08-12", "2:15", "a"));
    }
    @Test
    public void analizarDatosTipo3() throws Exception {
        assertEquals(3, comprobarDatos.analizarDatos("18-2-10", "1:00", "a"));
    }
    @Test
    public void analizarDatosTipo4() throws Exception {
        assertEquals(0, comprobarDatos.analizarDatos("18-2-10", "10:00", "a"));
    }

    @Test
    public void superaElMes() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
        Date dateSelected = null;
        try {
            dateSelected = simpleDateFormat.parse("18-12-12");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals(true, comprobarDatos.superaElMes(Calendar.getInstance().getTime(), dateSelected));
    }
    @Test
    public void noSuperaElMes() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
        Date dateSelected = null;
        try {
            dateSelected = simpleDateFormat.parse("2018-2-10");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals(false,comprobarDatos.superaElMes(Calendar.getInstance().getTime(), dateSelected));
    }
}
