package com.bd.utils;

import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;

public class SpannableUtils {
    public static void color(@NonNull Spannable spannableText, @NonNull String textToHighlight, int color) {
        String text = spannableText.toString();
        int begin = text.indexOf(textToHighlight);
        while (begin >= 0) {
            int end = begin + textToHighlight.length();
            spannableText.setSpan(new ForegroundColorSpan(color), begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            begin = text.indexOf(textToHighlight, begin + textToHighlight.length());
        }
    }
}
