
package com.nighthawk.spring_portfolio.mvc.calendar;

public class Day {
    private int year;
    private int month;
    private int day;
    private int dayOfYear;
    private int dayOfWeek;
    private int firstDayOfYear;

    // zero argument constructor
    public Day() {
    }

    public void setStuff(int month, int year, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.setDayOfWeek();
        this.setDayOfYear();
        this.setFirstDayOfYear();
    }

    /* dayOfYear getter/setters */
    public int getDayOfYear() {
        return APCalendar.dayOfYear(month, day, year);
    }

    private void setDayOfYear() { // this is private to avoid tampering
        this.dayOfYear = APCalendar.dayOfYear(month, day, year);
    }

    public int getFirstDayOfYear() {
        return APCalendar.firstDayOfYear(year);
    }

    private void setFirstDayOfYear() { // this is private to avoid tampering
        this.firstDayOfYear = APCalendar.firstDayOfYear(year);
    }

    /* dayOfWeek getter/setters */
    public int getDayOfWeek() {
        return APCalendar.dayOfWeek(month, day, year);
    }

    private void setDayOfWeek() { // this is private to avoid tampering
        this.dayOfWeek = APCalendar.dayOfWeek(month, day, year);
    }

    /* isLeapYearToString formatted to be mapped to JSON */
    public String jSONFormat() {
        return String.format(
                "{ \"year\": %d, \"month\": %d, \"day\": %d, \"dayOfWeek\": %d, \"dayOfYear\": %d, \"firstDayOfYear\": %d}",
                this.year,
                this.month, this.day, this.dayOfWeek, this.dayOfYear, this.firstDayOfYear);
    }

    /* standard toString placeholder until class is extended */
    public String toString() {
        return jSONFormat();
    }

    public static void main(String[] args) {
        Day dayobj = new Day();
        dayobj.setStuff(3, 2016, 1);

        System.out.println(dayobj.jSONFormat());
    }
}