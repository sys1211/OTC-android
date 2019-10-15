package com.ubfx.library.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.TextUtils;


import com.ubfx.library.R;
import com.ubfx.theme.ThemeMgr;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chuanzheyang on 17/1/20.
 */

public class StringUtils {

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static boolean regexMatch(String str, String patternStr) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    public static String getMoneyFormat(String money) {
        try {
            double m = Double.valueOf(money);
            return getMoneyFormat(m);
        } catch (Exception e) {
            return money;
        }
    }

    /**
     * 金额格式化 每三位加, 保留两位小数
     *
     * @param money
     * @return
     */
    public static String getMoneyFormat(double money) {
        if (money == 0) {
            return "0.00";
        }
        DecimalFormat df = new DecimalFormat(",##0.00");
        return df.format(money);
    }

    /**
     * 金额格式化 每三位加, 不要小数
     *
     * @param money
     * @return
     */
    public static String getMoneyFormat3(double money) {
        if (money == 0) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat(",###");
        return df.format(money);
    }

    public static String getMoneyFormat2(double money) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(money);
    }

    public static String getMoneyString(String money, int precision) {
        if (TextUtils.isEmpty(money)) {
            return getMoneyString(0, precision);
        }
        try {
            double m = Double.valueOf(money);
            return getMoneyString(m, precision);
        } catch (Exception e) {
            return money;
        }
    }

    public static String getMoneyString(String money, int precision, boolean positiveSymbol) {
        if (TextUtils.isEmpty(money)) {
            return getMoneyString(0, precision);
        }
        try {
            double m = Double.valueOf(money);
            return (m > 0 && positiveSymbol ? "+" : "") + getMoneyString(m, precision);
        } catch (Exception e) {
            return money;
        }
    }

    public static String getMoneyString(double money, int precision) {
        return getMoneyStringWithLocale(money, precision, Locale.US);
    }

    public static String getMoneyString(double money, int precision, boolean positiveSymbol) {
        String result = getMoneyString(money, precision);
        return (money > 0 && positiveSymbol) ? ("+" + result) : result;
    }

    public static String getMoneyStringWithLocale(double money, int precision, Locale locale) {
        String currencySymbol = DecimalFormatSymbols.getInstance(locale).getCurrencySymbol();
        String fmt = currencySymbol + (precision > 0 ? "#0." : "#0");
        for (int idx = 0; idx < precision; idx++) {
            fmt += "0";
        }

        DecimalFormat df = new DecimalFormat(fmt);

        return df.format(money);
    }

    public static double parseMoneyString(String money) {
        return parseMoneyString(money, Locale.US);
    }

    public static double parseMoneyString(String money, Locale locale) {
        String moneyStr = money.replace("+", ""); // remove extra characters.
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        try {
            return fmt.parse(moneyStr).doubleValue();
        } catch (ParseException e) {
            return 0;
        }
    }

    public static double toDouble(String money) {
        if (money == null || money.length() == 0) {
            return 0.0;
        }
        try {
            return Double.valueOf(money);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public static Integer toInt(@NonNull String intStr) {
        Integer result = null;
        if (intStr.equals("")) {
            return null;
        }
        try {
            result = Integer.valueOf(intStr);
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    public static String getDecimalString(double value, int precision) {
        NumberFormat df = NumberFormat.getInstance();
        df.setMaximumFractionDigits(precision);
        df.setMinimumFractionDigits(precision);
        df.setGroupingUsed(false);
        return df.format(value);
    }

    public static String getDecimalString(String value, int precision) {
        float v;
        try {
            v = Float.valueOf(value);
        } catch (Exception e) {
            return value;
        }
        return getDecimalString(v, precision);
    }

    public static String getPercentString(double value, int precision) {
        NumberFormat df = NumberFormat.getPercentInstance();
        df.setMaximumFractionDigits(precision);
        df.setMinimumFractionDigits(precision);
        df.setGroupingUsed(false);
        return df.format(value);
    }

    private static Date formatDate = new Date();
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm");
    private static SimpleDateFormat format1 = new SimpleDateFormat("MM-dd HH:mm");
    private static SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat format4 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static SimpleDateFormat format5 = new SimpleDateFormat("yyyy.MM.dd");

    /**
     * yyyy.MM.dd HH:mm
     *
     * @param timeInMilliseconds
     * @return
     */
    public static String getDateWithFormat(long timeInMilliseconds) {
        formatDate.setTime(timeInMilliseconds);
        return format.format(formatDate);
    }

    /**
     * yyyy.MM.dd
     *
     * @param timeInMilliseconds
     * @return
     */
    public static String getDateWithFormat1(long timeInMilliseconds) {
        formatDate.setTime(timeInMilliseconds);
        return format5.format(formatDate);
    }

    /**
     * MM-dd HH:mm
     *
     * @param timeInMilliseconds
     * @return
     */
    public static String getDateWithFormat2(long timeInMilliseconds) {
        return getDateWithFormat2(timeInMilliseconds, false);
    }

    /**
     * MM-dd HH:mm
     *
     * @param timeInMilliseconds
     * @param utc
     * @return
     */
    public static String getDateWithFormat2(long timeInMilliseconds, boolean utc) {
        formatDate.setTime(timeInMilliseconds);
        if (utc) {
            format1.setTimeZone(TimeZone.getTimeZone("UTC"));
        } else {
            format1.setTimeZone(TimeZone.getDefault());
        }
        return format1.format(formatDate);
    }

    /**
     * yyyy-MM-dd
     *
     * @param timeInMilliseconds
     * @return
     */
    public static String getDateWithFormat3(long timeInMilliseconds) {
        return getDateWithFormat3(timeInMilliseconds, false);
    }

    /**
     * yyyy-MM-dd
     *
     * @param timeInMilliseconds
     * @param utc
     * @return
     */
    public static String getDateWithFormat3(long timeInMilliseconds, boolean utc) {
        formatDate.setTime(timeInMilliseconds);
        if (utc) {
            format2.setTimeZone(TimeZone.getTimeZone("UTC"));
        } else {
            format2.setTimeZone(TimeZone.getDefault());
        }
        return format2.format(formatDate);
    }

    /**
     * yyyy-MM-dd HH:mm
     *
     * @param timeInMilliseconds
     * @return
     */
    public static String getDateWithFormat5(long timeInMilliseconds) {
        return getDateWithFormat5(timeInMilliseconds, false);
    }

    /**
     * yyyy-MM-dd HH:mm
     *
     * @param timeInMilliseconds
     * @param utc
     * @return
     */
    public static String getDateWithFormat5(long timeInMilliseconds, boolean utc) {
        formatDate.setTime(timeInMilliseconds);
        if (utc) {
            format4.setTimeZone(TimeZone.getTimeZone("UTC"));
        } else {
            format4.setTimeZone(TimeZone.getDefault());
        }
        return format4.format(formatDate);
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param timeInMilliseconds
     * @return
     */
    public static String getDateWithFormat4(long timeInMilliseconds) {
        return getDateWithFormat4(timeInMilliseconds, false);
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param timeInMilliseconds
     * @param utc
     * @return
     */
    public static String getDateWithFormat4(long timeInMilliseconds, boolean utc) {
        formatDate.setTime(timeInMilliseconds);
        if (utc) {
            format3.setTimeZone(TimeZone.getTimeZone("UTC"));
        } else {
            format3.setTimeZone(TimeZone.getDefault());
        }
        return format3.format(formatDate);
    }


    public static String getDateWithFormatEx(long timeInMilliseconds, String format) {
        return getDateWithFormatEx(timeInMilliseconds, format, false);
    }

    public static String getDateWithFormatEx(long timeInMilliseconds, String format, boolean utc) {
        formatDate.setTime(timeInMilliseconds);
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        if (utc) {
            fmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        } else {
            fmt.setTimeZone(TimeZone.getDefault());
        }
        return fmt.format(formatDate);
    }

    public static long getTime(String timeString) {
        return getTime(timeString, "yyyyMMdd");
    }

    public static long getTime(String timeString, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        Date d;
        long l = 0;
        try {
            d = sdf.parse(timeString);
            l = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return l;
    }

    public static String hiddenStr(String src, int beginIndex, int endIndex) {
        if (TextUtils.isEmpty(src)) {
            return src;
        }
        if (endIndex - beginIndex < 0) {
            return src;
        }
        int hiddenLength = endIndex - beginIndex;

        String hiddenStrStart = src.substring(0, beginIndex);
        String hiddenStrEnd = src.substring(endIndex);

        String hiddenPlaceHolder = "";
        for (int i = 0; i < hiddenLength; i++) {
            hiddenPlaceHolder += "*";
        }

        return hiddenStrStart + hiddenPlaceHolder + hiddenStrEnd;
    }

    /**
     * example  input  allen  output  a****
     *
     * @param src
     * @return
     */
    public static String nameHidden(String src) {
        if (src.length() < 2) {
            return src;
        }
        return hiddenStr(src, 1, src.length());
    }

    public static String nameHiddenCenter(String src) {
        if (src.length() < 2) {
            return src;
        }
        return hiddenStr(src, Math.max(1, src.length() / 4), Math.max(2, src.length() * 3 / 4));
    }

    public static float getTextWidth(String text, float textSize) {
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(textSize);
        return textPaint.measureText(text);
    }

    public static String fitWidth(String src, float width, float textSize) {
        float stringWidth = getTextWidth(src, textSize);
        float breakSize = getTextWidth("...", textSize);
        if ((stringWidth + breakSize) > width) {
            src = src.substring(0, src.length() - 1);
            src = fitWidth(src, width, textSize);
        }
        return src;
    }

    public static String phoneHide(String phone_number) {
        if (phone_number.length() > 8) {
            return hiddenStr(phone_number, phone_number.length() - 8, phone_number.length() - 4);
        }
        return "";
    }

    public static String bankHidden(@NonNull String bank_number) {
        String result = bank_number;
        if (result.length() > 8) {
            int beginIdx = 4;
            int endIdx = result.length() - 4;
            String resultBefore = result.substring(0, beginIdx);
            String resultAfter = result.substring(endIdx);
            result = resultBefore + " **** **** " + resultAfter;
        }

        return result;
    }


    public static String htmlStyleWrapper(String src) {
        String textSizeScript = "<script >!function(c,b){var f=b.documentElement||1,d=3.75;function a(){var g=f.clientWidth/(d);f.style.fontSize=g+'px'}if(function c(){b.body?b.body.style.fontSize='16px':b.addEventListener('DOMContentLoaded',c)}(),a()){}}(window,document);</script>";

        String meta = "<meta name=\"viewport\" content=\"initial-scale=1, maximum-scale=1, minimum-scale=1, width=device-width, user-scalable=no\"></meta>";

        String stylebody = "html,body{padding:0;margin:0;}*{box-sizing: border-box;}ul,li{list-style:none;padding:0;margin:0;}";

        String styleImg = "img{width: 100% !important;height: auto !important;}";
        String content = src;
        String style = "table td{border:1px solid #666;} table{width:100%}";
        String pStyle = "p{width: 100% !important;padding: 0;margin: 0;text-align: left;line-height: 1.8 !important;background-color: transparent !important;}";
        String fontColorStyle = "";
        @ColorInt int fontColor = ThemeMgr.get().getThemeProvider().fontMain();
        if (fontColor != 0) {
            String colorHex = hexFrom(fontColor);
            fontColorStyle = "style=\"color:" + colorHex + "\"";
        }
        content = "<html> <head> " + meta + textSizeScript + " <style>" + stylebody + style + styleImg + pStyle + "</style></head> <body " + fontColorStyle + " >" + content + "</body></html>";
        return content;
    }

    public static String hexFrom(@ColorInt int color) {
        return String.format("#%02X%02X%02X", Color.red(color), Color.green(color), Color.blue(color));
    }

    public static String getFriendlyDateStr(Context context, long timestamp) {
        String strDate = StringUtils.getDateWithFormat3(timestamp);
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String nowStr = format.format(now);
        if (nowStr.equals(strDate)) {
            return context.getApplicationContext().getResources().getString(R.string.lib_today) + " " + StringUtils.getDateWithFormatEx(timestamp, "HH:mm");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        String yesterdayStr = format.format(calendar.getTime());
        if (yesterdayStr.equals(strDate)) {
            return context.getApplicationContext().getResources().getString(R.string.lib_yesterday) + " " + StringUtils.getDateWithFormatEx(timestamp, "HH:mm");
        }

        return StringUtils.getDateWithFormat5(timestamp);
    }
}
