package database;

import entertainment.Movie;
import entertainment.Season;
import entertainment.Serial;
import entertainment.Video;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class User {
    private String username;
    private SubscriptionType subscriptionType;
    private Map<Video, Integer> history;
    private ArrayList<Video> favorites;
    private Map<Movie, Double> movieRatings = new HashMap<>();
    private Map<Season, Double> serialRatings = new HashMap<>();
    private int numOfRatings = 0;

    public User(final String username, final String subscriptionType,
                final Map<Video, Integer> history, final ArrayList<Video> favorites) {
        this.username = username;
        this.subscriptionType = Utils.stringToSubscription(subscriptionType);
        this.history = history;
        this.favorites = favorites;
    }

    public String getUsername() {
        return username;
    }

    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public Map<Video, Integer> getHistory() {
        return history;
    }

    public ArrayList<Video> getFavorites() {
        return favorites;
    }

    public int getNumOfRatings() {
        return numOfRatings;
    }

    public boolean hasWatched(final Video film) {
        return history.containsKey(film);
    }

    @Override
    public String toString() {
        return this.username;
    }

    /**
     * Adds a favorite film for the user
     * @param film to be added to favorites
     * @return -1 if the film was not watched, 0 if the film is already in favorites,
     * 1 if it was successfully added
     */
    public int addFavorite(final Video film) {
        if (!(this.getHistory().containsKey(film))) {
            return -1;
        }
        if (this.getFavorites().contains(film)) {
            return 0;
        } else {
            this.getFavorites().add(film);
            return 1;
        }

    }

    /**
     * View film as the user
     * @param film to watch
     * @return -1 if the film is null or the new number of views
     */
    public int watchFilm(final Video film) {
        int views = -1;
        if (film == null) {
            return views;
        }
        if (this.getHistory().containsKey(film)) {
            views = history.get(film) + 1;

        } else {
            views = 1;
        }
        history.put(film, views);
        return views;
    }

    /**
     * @return -1 if the film was not watched or the season cannot be found,
     * 0 if the film is already rated, 1 if it was successfully rated
     */
    public int addRatingSerial(final Video film, final Double grade, final int season) {
        Season ratedSeason = ((Serial) film).findSeason(season);
        if (ratedSeason == null) {
            return -1;
        } else if (serialRatings.containsKey(ratedSeason)) {
            return 0;
        } else {
            if (!hasWatched(film)) {
                return -1;
            }
            serialRatings.put(ratedSeason, grade);
            numOfRatings++;
            return 1;
        }
    }

    /**
     * @return -1 if the film was not watched, 0 if the film is already rated,
     * 1 if it was successfully rated
     */
    public int addRatingMovie(final Video film, final Double grade) {
        if (movieRatings.containsKey(film)) {
            return 0;
        } else if (!hasWatched(film)) {
            return -1;
        }
        movieRatings.put((Movie) film, grade);
        numOfRatings++;
        return 1;
    }
}
