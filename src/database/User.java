package database;

import entertainment.Video;
import utils.Utils;

import java.util.ArrayList;
import java.util.Map;

public class User {
    private String username;
    private SubscriptionType subscriptionType;
    private Map<Video, Integer> history;
    private ArrayList<Video> favorites;
    private int numOfRatings;

    //TODO add class Rating and lists in User and Video

    public User(String username, String subscriptionType, Map<Video, Integer> history, ArrayList<Video> favorites) {
        this.username = username;
        this.subscriptionType = Utils.stringToSubscription(subscriptionType);
        this.history = history;
        this.favorites = favorites;
        numOfRatings = 0;
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

    //TODO add fields for: view, favorite, rate
}
