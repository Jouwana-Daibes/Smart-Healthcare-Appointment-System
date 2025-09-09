package com.smarthealthcare.appointment.smarthealthcare_appointment.model;

import java.time.DayOfWeek;

public enum DayOfWeekShortDay {
    MON("Mon", DayOfWeek.MONDAY),
    TUE("Tue", DayOfWeek.TUESDAY),
    WED("Wed", DayOfWeek.WEDNESDAY),
    THU("Thu", DayOfWeek.THURSDAY),
    FRI("Fri", DayOfWeek.FRIDAY),
    SAT("Sat", DayOfWeek.SATURDAY),
    SUN("Sun", DayOfWeek.SUNDAY);

    private final String shortName;
    private final DayOfWeek dayOfWeek;

    DayOfWeekShortDay(String shortName, DayOfWeek dayOfWeek) {
        this.shortName = shortName;
        this.dayOfWeek = dayOfWeek;
    }

    public DayOfWeek toDayOfWeek() {
        return dayOfWeek;
    }

    public static DayOfWeek fromString(String s) {
        for (DayOfWeekShortDay sd : values()) {
            if (sd.shortName.equalsIgnoreCase(s)) {
                return sd.dayOfWeek;
            }
        }
        throw new IllegalArgumentException("Invalid day: " + s);
    }
}
