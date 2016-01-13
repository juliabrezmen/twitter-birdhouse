package com.bd.utils;

public class UrlUtils {
    public static String createOriginImageUrl(String imageUrl) {
        return imageUrl.replace("_normal", "");
    }
}
