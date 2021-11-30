package database;

import actor.Actor;
import actor.ActorsAwards;
import common.Constants;
import entertainment.Genre;
import entertainment.Movie;
import entertainment.Serial;
import entertainment.Video;
import fileio.ActorInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import fileio.Input;
import utils.Utils;

import java.util.*;

public final class Database {
    private final ArrayList<Actor> actors;
    private final ArrayList<Movie> movies;
    private final ArrayList<Serial> serials;
    private final ArrayList<User> users;

    private final Map<Genre, Integer> genrePopularity;

    public Database() {
        actors = new ArrayList<>();
        movies = new ArrayList<>();
        serials = new ArrayList<>();
        users = new ArrayList<>();
        genrePopularity = new HashMap<>();
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public ArrayList<Serial> getSerials() {
        return serials;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public Map<Genre, Integer> getGenrePopularity() {
        return genrePopularity;
    }

    //Prerequisite: @calculateGenrePopularity
    public Genre getMostPopularGenre() {
        Map.Entry<Genre, Integer> mostPopular = null;
        for (Map.Entry<Genre, Integer> g : genrePopularity.entrySet()) {
            if (mostPopular == null || g.getValue() > mostPopular.getValue()) {
                mostPopular = g;
            }
        }
        return mostPopular.getKey();
    }

    public Video findVideo(final String film) {
        for (var existingMovie : this.getMovies()) {
            if (film.equals(existingMovie.getTitle())) {
                return existingMovie;
            }
        }
        for (var existingSerial : this.getSerials()) {
            if (film.equals(existingSerial.getTitle())) {
                return existingSerial;
            }
        }

        return new Movie(film);
    }

    public Actor findActor(final String actor) {
        for (var existingActor : this.getActors()) {
            if (actor.equals(existingActor.getName())) {
                return existingActor;
            }
        }

        return new Actor(actor);
    }

    public User findUser(final String user) {
        for (var existingUser : this.getUsers()) {
            if (user.equals(existingUser.getUsername())) {
                return existingUser;
            }
        }
        //TODO (if need be) add new hollow user or verify if result is null
        return null;
    }

    public ArrayList<Video> stringsToVideos(final ArrayList<String> filmStrings) {
        ArrayList<Video> res = new ArrayList<>();
        for (var currentFilm : filmStrings) {
            res.add(findVideo(currentFilm));
        }
        return res;
    }

    public ArrayList<Actor> stringsToActors(final ArrayList<String> actorStrings) {
        ArrayList<Actor> res = new ArrayList<>();
        for (var currentActor : actorStrings) {
            res.add(findActor(currentActor));
        }
        return res;
    }

    public Map<Video, Integer> convertMapStringToVideo(final Map<String, Integer> map) {
        Map<Video, Integer> res = new HashMap<>();
        for (var m : map.entrySet()) {
            res.put(findVideo(m.getKey()), m.getValue());
        }
        return res;
    }

    //No prerequisites necessary
    private void addActorsFromInputWithoutFilmography(final List<ActorInputData> actorData) {
        for (var data : actorData) {
            actors.add(new Actor(data.getName(), data.getCareerDescription(), data.getAwards()));
        }
    }

    //Ideally called after addActorsFromInput
    private void addMoviesFromInput(final List<MovieInputData> movieData) {
        for (var data : movieData) {
            movies.add(new Movie(data.getTitle(), data.getYear(),
                    Utils.stringsToGenres(data.getGenres()),
                    this.stringsToActors(data.getCast()), data.getDuration()));
        }
    }

    //Ideally called after addActorsFromInput
    private void addSerialsFromInput(final List<SerialInputData> serialData) {
        for (var data : serialData) {
            serials.add(new Serial(data.getTitle(), data.getYear(),
                    Utils.stringsToGenres(data.getGenres()), this.stringsToActors(data.getCast()),
                    data.getNumberSeason(), data.getSeasons()));
        }
    }

    //Ideally called after adding Videos
    private void addUsersFromInput(final List<UserInputData> userData) {
        for (var data : userData) {
            users.add(new User(data.getUsername(), data.getSubscriptionType(),
                    this.convertMapStringToVideo(data.getHistory()),
                    this.stringsToVideos(data.getFavoriteMovies())));
        }
    }

    //Prerequisite: @addActorsFromInputWithoutFilmography called
    private void initActorsFilmographyFromInput(final List<ActorInputData> actorData) {
        ArrayList<Video> filmography;
        Actor actor;
        for (var data : actorData) {
            actor = findActor(data.getName());
            filmography = stringsToVideos(data.getFilmography());
            actor.setFilmography(filmography);
            actor.updateRating();
        }
    }

    //Prerequisite: @addUsersFromInput @addVideosFromInput
    private void initVideoViewsAndFavorites() {
        for (var u : users) {
            for (Map.Entry<Video, Integer> m : u.getHistory().entrySet()) {
                m.getKey().incrementViews();
            }
            for (var m : u.getFavorites()) {
                m.incrementNumOfFavorites();
            }
        }
    }

    //To be used only when needed
    private void calculateGenrePopularity() {
        for (var m : movies) {
            for (var g : m.getGenres()) {
                if (genrePopularity.containsKey(g)) {
                    genrePopularity.put(g, genrePopularity.get(g) + 1);
                } else {
                    genrePopularity.put(g, 1);
                }
            }
        }
        for (var s : serials) {
            for (var g : s.getGenres()) {
                if (genrePopularity.containsKey(g)) {
                    genrePopularity.put(g, genrePopularity.get(g) + 1);
                } else {
                    genrePopularity.put(g, 1);
                }
            }
        }
    }

    //To be used only when needee
    private void updateActorsRatings() {
        for (var a : actors) {
            a.updateRating();
        }
    }

    public void initDatabaseContent(final Input input) {
        addActorsFromInputWithoutFilmography(input.getActors());
        addMoviesFromInput(input.getMovies());
        addSerialsFromInput(input.getSerials());
        addUsersFromInput(input.getUsers());
        initActorsFilmographyFromInput(input.getActors());
        initVideoViewsAndFavorites();
    }

    public int commandFavorite(final String username, final String title) {
        Video film = findVideo(title);
        User user = findUser(username);
        int returnCode = user.addFavorite(film);
        if (returnCode == 1) {
            film.incrementNumOfFavorites();
        }
        return returnCode;
    }

    public int commandView(final String username, final String title) {
        Video film = findVideo(title);
        User user = findUser(username);
        int returnCode = user.watchFilm(film);
        if (returnCode == 1) {
            film.incrementViews();
        }
        return returnCode;
    }

    public int commandRating(final String username, final String title,
                             final double grade, final int season) {
        Video film = findVideo(title);
        if (film.getYear() == -1) {
            return -1;
        }
        User user = findUser(username);
        if (user == null) {
            return -1;
        }
        int returnCode = -1;
        if (season <= 0) {
            returnCode = user.addRatingMovie(film, grade);
        } else {
            returnCode = user.addRatingSerial(film, grade, season);
        }
        if (returnCode == 1) {
            return film.addRating(grade, season);
        } else {
            return returnCode;
        }

    }

    public Video recommendStandard(final String username) {
        User user = findUser(username);
        if (user == null) {
            return null;
        }

        for (var movie : movies) {
            if (!(user.hasWatched(movie))) {
                return movie;
            }
        }
        for (var show : serials) {
            if (!(user.hasWatched(show))) {
                return show;
            }
        }
        return null;
    }

    public Video recommendBestUnseen(final String username) {
        User user = findUser(username);
        if (user == null) {
            return null;
        }

        Video bestUnseen = null;
        ArrayList<Movie> sortedMovies = new ArrayList<>(movies);
        Collections.sort(sortedMovies);
        for (var movie : sortedMovies) {
            if (!(user.hasWatched(movie))) {
                bestUnseen = movie;
                break;
            }
        }
        ArrayList<Serial> sortedSerials = new ArrayList<>(serials);
        Collections.sort(sortedSerials);
        for (var show : sortedSerials) {
            if (!(user.hasWatched(show))) {
                if (show.compareTo(bestUnseen) > 0) {
                    bestUnseen = show;
                }
                break;
            }
        }
        return bestUnseen;
    }

    public Video recommendPopular(final String username) {
        User user = findUser(username);
        if (user == null || user.getSubscriptionType().equals(SubscriptionType.BASIC)) {
            return null;
        }

        this.calculateGenrePopularity();
        Genre mostPopularGenre = null;
        Video recommendationPopular = null;
        while (!(this.genrePopularity.isEmpty())) {
            mostPopularGenre = getMostPopularGenre();
            for (var m : movies) {
                if (m.getGenres().contains(mostPopularGenre) && !(user.hasWatched(m))) {
                    return m;
                }
            }
            for (var s : serials) {
                if (s.getGenres().contains(mostPopularGenre) && !(user.hasWatched(s))) {
                    return s;
                }
            }
            this.genrePopularity.remove(mostPopularGenre);
        }

        if (recommendationPopular != null && !(user.hasWatched(recommendationPopular))) {
            return recommendationPopular;
        } else {
            return null;
        }
    }

    public Video recommendFavorite(final String username) {
        User user = findUser(username);
        if (user == null || user.getSubscriptionType().equals(SubscriptionType.BASIC)) {
            return null;
        }

        Video mostFavorited = null;
        ArrayList<Movie> sortedMovies = new ArrayList<>(movies);
        Collections.sort(sortedMovies, new Comparator<Movie>() {
            @Override
            public int compare(final Movie o1, final Movie o2) {
                return o1.getNumOfFavorites() - o2.getNumOfFavorites();
            }
        });
        for (var movie : sortedMovies) {
            if (!(user.hasWatched(movie))) {
                mostFavorited = movie;
                break;
            }
        }
        ArrayList<Serial> sortedSerials = new ArrayList<>(serials);
        Collections.sort(sortedSerials, new Comparator<Serial>() {
            @Override
            public int compare(final Serial o1, final Serial o2) {
                return o1.getNumOfFavorites() - o2.getNumOfFavorites();
            }
        });
        for (var show : sortedSerials) {
            if (!(user.hasWatched(show))) {
                if (show.getNumOfFavorites() > mostFavorited.getNumOfFavorites()) {
                    mostFavorited = show;
                }
                break;
            }
        }
        if (mostFavorited != null && mostFavorited.getNumOfFavorites() > 0) {
            return mostFavorited;
        } else {
            return null;
        }
    }

    public ArrayList<Video> recommendSearch(final String username, final Genre genre) {
        User user = findUser(username);
        ArrayList<Video> recommendation = new ArrayList<>();
        if (user == null || user.getSubscriptionType().equals(SubscriptionType.BASIC)
                || genre == null) {
            return recommendation;
        }

        for (var m : movies) {
            if (m.getGenres().contains(genre) && !(user.hasWatched(m))) {
                recommendation.add(m);
            }
        }
        for (var s : serials) {
            if (s.getGenres().contains(genre) && !(user.hasWatched(s))) {
                recommendation.add(s);
            }
        }
        Collections.sort(recommendation, new Comparator<Video>() {
            @Override
            public int compare(final Video o1, final Video o2) {
                if (o1.getRating().equals(o2.getRating())) {
                    return o1.getTitle().compareTo(o2.getTitle());
                } else {
                    return o2.compareTo(o1);
                }
            }
        });

        return recommendation;
    }

    public List<Actor> queryActorsAverage(final int limit, final boolean asc) {
        this.updateActorsRatings();
        ArrayList<Actor> res = new ArrayList<>(actors);
        res.removeIf(actor -> actor.getAverage() <= 0);
        var comp = new Comparator<Actor>() {
            public int compare(final Actor o1, final Actor o2) {
                if (!(o1.getAverage().equals(o2.getAverage()))) {
                    return o1.getAverage().compareTo(o2.getAverage());
                } else {
                    return o1.getName().compareTo(o2.getName());
                }
            }
        };
        if (asc) {
            Collections.sort(res, comp);
        } else {
            Collections.sort(res, Collections.reverseOrder(comp));
        }
        if (limit <= res.size()) {
            return new ArrayList<>(res.subList(0, limit));
        } else {
            return res;
        }
    }

    public List<Actor> queryActorsAwards(final ArrayList<ActorsAwards> filter,
                                         final int limit, final boolean asc) {
        ArrayList<Actor> res = new ArrayList<>();
        boolean ok = true;
        for (var a : actors) {
            ok = true;
            for (var f : filter) {
                if (!(a.hasAward(f))) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                res.add(a);
            }
        }

        var comp = new Comparator<Actor>() {
            public int compare(final Actor o1, final Actor o2) {
                if (o1.getAwards().size() != o2.getAwards().size()) {
                    return o1.getAwards().size() - o2.getAwards().size();
                } else {
                    return o1.getName().compareTo(o2.getName());
                }
            }
        };
        if (asc) {
            Collections.sort(res, comp);
        } else {
            Collections.sort(res, Collections.reverseOrder(comp));
        }
        if (limit <= res.size()) {
            return new ArrayList<>(res.subList(0, limit));
        } else {
            return res;
        }
    }

    public List<Actor> queryActorsDescription(final List<String> filter, final boolean asc) {
        ArrayList<Actor> res = new ArrayList<>();
        boolean ok = true;
        for (var a : actors) {
            ok = true;
            for (var f : filter) {
                if (!(a.includesWordDescription(f))) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                res.add(a);
            }
        }

        var comp = new Comparator<Actor>() {
            public int compare(final Actor o1, final Actor o2) {
                    return o1.getName().compareTo(o2.getName());
            }
        };
        if (asc) {
            Collections.sort(res, comp);
        } else {
            Collections.sort(res, Collections.reverseOrder(comp));
        }

        return res;
    }

    public List<User> queryUsersActive(final String criteria, final int limit,
                                            final boolean asc) {
        if (!(criteria.equals(Constants.NUM_RATINGS))) {
            return new ArrayList<>();
        }

        ArrayList<User> res = new ArrayList<>(users);
        res.removeIf(user -> user.getNumOfRatings() <= 0);
        var comp = new Comparator<User>() {
            public int compare(final User o1, final User o2) {
                if (o1.getNumOfRatings() != o2.getNumOfRatings()) {
                    return o1.getNumOfRatings() - o2.getNumOfRatings();
                } else {
                    return o1.getUsername().compareTo(o2.getUsername());
                }
            }
        };
        if (asc) {
            Collections.sort(res, comp);
        } else {
            Collections.sort(res, Collections.reverseOrder(comp));
        }
        if (limit <= res.size()) {
            return new ArrayList<>(res.subList(0, limit));
        } else {
            return res;
        }
    }
}
