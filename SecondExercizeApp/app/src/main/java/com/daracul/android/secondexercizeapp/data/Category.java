package com.daracul.android.secondexercizeapp.data;

public class Category {
    public static final int CATEGORY_ANIMAL = 3;
    public static final int CATEGORY_CRIMINAL = 1;
    public static final int CATEGORY_DARWIN = 2;
    public static final int CATEGORY_MUSIC  = 4;
    private final int id;
    private final String name;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
