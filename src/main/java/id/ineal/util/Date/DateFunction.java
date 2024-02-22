package id.ineal.util.Date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

public class DateFunction extends _Date {

    static Object getCurrent(String pattern,String mode) {
        if(mode != null) {
            if(mode.equalsIgnoreCase("long")) {
                return System.currentTimeMillis();
            } else if(mode.equalsIgnoreCase("date")) {
                return new Date(System.currentTimeMillis());
            }
        }
        Date date = new Date(System.currentTimeMillis());
        if(pattern != null) {
            sdf = new SimpleDateFormat(pattern);
        } else {
            sdf = new SimpleDateFormat(FULL_PATTERN);
        }
        return sdf.format(date);
    }

    static Object calculateCalendar(String type,int num,Date periode,String pattern,String out) {
        Calendar calendar = Calendar.getInstance();
        if(periode == null) {
            calendar.setTime(new Date(System.currentTimeMillis()));
        } else {
            calendar.setTime(periode);
        }

        if(type.equalsIgnoreCase("day")) {
            calendar.add(Calendar.DATE, num);
        } else if(type.equalsIgnoreCase("month")) {
            calendar.add(Calendar.MONTH, num);
        } else if(type.equalsIgnoreCase("hour")) {
            calendar.add(Calendar.HOUR, num);
        } else if(type.equalsIgnoreCase("minutes")) {
            calendar.add(Calendar.MINUTE, num);
        }
        if(out != null) {
            if(out.equalsIgnoreCase("long")) {
                return calendar.getTimeInMillis();
            } else if(out.equalsIgnoreCase("date")) {
                return calendar.getTime();
            }
        }
        date = calendar.getTime();
        pattern = Optional.ofNullable(pattern)
                .orElse(FULL_PATTERN);

        sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    static Object getEod(Date periode,String pattern,String out) {
        Calendar calendar = Calendar.getInstance();
        if(periode == null) {
            calendar.setTime(new Date(System.currentTimeMillis()));
        } else {
            calendar.setTime(periode);
        }
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);

        if(out != null) {
            if(out.equalsIgnoreCase("long")) {
                return calendar.getTimeInMillis();
            } else if(out.equalsIgnoreCase("date")) {
                return calendar.getTime();
            }
        }
        date = calendar.getTime();
        pattern = Optional.ofNullable(pattern)
                .orElse(FULL_PATTERN);

        sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

}
