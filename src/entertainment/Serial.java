package entertainment;

import actor.Actor;

import java.util.ArrayList;

public class Serial extends Video {
    private int numberOfSeasons;
    private ArrayList<Season> seasons;

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public Serial(String title, int year, ArrayList<Genre> genres, ArrayList<Actor> cast, int numberOfSeasons, ArrayList<Season> seasons) {
        super(title, year, genres, cast);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
        this.updateRating();
        this.setDuration(this.calculateDuration());
    }

    public int calculateDuration()
    {
        int sum = 0;
        for (Season s : seasons)
        {
            sum += s.getDuration();
        }
        return sum;
    }

    //TODO implement rating system for serials
    @Override
    public void updateRating() {

    }
}
