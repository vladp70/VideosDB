package actor;

import entertainment.Video;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class Actor {
    private String name;
    private String careerDescription;
    private ArrayList<Video> filmography;
    private Map<ActorsAwards, Integer> awards;
    private Double average;

    public Actor(final String name, final String careerDescription,
                 final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = new ArrayList<>();
        this.awards = awards;
        this.average = 0.0;
    }

    public Actor(final String name) {
        this.name = name;
        this.careerDescription = "";
        this.filmography = new ArrayList<>();
        this.awards = new HashMap<>();
        this.average = -1.0;
    }

    public String getName() {
        return name;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public ArrayList<Video> getFilmography() {
        return filmography;
    }

    public void setFilmography(final ArrayList<Video> filmography) {
        this.filmography = filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(final Double average) {
        this.average = average;
    }

    public void updateRating() {
        Double sum = 0.0;
        int numOfRatedVideos = 0;
        for (Video v : this.getFilmography()) {
            if (v.getRating() != 0) {
                numOfRatedVideos++;
                sum += v.getRating();
            }
        }
        if (numOfRatedVideos > 0) {
            this.setAverage(sum / numOfRatedVideos);
        } else {
            this.setAverage(0.0);
        }
    }

    public boolean hasAward(final ActorsAwards award) {
        return awards.containsKey(award);
    }

    public boolean includesWordDescription(final String word) {
        return careerDescription.toLowerCase(Locale.ROOT).contains(word.toLowerCase(Locale.ROOT));
    }

    @Override
    public String toString() {
        return this.name;
    }
}
