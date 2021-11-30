package entertainment;

import actor.Actor;

import java.util.ArrayList;

public final class Serial extends Video {
    private int numberOfSeasons;
    private ArrayList<Season> seasons;

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public Serial(final String title, final int year, final ArrayList<Genre> genres,
                  final ArrayList<Actor> cast, final int numberOfSeasons,
                  final ArrayList<Season> seasons) {
        super(title, year, genres, cast);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
        this.setDuration(this.calculateDuration());
    }

    public int calculateDuration() {
        int sum = 0;
        for (Season s : seasons) {
            sum += s.getDuration();
        }
        return sum;
    }

    public Season findSeason(final int season) {
        for (var s : seasons) {
            if (s.getCurrentSeason() == season) {
                return s;
            }
        }
        return null;
    }

    @Override
    public int addRating(final Double rating, final int season) {
        Season aux = findSeason(season);
        if (aux == null) {
            return -1;
        } else {
            aux.addRating(rating);
        }

        super.setNumOfRatings(super.getNumOfRatings() + 1);
        Double average = 0.0;
        for (var s : seasons) {
            average += s.getRating();
        }
        average /= seasons.size();
        super.setRating(average);
        return 1;
    }
}
