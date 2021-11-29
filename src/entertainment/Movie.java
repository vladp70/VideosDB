package entertainment;

import actor.Actor;

import java.util.ArrayList;

public class Movie extends Video {

    public Movie(String title, int year, ArrayList<Genre> genres, ArrayList<Actor> cast, int duration) {
        super(title, year, genres, cast);
        this.setDuration(duration);
    }

    //TODO implement rating system for movies
    @Override
    public void updateRating() {

    }
}
