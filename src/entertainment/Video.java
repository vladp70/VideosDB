package entertainment;

import actor.Actor;

import java.util.ArrayList;

public abstract class Video {
    private String title;
    private int year;
    private ArrayList<Genre> genres;
    private Double rating;
    private ArrayList<Actor> cast;
    private int duration;

    //TODO add fields: number of ratings(?), views, number of appearances as favorites

    public Video(String title, int year, ArrayList<Genre> genres) {
        this.title = title;
        this.year = year;
        this.genres = genres;
        cast = new ArrayList<>();
        rating = 0.0;
        duration = 0;
    }

    public Video(String title, int year, ArrayList<Genre> genres, ArrayList<Actor> cast) {
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.cast = cast;
        rating = 0.0;
        duration = 0;
    }

    public ArrayList<Actor> getCast() {
        return cast;
    }

    public void setCast(ArrayList<Actor> cast) {
        this.cast = cast;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    //TODO plan the rating system for videos
    public abstract void updateRating();
}
