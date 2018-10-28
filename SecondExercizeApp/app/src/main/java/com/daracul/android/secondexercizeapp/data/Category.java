package com.daracul.android.secondexercizeapp.data;

public class Category {
    private static String[] categories = new String[]{"Home",
            "Opinion",
            "World",
            "National",
            "Politics",
            "Upshot",
            "Nyregion",
            "Business",
            "Technology",
            "Science",
            "Health",
            "Sports",
            "Arts",
            "Books",
            "Movies",
            "Theater",
            "Sundayreview",
            "Fashion",
            "Tmagazine",
            "Food",
            "Travel",
            "Magazine",
            "Realestate",
            "Automobiles",
            "Obituaries",
            "Insider"};

    public static String[] getCategories() {
        return categories;
    }

    public static int getCategoryIndexByName(String category) {
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(category)) return i;
        }
        return 0;
    }
}
