package id.ineal.util.Date;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class _Date {

    protected static DateFormat sdf;
    protected static String FULL_PATTERN = "yyyy-MM-dd HH:mm:ss a";
    protected static String ALT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    protected static String PATTERN = "yyyy-MM-dd";
    protected static Date date;

    /*=======================
    *       GET CURRENT
    *=========================*/
    public static String getCurrent() {
        return (String)DateFunction.getCurrent(null,null);
    }
    
    public static String getCurrent(String pattern) {
        return (String)DateFunction.getCurrent(pattern,null);
    }

    public static <T> T getCurrent(Class<T> ret) {
        if(ret.isAssignableFrom(Long.class)) { 
            return ret.cast(DateFunction.getCurrent(null,"long"));
        } else if (ret.isAssignableFrom(Date.class)) {
            return ret.cast(DateFunction.getCurrent(null,"date"));
        }
        return null;
    }
     /*=======================
    *       SET OBJECT
    *=========================*/

    public static String set(Object periode,Integer style) {
        if(periode != null && style != null) {
            date = parseToDate(periode);
            return DateTemplate.set(date,style);
        }
        return null;
    }

    public static String setMonth(String month) {
        return (month != null) ? DateTemplate.setMonth(month) : null;
    }

    public static String pretty(Object periode) {
        if(periode != null) {
            date = parseToDate(periode);
            return DateTemplate.timeAgo(date);
        }
        return null;
    }

    public static Map<String,String> toMap(Object target) {
        Map<String,String> res = new LinkedHashMap<String,String>(){{
            put("day",null);
            put("month",null);
            put("year",null);
            put("hour",null);
            put("minutes",null);
            put("second",null);
            put("form",null);
        }};

        String date = parseToString(target,FULL_PATTERN);

        List<String> times  = Arrays.asList(date.trim().split(" "));
        if(times.get(0) != null) {
            String[] t = times.get(0).split("\\-");
            if(t[0] != null) res.put("year", t[0]);
            if(t[1] != null) res.put("month", t[1]);
            if(t[2] != null) res.put("day", t[2]);
        }
        if(times.size() > 1 && times.get(1) != null) {
            String[] h = times.get(1).split("\\:");
            if(h[0] != null) res.put("hour", h[0]);
            if(h[1] != null) res.put("minutes", h[1]);
            if(h[2] != null) res.put("second", h[2]);
        }
        if(times.size() > 2 && times.get(2) != null) {
            res.put("form",times.get(2)); // AM|PM
        }
        return res;
    }


    /*=======================
            ADD DAY
    =========================*/
    /**
     * @param total_day : integer jumlah hari
     * @return String
     */
    public static String addDay(int total_day) {
        return (String)DateFunction.calculateCalendar("day",total_day,null,null,null);
    }

    public static String addDay(int total_day,String pattern) {
        return (String)DateFunction.calculateCalendar("day",total_day,null,pattern,null);
    }
    
    public static <T> T addDay(int total_day,Class<T> ret) {
        if(ret.isAssignableFrom(Long.class)) { 
            return ret.cast(DateFunction.calculateCalendar("day",total_day,null,null,"long"));
        } else if (ret.isAssignableFrom(Date.class)) {
            return ret.cast(DateFunction.calculateCalendar("day",total_day,null,null,"date"));
        }
        return null;
    }
    
    /*----- DAY FROM -----*/
    public static String addDayFrom(Date date,int total_day) {
        return (String)DateFunction.calculateCalendar("day",total_day,date,null,null);
    }
    
    public static String addDayFrom(Date date,int total_day,String pattern) {
        return (String)DateFunction.calculateCalendar("day",total_day,date,pattern,null);
    }

    public static <T> T addDayFrom(Date date,int total_day,Class<T> ret) {
        if(ret.isAssignableFrom(Long.class)) { 
            return ret.cast(DateFunction.calculateCalendar("day",total_day,null,null,"long"));
        } else if (ret.isAssignableFrom(Date.class)) {
            return ret.cast(DateFunction.calculateCalendar("day",total_day,null,null,"date"));
        }
        return null;
    }
    /*=======================
            ADD MONTH
    =========================*/
    public static String addMonth(int total_month) {
        return (String)DateFunction.calculateCalendar("month",total_month,null,null,null);
    }

    public static String addMonth(int total_month,String pattern) {
        return (String)DateFunction.calculateCalendar("month",total_month,null,pattern,null);
    }

    public static <T> T addMonth(int total_month,Class<T> ret) {
         if(ret.isAssignableFrom(Long.class)) { 
            return ret.cast(DateFunction.calculateCalendar("month",total_month,null,null,"long"));
        } else if (ret.isAssignableFrom(Date.class)) {
            return ret.cast(DateFunction.calculateCalendar("month",total_month,null,null,"date"));
        }
        return null;
    }

     /*----- MONTH FROM -----*/
    public static String addMonthFrom(Object date,int total_month) {
        Date val = (date instanceof String) ? parseToDate(date.toString(),null) : parseToDate(date);
        return (String)DateFunction.calculateCalendar("month",total_month,val,null,null);
    }

    public static String addMonthFrom(Object date,int total_month,String pattern) {
        Date val = (date instanceof String) ? parseToDate(date.toString(),null) : parseToDate(date);
        return (String)DateFunction.calculateCalendar("month",total_month,val,pattern,null);
    }

    public static <T> T addMonthFrom(Date date,int total_month,Class<T> ret) {
        if(ret.isAssignableFrom(Long.class)) { 
            return ret.cast(DateFunction.calculateCalendar("month",total_month,date,null,"long"));
        } else if (ret.isAssignableFrom(Date.class)) {
            return ret.cast(DateFunction.calculateCalendar("month",total_month,date,null,"date"));
        }
        return null;
    }
    /*=======================
            ADD HOUR
    =========================*/
    public static String addHour(int total_hour) {
        return (String)DateFunction.calculateCalendar("hour",total_hour,null,null,null);
    }

    public static String addHour(int total_hour,String pattern) {
        return (String)DateFunction.calculateCalendar("hour",total_hour,null,pattern,null);
    }

    public static <T> T addHour(int total_hour,Class<T> ret) {
        Date val = parseToDate(date);
        if(ret.isAssignableFrom(Long.class)) { 
            return ret.cast(DateFunction.calculateCalendar("hour",total_hour,val,null,"long"));
        } else if (ret.isAssignableFrom(Date.class)) {
            return ret.cast(DateFunction.calculateCalendar("hour",total_hour,val,null,"date"));
        }
        return null;
    }

    /*----- HOUR FROM -----*/
    public static String addHourFrom(Object date,int total_hour) {
        Date val = (date instanceof String) ? parseToDate(date.toString(),null) : parseToDate(date);
        return (String)DateFunction.calculateCalendar("hour",total_hour,val,null,null);
    }

    public static String addHourFrom(Object date,int total_hour,String pattern) {
        Date val = (date instanceof String) ? parseToDate(date.toString(),null) : parseToDate(date);
        return (String)DateFunction.calculateCalendar("hour",total_hour,val,pattern,null);
    }

    public static <T> T addHourFrom(Object date,int total_hour,Class<T> ret) {
        Date val = (date instanceof String) ? parseToDate(date.toString(),null) : parseToDate(date);
        if(ret.isAssignableFrom(Long.class)) { 
            return ret.cast(DateFunction.calculateCalendar("hour",total_hour,val,null,"long"));
        } else if (ret.isAssignableFrom(Date.class)) {
            return ret.cast(DateFunction.calculateCalendar("hour",total_hour,val,null,"date"));
        }
        return null;
    }

    /*=======================
        ADD MINUTES
    =========================*/
    public static String addMinutes(int minutes) {
        return (String)DateFunction.calculateCalendar("minutes",minutes,null,null,null);
    }

    public static String addMinutes(int minutes,String pattern) {
        return (String)DateFunction.calculateCalendar("minutes",minutes,null,pattern,null);
    }

    public static <T> T addMinutes(int total_minutes,Class<T> ret) {
        Date val = parseToDate(date);
        if(ret.isAssignableFrom(Long.class)) { 
            return ret.cast(DateFunction.calculateCalendar("minutes",total_minutes,val,null,"long"));
        } else if (ret.isAssignableFrom(Date.class)) {
            return ret.cast(DateFunction.calculateCalendar("minutes",total_minutes,val,null,"date"));
        }
        return null;
    }

    /*----- MINUTES FROM -----*/
    public static String addMinutesFrom(Object date,int total_minutes) {
        Date val = (date instanceof String) ? parseToDate(date.toString(),null) : parseToDate(date);
        return (String)DateFunction.calculateCalendar("minutes",total_minutes,val,null,null);
    }

    public static String addMinutesFrom(Object date,int total_minutes,String pattern) {
        Date val = (date instanceof String) ? parseToDate(date.toString(),null) : parseToDate(date);
        return (String)DateFunction.calculateCalendar("minutes",total_minutes,val,pattern,null);
    }

    public static <T> T addMinutesFrom(Object date,int total_minutes,Class<T> ret) {
        Date val = (date instanceof String) ? parseToDate(date.toString(),null) : parseToDate(date);
        if(ret.isAssignableFrom(Long.class)) { 
            return ret.cast(DateFunction.calculateCalendar("minutes",total_minutes,val,null,"long"));
        } else if (ret.isAssignableFrom(Date.class)) {
            return ret.cast(DateFunction.calculateCalendar("minutes",total_minutes,val,null,"date"));
        }
        return null;
    }

    /*=======================
        EOD (Tanggal akhir bulan)
    =========================*/
    public static String getEod() {
        return (String)DateFunction.getEod(null,null,null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getEod(Object obj) {
        if(obj instanceof String) {
             return (T)(String)DateFunction.getEod(null,obj.toString(),null);
        } else {
            if(obj instanceof Class<?>) {
                Class<?> type = (Class<?>) obj;
                if(type.isAssignableFrom(Long.class)) {
                    return (T)DateFunction.getEod(null,null,"long");
                } else if(type.isAssignableFrom(Date.class)) {
                    return (T)DateFunction.getEod(null,null,"date");
                }
            }
        }
        return null;
    }

    public static String getEodFrom(Object date) {
        Date val = (date instanceof String) ? parseToDate(date.toString(),null) : parseToDate(date);
        return (String)DateFunction.getEod(val,null,null);
    }

    public static String getEodFrom(Object date,String pattern) {
        Date val = (date instanceof String) ? parseToDate(date.toString(),null) : parseToDate(date);
        return (String)DateFunction.getEod(val,pattern,null);
    }
    
    public static <T> T getEodFrom(Object date,Class<T> ret) {
        Date val = (date instanceof String) ? parseToDate(date.toString(),null) : parseToDate(date);
        if(ret.isAssignableFrom(Long.class)) {
            return ret.cast(DateFunction.getEod(val,null,"long"));
        } else if(ret.isAssignableFrom(Date.class)) {
            return ret.cast(DateFunction.getEod(val,null,"date"));
        }
        return null;
    }
    /*=======================
        DiffTime
    =========================*/
    public static String getDiffTime(Object start,Object end) {
        Date x = parseToDate(start);
        Date y = parseToDate(end);
        
        long start_time = x.getTime();
        long end_time = y.getTime();

        long time = end_time - start_time;
        long s = time /1000 % 60;
        long m = time / (60 * 1000) % 60;
        long h = time / (60 * 60 * 1000) % 24;
        return String.format("%02d:%02d:%02d", h,m,s);
    }

    /*====================
     *      PARSER
     *=====================*/
    public static String parseToString(Object target,String pattern) {
        date = parseToDate(target);
        if(pattern == null) {
            pattern = FULL_PATTERN;
        }
        sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static Date parseToDate(Object target) {
        if(target instanceof Long) {
            date = new Date(Long.parseLong(target.toString()));
        } else if(target instanceof  Timestamp) {
            date=  new Date(((Timestamp) target).getTime());
        } else if(target instanceof LocalDateTime) {
            date = Date.from(((LocalDateTime) target).atZone(ZoneId.systemDefault()).toInstant());
        } else if (target instanceof LocalDate) {
            date = Date.from(((LocalDate) target).atStartOfDay(ZoneId.systemDefault()).toInstant());
        } else if(target instanceof Instant) {
            date = Date.from((Instant) target);
        } else if(target instanceof Double) {
            Double d = (Double) target;
            date = new Date(Long.parseLong(new BigDecimal(d).toString()) * 1000L);
        } else if(target instanceof Integer) {
            date = new Date(Long.parseLong(new BigDecimal(target.toString()).toString()) * 1000L);
        } else {
            date = (Date)target;
        }
        return date;
    }

    public static Date parseToDate(String target,String pattern) {
        if(pattern == null) {
            try{
                sdf = new SimpleDateFormat(ALT_PATTERN);
                date = sdf.parse(target);  
            } catch(ParseException e) {
                try {
                    sdf = new SimpleDateFormat(PATTERN);
                    date = sdf.parse(target);
                } catch (ParseException x) {
                    try {
                        sdf = new SimpleDateFormat(FULL_PATTERN);
                        date = sdf.parse(target);
                    } catch (ParseException o) {
                        throw new RuntimeException("Gagal Parsing@@");
                    }
                }  
            }
        } else {
            try {
                sdf = new SimpleDateFormat(pattern);
                date = sdf.parse(target);
            } catch (ParseException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return date;
    }


    /*===========================
    *			BETWEEN
    *
    *       getYearBetween("2023",5);
    *       getYearBetween(2023,5);
    *============================*/
    public static List<String> getYearBetween(Object currentYear,int count) {
        
        int cy = Integer.parseInt(currentYear.toString());
        int maxY = 0;
        int minY = 0;

        if(count < 0) {
            minY = cy+count;
            maxY = cy;
        } else {
            minY = cy;
            maxY = cy+count;
        }
        List<String> years = new LinkedList<>();
        for (int i = minY; i <= maxY; i++) {
            years.add(String.valueOf(i));
        }
        return years;  
    }


}
