package com.bd.utils;

import android.support.annotation.Nullable;

public class UrlUtils {

    @Nullable
    public static String createOriginImageUrl(@Nullable String imageUrl) {
        String result = null;
        if (imageUrl != null) {
            result = imageUrl.replace("_normal", "");
        }

        return result;
    }
}
