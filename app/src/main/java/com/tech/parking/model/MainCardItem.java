package com.tech.parking.model;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

public class MainCardItem implements CardItem {
    private final String title;
    private final Drawable icon;
    private final int numberOfNews;
    private Fragment fragment;

    private MainCardItem(Drawable icon, String title, int numberOfNews, Fragment fragment) {
        this.title = title;
        this.icon = icon;
        this.numberOfNews = numberOfNews;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public Drawable getDrawable() {
        return icon;
    }

    public int getCounts() {
        return numberOfNews;
    }

    public Fragment getFragment() {
        return fragment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MainCardItem that = (MainCardItem) o;

        if (numberOfNews != that.numberOfNews) return false;
        if (!title.equals(that.title)) return false;
        if (!icon.equals(that.icon)) return false;
        return fragment.equals(that.fragment);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + icon.hashCode();
        result = 31 * result + numberOfNews;
        result = 31 * result + fragment.hashCode();
        return result;
    }

    public static class Builder {
        private String title;
        private Drawable icon;
        private int counts = -1;
        private Fragment fragment;

        public static Builder makeInstance() {
            return new Builder();
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withIcon(Drawable icon) {
            this.icon = icon;
            return this;
        }

        public Builder withCounts(int numberOfNews) {
            this.counts = numberOfNews;
            return this;
        }

        public Builder withFragment(Fragment fragment) {
            this.fragment = fragment;
            return this;
        }

        public MainCardItem build() {
            return new MainCardItem(icon, title, counts, fragment);
        }
    }
}