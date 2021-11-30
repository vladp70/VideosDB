package entertainment;

import java.util.ArrayList;
import java.util.List;

/**
 * Information about a season of a tv show
 * <p>
 * DO NOT MODIFY
 */
public final class Season {
    /**
     * Number of current season
     */
    private final int currentSeason;
    /**
     * Duration in minutes of a season
     */
    private int duration;
    /**
     * List of ratings for each season
     */
    private List<Double> ratings;
    private Double rating = 0.0;

    public Season(final int currentSeason, final int duration) {
        this.currentSeason = currentSeason;
        this.duration = duration;
        this.ratings = new ArrayList<>();
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public List<Double> getRatings() {
        return ratings;
    }

    public Double getRating() {
        return rating;
    }

    public void setRatings(final List<Double> ratings) {
        this.ratings = ratings;
    }

    public void addRating(final Double newRating) {
        int numOfRatings = this.ratings.size();
        this.rating *= numOfRatings;
        this.rating += newRating;
        this.ratings.add(newRating);
        numOfRatings++;
        this.rating /= numOfRatings;
    }

    public int getCurrentSeason() {
        return currentSeason;
    }

    @Override
    public String toString() {
        return "Episode{"
                + "currentSeason="
                + currentSeason
                + ", duration="
                + duration
                + '}';
    }
}

