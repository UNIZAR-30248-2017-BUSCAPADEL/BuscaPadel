package company.buscapadel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.abs;

/**
 * Created by ivansantamaria on 1/12/17.
 */

public class ComprobarDatos {

    public boolean superaElMes(Date time1, Date time2) {
        int daysApart = (int)((time2.getTime() - time1.getTime()) / (1000*60*60*24l));
        if (abs(daysApart) >= 30)
            return true;
        else
            return false;
    }

    public int analizarDatos(String fecha, String hora, String lugar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
        String[] horaParts = hora.split(":");
        Date today = Calendar.getInstance().getTime();
        int horaNum = Integer.parseInt(horaParts[0]);
        Date dateSelected = null;
        try {
            dateSelected = simpleDateFormat.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (fecha.equals("") || hora.equals("") || lugar.equals("")) {
            return 1;
        }
        else if (dateSelected.before(today) || superaElMes(today, dateSelected)) {
            return 2;
        }
        else if (horaNum < 9 || horaNum >= 21) {
            return 3;
        }
        else {
            return 0;
        }
    }

    public boolean esFechaCorrecta(SimpleDateFormat simpleDateFormat, String fecha) {
        Date dateSelected = null;
        boolean fechaCorrecta = true;
        try {
            dateSelected = simpleDateFormat.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
            fechaCorrecta = false;
        }
        return fechaCorrecta;
    }
}
