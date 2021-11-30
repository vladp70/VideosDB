package entertainment;

import actor.Actor;

import java.util.ArrayList;

public abstract class Video implements Comparable<Video> {
    private String title;
    private int year;
    private ArrayList<Genre> genres;
    private Double rating = 0.0;
    private int numOfRatings = 0;
    private ArrayList<Actor> cast;
    private int duration = 0;
    private int views = 0;
    private int numOfFavorites = 0;

    public Video(final String title, final int year, final ArrayList<Genre> genres) {
        this.title = title;
        this.year = year;
        this.genres = genres;
        cast = new ArrayList<>();
    }

    public Video(final String title, final int year, final ArrayList<Genre> genres,
                 final ArrayList<Actor> cast) {
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.cast = cast;
    }

    public Video(final String title) {
        this.title = title;
        this.year = -1;
        this.genres = new ArrayList<>();
        this.rating = -1.0;
        this.cast = new ArrayList<>();
        this.duration = -1;
    }

    public ArrayList<Actor> getCast() {
        return cast;
    }

    public void setCast(final ArrayList<Actor> cast) {
        this.cast = cast;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(final Double rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public int getNumOfRatings() {
        return numOfRatings;
    }

    public void setNumOfRatings(final int numOfRatings) {
        this.numOfRatings = numOfRatings;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public int getViews() {
        return views;
    }

    public void incrementViews() {
        views++;
    }

    public int getNumOfFavorites() {
        return numOfFavorites;
    }

    public void incrementNumOfFavorites() {
        numOfFavorites++;
    }

    @Override
    public int compareTo(final Video o) {
        if (this.getRating() > o.getRating()) {
            return 1;
        } else if (this.getRating() < o.getRating()) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return this.title;
    }

    public abstract int addRating(Double newRating, int season);
}
