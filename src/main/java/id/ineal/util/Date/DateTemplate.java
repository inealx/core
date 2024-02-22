package id.ineal.util.Date;

import java.util.Date;
import java.util.Map;

public class DateTemplate extends _Date {

    public static String set(Object date,Integer style) {
        Map<String,String> d = toMap(date);

        String month = setMonth(d.get("month"));
        String result = "";

        switch(style) {
            case 1 :
                // 25 Juli 2021 04:43:13
                result = d.get("day")+" "+month+" "+ d.get("year")+" "+d.get("hour")+":"+d.get("minutes")+":"+d.get("second");
                break;
            case 2:
                //Juli 23, 2021
                result = d.get("day")+" "+month+" "+ d.get("year");
                break;
            // 2022-01-21 12:07:25
            case 3 :
                result = d.get("year")+"-"+d.get("month")+"-"+d.get("day")+" "+d.get("hour")+":"+d.get("minutes")+":"+d.get("second");
                break;
            //Jan 10 at 13:00 PM
            case 4 :
                result = String.format("%s %s at %s:%s %s",month,d.get("day"),d.get("hour"),d.get("minutes"),d.get("form"));
                break;
            //Jan 10,2018
            case 5 :
                result = String.format("%s %s,%s",month,d.get("day"),d.get("year"));
                break;
            // 20/05/2023
            case 6 :
                result = d.get("day")+"/"+d.get("month")+"/"+d.get("year");
            break;
             // 20/05/2023 10:30 PM
            case 7 :
                result = String.format("%s/%s/%s %s:%s %s",d.get("day"),d.get("month"),d.get("year"),d.get("hour"),d.get("minutes"),d.get("form"));
            break;
            // 10:30:35 PM
            case 8 :
                result = String.format("%s:%s:%s %s",d.get("hour"),d.get("minutes"),d.get("second"),d.get("form"));
            break;
            
        }
        return result;
    }

    public static String setMonth(String month) {
        String result = "";
        switch(month) {
            case "1":
            case "01":
                result = "Jan";
                break;
            case "2":
            case "02":
                result = "Feb";
                break;
            case "3":
            case "03":
                result = "Mar";
                break;
            case "4":
            case "04":
                result = "Apr";
                break;
            case "5":
            case "05":
                result = "May";
                break;
            case "6":
            case "06":
                result = "Jun";
                break;
            case "7":
            case "07":
                result = "Jul";
                break;
            case "8":
            case "08":
                result = "Aug";
                break;
            case "9":
            case "09":
                result = "Sep";
                break;
            case "10":
                result = "Oct";
                break;
            case "11":
                result = "Nov";
                break;
            case "12":
                result = "Dec";
                break;
        }
        return result;
    }

    public static String timeAgo(Date pastDate) {
        Date currentDate = parseToDate(System.currentTimeMillis());
        Map<String,String> inf = toMap(pastDate);

        long milliSecPerMinute = 60 * 1000; //Milliseconds Per Minute
        long milliSecPerHour   = milliSecPerMinute * 60; //Milliseconds Per Hour
        long milliSecPerDay    = milliSecPerHour * 24; //Milliseconds Per Day
        long milliSecPerMonth  = milliSecPerDay * 30; //Milliseconds Per Month
        long milliSecPerYear   = milliSecPerDay * 365; //Milliseconds Per Year

        long msExpired = currentDate.getTime() - pastDate.getTime();

        if (msExpired < 0) {
            return "In the Future";// Tanggal masa depan
        }

        //Second
        if (msExpired < milliSecPerMinute) {
            if (Math.round(msExpired / 1000) == 1) {
                return Math.round(msExpired / 1000) + " second ago";
            } else {
                return Math.round(msExpired / 1000) + " seconds ago";
            }
        }
        //Minute
        else if (msExpired < milliSecPerHour) {
            if (Math.round(msExpired / milliSecPerMinute) == 1) {
                return Math.round(msExpired / milliSecPerMinute) + " minute ago";
            } else {
                return Math.round(msExpired / milliSecPerMinute) + " minutes ago";
            }
            // Hour
        } else if (msExpired < milliSecPerDay) {
            if (Math.round(msExpired / milliSecPerHour) == 1) {
                return Math.round(msExpired / milliSecPerHour) + " hour ago";
            } else {
                return Math.round(msExpired / milliSecPerHour) + " hours ago";
            }
            // Day
        } else if (msExpired < milliSecPerMonth) {
            if (Math.round(msExpired / milliSecPerDay) == 1) {
                return String.format("Yesterday at %s:%s %s",inf.get("hour"),inf.get("minutes"),inf.get("form"));
            } else {
                String day = String.valueOf(Math.round(msExpired / milliSecPerDay));
                return String.format("%s days ago at %s:%s %s",day,inf.get("hour"),inf.get("minutes"),inf.get("form"));
            }
        }
        //Month
        else if (msExpired < milliSecPerYear) {
            if (Math.round(msExpired / milliSecPerMonth) == 1) {
                String month = String.valueOf(Math.round(msExpired / milliSecPerMonth));
                return String.format("%s month ago at %s:%s %s",month,inf.get("hour"),inf.get("minutes"),inf.get("form"));
            } else {
                return set(pastDate,4);
            }
        }
        //Year
        else {
            long years = msExpired / milliSecPerYear;
            if (years > 20) {
                return set(pastDate,1);
            } else {
                return years + " years ago at " + inf.get("hour") + ":" + inf.get("minutes") + " " + inf.get("form");
            }
        }
    }
}
