package net.hodyus.libsplugin.formatter;

public class TimeFormatter {

	public static String timerFormatter(long time) {

        // Limite de cada
        int second = 60;
        int minute = 60;
        int hour = 24;
        int day = 30;
        int month = 12;
        
        
        int seconds = 0, minutes = 0, hours = 0, days = 0, months = 0, years = 0;
        
        StringBuilder builder = new StringBuilder();

        seconds = (int) (time / 1000);
        while (true) {
            if (seconds >= second) {
                seconds -= second;
                minutes++;
                continue;
            }
            if (minutes >= minute) {
                minutes -= minute;
                hours++;
                continue;
            }
            if (hours >= hour) {
                hours -= hour;
                days++;
                continue;
            }
            if (days >= day) {
                days -= day;
                months++;
                continue;
            }
            if (months >= month) {
                months -= month;
                years++;
                continue;
            }
            break;
        }
        if (years >= 2) {
            builder.append(years + " anos");
        }
        if (years == 1) {
        	builder.append(years + " ano");
        }
        if (months >= 2) {
            builder.append(" e " + months + " meses");
        }
        if (months == 1) {
            builder.append(" e " + months + " mes");
        }
        if (days == 1) {
            builder.append(" e " + days + " dia");
        }
        if (days >= 2) {
            builder.append(" e " + days + " dias");
        }
        if (hours == 1) {
            builder.append(" e " + hours + " hora");
        }
        if (hours >= 2) {
            builder.append(" e " + hours + " horas");
        }
        if (minutes == 1) {
            builder.append(" e " + minutes + " minuto");
        }
        if (minutes >= 2) {
            builder.append(" e " + minutes + " minutos");
        }
        if (seconds == 1) {
            builder.append(" e " + seconds + " segundo");
        }
        if (seconds >= 2) {
            builder.append(" e " + seconds + " segundos");
        }
        return builder.toString();
    }
	
}
