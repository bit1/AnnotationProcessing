package com.noogler.annotationprocessing.usage;

public class Main {

    private static final String nullString = null;
    private static final String emptyString = "";
    private static final String someString = "some_string";

    public static void main(String[] args) {
        StringUtils stringUtils = StringUtilsSingleton.getInstance();
        System.out.println("nullString is null: " + stringUtils.isNullOrEmpty(nullString));
        stringUtils = StringUtilsSingleton.getInstance();
        System.out.println("emptyString is empty: " + stringUtils.isNullOrEmpty(emptyString));
        stringUtils = StringUtilsSingleton.getInstance();
        System.out.println("someString is null: " + stringUtils.isNullOrEmpty(someString));
    }

}
