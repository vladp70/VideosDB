package entertainment;

import actor.Actor;

import java.util.ArrayList;

public final class Movie extends Video {

    public Movie(final String title, final int year, final ArrayList<Genre> genres,
                 final ArrayList<Actor> cast, final int duration) {
        super(title, year, genres, cast);
        this.setDuration(duration);
    }

    public Movie(final String title) {
        super(title);
    }

    @Override
    public int addRating(final Double rating, final int season) {
        int numOfRatings = super.getNumOfRatings();
        Double average = numOfRatings * super.getRating();
        average += rating;
        numOfRatings++;
        average /= numOfRatings;
        super.setRating(average);
        super.setNumOfRatings(numOfRatings);
        return 1;
    }
}
