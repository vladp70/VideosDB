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
        this.rating = 0.0;
        this.cast = new ArrayList<>();
        this.duration = -1;
    }

    public final ArrayList<Actor> getCast() {
        return cast;
    }

    public final void setCast(final ArrayList<Actor> cast) {
        this.cast = cast;
    }

    public final Double getRating() {
        return rating;
    }

    public final void setRating(final Double rating) {
        this.rating = rating;
    }

    public final String getTitle() {
        return title;
    }

    public final int getYear() {
        return year;
    }

    public final int getNumOfRatings() {
        return numOfRatings;
    }

    public final void setNumOfRatings(final int numOfRatings) {
        this.numOfRatings = numOfRatings;
    }

    public final ArrayList<Genre> getGenres() {
        return genres;
    }

    public final int getDuration() {
        return duration;
    }

    public final void setDuration(final int duration) {
        this.duration = duration;
    }

    public final int getViews() {
        return views;
    }

    public final void incrementViews(final int value) {
        views += value;
    }

    public final int getNumOfFavorites() {
        return numOfFavorites;
    }

    public final void incrementNumOfFavorites() {
        numOfFavorites++;
    }

    public final boolean isGenre(final Genre genre) {
        return genres.contains(genre);
    }

    @Override
    public final int compareTo(final Video o) {
        return this.rating.compareTo(o.rating);
    }

    @Override
    public final String toString() {
        return this.title;
    }

    /**
     * @param newRating is the rating that needs to be added
     * @param season is the rated season for shows and is ignored for movies
     * @return 1 if the rating was added successfully and -1 if not
     */
    public abstract int addRating(Double newRating, int season);
}
