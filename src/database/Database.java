package database;

import actor.Actor;
import entertainment.Movie;
import entertainment.Serial;
import entertainment.Video;
import fileio.*;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private ArrayList<Actor> actors;
    private ArrayList<Movie> movies;
    private ArrayList<Serial> serials;
    private ArrayList<User> users;

    public Database() {
        actors = new ArrayList<>();
        movies = new ArrayList<>();
        serials = new ArrayList<>();
        users = new ArrayList<>();
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

    public void addActor(Actor actor) {
        actors.add(actor);
    }

    public Video findVideo(String film) {
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
        //TODO (if need be) add new hollow film
        return null;
    }

    public Actor findActor(String actor) {
        for (var existingActor : this.getActors()) {
            if (actor.equals(existingActor.getName())) {
                return existingActor;
            }
        }
        //TODO (if need be) add new hollow actor
        return null;
    }

    public ArrayList<Video> stringsToVideos(ArrayList<String> films) {
        ArrayList<Video> res = new ArrayList<>();
        for (var currentFilm : films) {
            res.add(findVideo(currentFilm));
        }
        return res;
    }

    public ArrayList<Actor> stringsToActors(ArrayList<String> actors) {
        ArrayList<Actor> res = new ArrayList<>();
        boolean found = false;
        for (var currentActor : actors) {
            res.add(findActor(currentActor));
        }
        return res;
    }

    public Map<Video, Integer> convertMapStringToVideo(Map<String, Integer> map) {
        Map<Video, Integer> res = new HashMap<>();
        for (var m : map.entrySet()) {
            res.put(findVideo(m.getKey()), m.getValue());
        }
        return res;
    }

    //No prerequisites necessary
    private void addActorsFromInputWithoutFilmography(List<ActorInputData> actorData) {
        for (var data : actorData) {
            actors.add(new Actor(data.getName(), data.getCareerDescription(), data.getAwards()));
        }
    }

    //Ideally called after addActorsFromInput
    private void addMoviesFromInput(List<MovieInputData> movieData) {
        for (var data : movieData) {
            movies.add(new Movie(data.getTitle(), data.getYear(), Utils.stringsToGenres(data.getGenres()),
                    this.stringsToActors(data.getCast()), data.getDuration()));
        }
    }

    //Ideally called after addActorsFromInput
    private void addSerialsFromInput(List<SerialInputData> serialData) {
        for (var data : serialData) {
            serials.add(new Serial(data.getTitle(), data.getYear(), Utils.stringsToGenres(data.getGenres()),
                    this.stringsToActors(data.getCast()), data.getNumberSeason(), data.getSeasons()));
        }
    }

    //Ideally called after adding Videos
    private void addUsersFromInput(List<UserInputData> userData) {
        for (var data : userData) {
            users.add(new User(data.getUsername(), data.getSubscriptionType(),
                    this.convertMapStringToVideo(data.getHistory()), this.stringsToVideos(data.getFavoriteMovies())));
        }
    }

    //Prerequisite: @addActorsFromInputWithoutFilmography called
    private void initActorsFilmographyFromInput(List<ActorInputData> actorData) {
        ArrayList<Video> filmography;
        for (var data : actorData) {
            filmography = stringsToVideos(data.getFilmography());
            findActor(data.getName()).setFilmography(filmography);
        }
    }

    public void readFromInput(Input input) {
        addActorsFromInputWithoutFilmography(input.getActors());
        addMoviesFromInput(input.getMovies());
        addSerialsFromInput(input.getSerials());
        addUsersFromInput(input.getUsers());
        initActorsFilmographyFromInput(input.getActors());
    }
}
