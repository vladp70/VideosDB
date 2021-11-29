package actor;

import entertainment.Video;

import java.util.ArrayList;
import java.util.Map;

public class Actor {
    private String name;
    private String careerDescription;
    private ArrayList<Video> filmography;
    private Map<ActorsAwards, Integer> awards;
    private Double average;

    public Actor(String name, String careerDescription, Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = null;
        this.awards = awards;
        this.average = 0.0;
        this.updateRating();
    }

    //TODO (if need be) add constructor with just the name for new mentions

    public String getName() {
        return name;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public ArrayList<Video> getFilmography() {
        return filmography;
    }

    public void setFilmography(ArrayList<Video> filmography) {
        this.filmography = filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public void updateRating() {
        Double sum = 0.0;
        int numOfRatedVideos = 0;
        for (Video v : this.getFilmography()) {
            if (v.getRating() != 0)
            {
                numOfRatedVideos++;
                sum += v.getRating();
            }
        }
        if (numOfRatedVideos > 0)
            this.setAverage(sum / numOfRatedVideos);
        else
            this.setAverage(0.0);
    }
}
