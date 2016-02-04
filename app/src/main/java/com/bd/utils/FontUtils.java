package com.bd.utils;

import android.graphics.Typeface;
import android.widget.TextView;

public class FontUtils {

    public static void setFont(TextView textView, Font font) {
        Typeface typeface = Typeface.createFromAsset(textView.getContext().getAssets(), font.toString());
        textView.setTypeface(typeface);
    }


    public enum Font {
        ROBOTO_MEDIUM, ROBOTO_LIGHT;

        @Override
        public String toString() {
            switch (this) {
                case ROBOTO_LIGHT:
                    return "fonts/Roboto-Light.ttf";
                case ROBOTO_MEDIUM:
                    return "fonts/Roboto-Medium.ttf";
            }
            return "fonts/Roboto-Medium.ttf";
        }
    }
}
