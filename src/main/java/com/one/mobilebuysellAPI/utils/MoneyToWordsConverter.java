package com.one.mobilebuysellAPI.utils;

public class MoneyToWordsConverter {

    private static final String[] units = {
            "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
            "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
            "Seventeen", "Eighteen", "Nineteen"
    };

    private static final String[] tens = {
            "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
    };

    public static String convert(double amount) {
        int rupees = (int) amount;
        int paise = (int) Math.round((amount - rupees) * 100);

        StringBuilder result = new StringBuilder();

        if (rupees > 0) {
            result.append(convertNumber(rupees)).append(" Rupees");
        } else {
            result.append("Zero Rupees");
        }

        if (paise > 0) {
            result.append(" and ").append(convertNumber(paise)).append(" Paise");
        }

        return result.toString().trim();
    }

    private static String convertNumber(int number) {
        if (number == 0) return "Zero";

        StringBuilder result = new StringBuilder();

        if (number >= 10000000) {
            result.append(convertBelowThousand(number / 10000000)).append(" Crore ");
            number %= 10000000;
        }
        if (number >= 100000) {
            result.append(convertBelowThousand(number / 100000)).append(" Lakh ");
            number %= 100000;
        }
        if (number >= 1000) {
            result.append(convertBelowThousand(number / 1000)).append(" Thousand ");
            number %= 1000;
        }
        if (number > 0) {
            result.append(convertBelowThousand(number));
        }

        return result.toString().trim();
    }

    private static String convertBelowThousand(int number) {
        StringBuilder result = new StringBuilder();

        if (number >= 100) {
            result.append(units[number / 100]).append(" Hundred ");
            number %= 100;
        }
        if (number >= 20) {
            result.append(tens[number / 10]).append(" ");
            number %= 10;
        }
        if (number > 0) {
            result.append(units[number]).append(" ");
        }

        return result.toString();
    }
}