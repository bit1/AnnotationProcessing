package com.noogler.annotationprocessing.usage;

import com.noogler.annotationprocessing.annotation.Singleton;

@Singleton
public class StringUtils {

    public boolean isNullOrEmpty(String str) {
        return (str == null) || (str.equals(""));
    }

}
