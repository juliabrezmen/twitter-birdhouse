package com.bd.utils;

import android.support.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    @Nullable
    public static String createShortDate1(Date tweetDate) {
        String shortDate = null;
        if (tweetDate == null) {
            return null;
        } else {
            Calendar currentCalendar = Calendar.getInstance();
            Calendar tweetCalendar = Calendar.getInstance();
            tweetCalendar.setTime(tweetDate);

            int yearDiff = getDiff(currentCalendar, tweetCalendar, Calendar.YEAR);

            if (yearDiff > 0) {
                shortDate = String.valueOf(yearDiff) + "y";
            } else {
                int monthDiff = getDiff(currentCalendar, tweetCalendar, Calendar.MONTH);
                if (monthDiff > 0) {
                    shortDate = String.valueOf(monthDiff) + "m";
                } else {
                    int dayDiff = getDiff(currentCalendar, tweetCalendar, Calendar.DAY_OF_MONTH);
                    if (dayDiff > 0) {
                        shortDate = String.valueOf(dayDiff) + "d";
                    } else {
                        int hourDiff = getDiff(currentCalendar, tweetCalendar, Calendar.HOUR_OF_DAY);
                        if (hourDiff > 0) {
                            shortDate = String.valueOf(hourDiff) + "h";
                        } else {
                            int minDiff = getDiff(currentCalendar, tweetCalendar, Calendar.MINUTE);
                            if (minDiff > 0) {
                                shortDate = String.valueOf(minDiff) + "m";
                            } else {
                                int secDiff = getDiff(currentCalendar, tweetCalendar, Calendar.SECOND);
                                if (secDiff > 0) {
                                    shortDate = String.valueOf(secDiff) + "s";
                                }
                            }
                        }
                    }
                }
            }
        }

        return shortDate;
    }

    private static int getDiff(Calendar currentCalendar, Calendar tweetCalendar, int field) {
        return currentCalendar.get(field) - tweetCalendar.get(field);
    }

    @Nullable
    public static String createShortDate(Date tweetDate) {
        String shortDate = null;
        if (tweetDate == null) {
            return null;
        } else {
            Calendar currentCalendar = Calendar.getInstance();
            Calendar tweetCalendar = Calendar.getInstance();
            tweetCalendar.setTime(tweetDate);

            long dateDiff = currentCalendar.getTimeInMillis() - tweetCalendar.getTimeInMillis();

            long secDiff = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            if (secDiff < 60) {
                shortDate = String.valueOf(secDiff) + "s";
            } else {
                long minDiff = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
                if (minDiff < 60) {
                    shortDate = String.valueOf(minDiff) + "m";
                } else {
                    long hourDiff = TimeUnit.MILLISECONDS.toHours(dateDiff);
                    if (hourDiff < 24) {
                        shortDate = String.valueOf(hourDiff) + "h";
                    } else {
                        long dayDiff = TimeUnit.MILLISECONDS.toDays(dateDiff);
                        if (dayDiff < 30) {
                            shortDate = String.valueOf(dayDiff) + "d";
                        } else {
                            if (dayDiff < 365) {
                                int monthDiff = (int) (dayDiff / 30);
                                shortDate = String.valueOf(monthDiff) + "m";
                            } else {
                                int yearDiff = (int) (dayDiff / 365);
                                shortDate = String.valueOf(yearDiff) + "y";
                            }
                        }
                    }
                }
            }

            return shortDate;
        }
    }
}
