package com.nepu.metro.util;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

import com.nepu.metro.vo.Zone;

public final class Util {

    private static final String SEPARATOR = "-";

    public static String getKeyFromZones(Zone zone1, Zone zone2) {
        StringBuilder keyBuilder = new StringBuilder();
        if (zone1.compareTo(zone2) > 0) {
            keyBuilder.append(zone2.getId())
                    .append(SEPARATOR)
                    .append(zone1.getId());
        } else {
            keyBuilder.append(zone1.getId())
                    .append(SEPARATOR)
                    .append(zone2.getId());
        }
        return keyBuilder.toString();
    }

    public static String getWeeklyKeyFromDateTime(LocalDateTime dateTime) {
        int weekOfYear = dateTime.get(WeekFields.of(Locale.UK).weekOfYear());
        StringBuilder sb = new StringBuilder();
        sb.append(dateTime.getYear())
                .append(SEPARATOR)
                .append(weekOfYear);

        return sb.toString();
    }

    public static String getDailyKeyFromDateTime(LocalDateTime dateTime) {
        return dateTime.toLocalDate().toString();
    }

}
