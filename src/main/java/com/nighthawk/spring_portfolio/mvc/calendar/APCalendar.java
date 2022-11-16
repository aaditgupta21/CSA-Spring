package com.nighthawk.spring_portfolio.mvc.calendar;
import java.util.*;

import java.util.HashMap;
// Prototype Implementation

public class APCalendar {

    /**
     * Returns true if year is a leap year and false otherwise.
     * isLeapYear(2019) returns False
     * isLeapYear(2016) returns True
     */
    public static boolean isLeapYear(int year) {
        // implementation not shown
        if (year % 4 == 0 && year % 100 != 0) {
            return true;
        } else if (year % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the value representing the day of the week
     * 0 denotes Sunday,
     * 1 denotes Monday, ...,
     * 6 denotes Saturday.
     * firstDayOfYear(2019) returns 2 for Tuesday.
     */
    private static int firstDayOfYear(int year) {
        // implementation not shown
        Date currentDate = new Date(year-1900, 0, 1);

        return currentDate.getDay();
    }

    /**
     * Returns n, where month, day, and year specify the nth day of the year.
     * This method accounts for whether year is a leap year.
     * dayOfYear(1, 1, 2019) return 1
     * dayOfYear(3, 1, 2017) returns 60, since 2017 is not a leap year
     * dayOfYear(3, 1, 2016) returns 61, since 2016 is a leap year.
     */
    private static int dayOfYear(int month, int day, int year) {
        int num = 0;
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(1, 0);
        map.put(2, 31);
        map.put(3, 59);
        map.put(4, 90);
        map.put(5, 120);
        map.put(6, 151);
        map.put(7, 181);
        map.put(8, 212);
        map.put(9, 243);
        map.put(10, 273);
        map.put(11, 304);
        map.put(12, 334);
        if (isLeapYear(year) && month > 2) {
            num = map.get(month) + day + 1;

        } else {
            num = map.get(month) + day;
        }

        return num;
    }

    /**
     * Returns the number of leap years between year1 and year2, inclusive.
     * Precondition: 0 <= year1 <= year2
     */
    public static int numberOfLeapYears(int year1, int year2) {
        // to be implemented in part (a)
        int count = 0;
        for (int i = year1; i <= year2; i++) {
            if (isLeapYear(i)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the value representing the day of the week for the given date
     * Precondition: The date represented by month, day, year is a valid date.
     */
    public static int dayOfWeek(int month, int day, int year) {
        // to be implemented in part (b)
        Date today = new Date(year-1900, month-1, day);

        return today.getDay();
    }

    /** Tester method */
    public static void main(String[] args) {
        // Private access modifiers
        System.out.println("firstDayOfYear: " + APCalendar.firstDayOfYear(0001));
        System.out.println("dayOfYear: " + APCalendar.dayOfYear(3, 1, 2017));

        // Public access modifiers
        System.out.println("isLeapYear: " + APCalendar.isLeapYear(2022));
        System.out.println("numberOfLeapYears: " + APCalendar.numberOfLeapYears(2000, 2022));
        System.out.println("dayOfWeek: " + APCalendar.dayOfWeek(1, 1, 2022));
    }

}