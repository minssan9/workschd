package com.voyagerss.api.service;

public class PayrollService {

    public double calculatePay(double hourlyRate, int regularHours, int overtimeHours, int holidayHours) {
        double regularPay = hourlyRate * regularHours;
        double overtimePay = hourlyRate * 1.5 * overtimeHours;
        double holidayPay = hourlyRate * 2 * holidayHours;
        return regularPay + overtimePay + holidayPay;
    }
}