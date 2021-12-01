package main;

import actor.Actor;
import actor.ActorsAwards;
import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import database.Database;
import database.User;
import entertainment.Genre;
import entertainment.Video;
import fileio.ActionInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.Writer;
import org.json.simple.JSONArray;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        Database database = new Database();
        database.initDatabaseContent(input);
        for (ActionInputData action : input.getCommands()) {
            StringBuilder str = new StringBuilder();
            if (action.getActionType().equals(Constants.COMMAND)) {
                int returnCode;
                if (action.getType().equals(Constants.FAVOURITE_MOVIES)) {
                    returnCode = database.commandFavorite(action.getUsername(), action.getTitle());
                    if (returnCode == 1) {
                        str.append("success -> " + action.getTitle() + " was added as favourite");
                    } else if (returnCode == 0) {
                        str.append("error -> " + action.getTitle()
                                + " is already in favourite list");
                    } else if (returnCode == -1) {
                        str.append("error -> " + action.getTitle() + " is not seen");
                    }
                } else if (action.getType().equals(Constants.VIEW_MOVIES)) {
                    returnCode = database.commandView(action.getUsername(), action.getTitle());
                    if (returnCode == -1) {
                        str.append("error -> " + action.getTitle()
                                + " not watched :(... na situatie!");
                    } else {
                        str.append("success -> " + action.getTitle()
                                + " was viewed with total views of " + returnCode);
                    }
                } else if (action.getType().equals(Constants.RATE_MOVIES)) {
                    returnCode = database.commandRating(action.getUsername(), action.getTitle(),
                            action.getGrade(), action.getSeasonNumber());
                    if (returnCode == 1) {
                        str.append("success -> " + action.getTitle() + " was rated with "
                                + action.getGrade() + " by " + action.getUsername());
                    } else if (returnCode == 0) {
                        str.append("error -> " + action.getTitle() + " has been already rated");
                    } else if (returnCode == -1) {
                        str.append("error -> " + action.getTitle() + " is not seen");
                    }
                }
            } else if (action.getActionType().equals(Constants.RECOMMENDATION)) {
                if (action.getType().equals(Constants.STANDARD)) {
                    Video recommendation = database.recommendStandard(action.getUsername());
                    if (recommendation != null) {
                        str.append("StandardRecommendation result: " + recommendation.getTitle());
                    } else {
                        str.append("StandardRecommendation cannot be applied!");
                    }
                } else if (action.getType().equals(Constants.BEST_UNSEEN)) {
                    Video recommendation = database.recommendBestUnseen(action.getUsername());
                    if (recommendation != null) {
                        str.append("BestRatedUnseenRecommendation result: "
                                + recommendation.getTitle());
                    } else {
                        str.append("BestRatedUnseenRecommendation cannot be applied!");
                    }

                } else if (action.getType().equals(Constants.POPULAR)) {
                    Video recommendation = database.recommendPopular(action.getUsername());
                    if (recommendation != null) {
                        str.append("PopularRecommendation result: " + recommendation.getTitle());
                    } else {
                        str.append("PopularRecommendation cannot be applied!");
                    }
                } else if (action.getType().equals(Constants.FAVOURITE_MOVIES)) {
                    Video recommendation = database.recommendFavorite(action.getUsername());
                    if (recommendation != null) {
                        str.append("FavoriteRecommendation result: "
                                + recommendation.getTitle());
                    } else {
                        str.append("FavoriteRecommendation cannot be applied!");
                    }
                } else if (action.getType().equals(Constants.SEARCH)) {
                    ArrayList<Video> recommendation =
                            database.recommendSearch(action.getUsername(),
                                    Utils.stringToGenre(action.getGenre()));
                    if (recommendation.size() > 0) {
                        str.append("SearchRecommendation result: "
                                + recommendation);
                    } else {
                        str.append("SearchRecommendation cannot be applied!");
                    }
                }
            } else if (action.getActionType().equals(Constants.QUERY)) {
                boolean asc = (action.getSortType().equals(Constants.ASCENDENT));
                str.append("Query result: ");
                if (action.getObjectType().equals(Constants.ACTORS)) {
                    List<Actor> result = null;
                    if (action.getCriteria().equals(Constants.AVERAGE)) {
                        result = database.queryActorsAverage(action.getNumber(), asc);
                    } else if (action.getCriteria().equals(Constants.AWARDS)) {
                        ArrayList<ActorsAwards> filter =
                                Utils.stringsToAwards(action.getFilters().get(Constants.AWARD_ID));
                        result = database.queryActorsAwards(filter, action.getNumber(), asc);
                    } else if (action.getCriteria().equals(Constants.FILTER_DESCRIPTIONS)) {
                        result = database.queryActorsDescription(action.getFilters().get(2), asc);
                    }

                    str.append(result);
                } else if (action.getObjectType().equals(Constants.MOVIES)
                        || action.getObjectType().equals(Constants.SHOWS)) {
                    List<Video> result = null;
                    boolean isMovie = action.getObjectType().equals(Constants.MOVIES);
                    Integer filterYear = 0;
                    Genre filterGenre = null;
                    if (action.getFilters().get(0).get(0) != null) {
                        filterYear = Integer.valueOf(action.getFilters().get(0).get(0));
                    }
                    if (action.getFilters().get(1).get(0) != null) {
                        filterGenre = Utils.stringToGenre(action.getFilters().get(1).get(0));
                        if (filterGenre == null) {
                            result = new ArrayList<>();
                            str.append(result);
                            arrayResult.add(fileWriter.writeFile(action.getActionId(),
                                    "", str.toString()));
                            continue;
                        }
                    }

                    if (action.getCriteria().equals(Constants.RATINGS)) {
                        result = database.queryMoviesRating(isMovie, filterYear, filterGenre,
                                action.getNumber(), asc);
                    } else if (action.getCriteria().equals(Constants.FAVOURITE_MOVIES)) {
                        result = database.queryMoviesFavorite(isMovie, filterYear, filterGenre,
                                action.getNumber(), asc);
                    } else if (action.getCriteria().equals(Constants.LONGEST)) {
                        result = database.queryMoviesLongest(isMovie, filterYear, filterGenre,
                                action.getNumber(), asc);
                    } else if (action.getCriteria().equals(Constants.MOST_VIEWED)) {
                        result = database.queryMoviesMostViewed(isMovie, filterYear, filterGenre,
                                action.getNumber(), asc);
                    }

                    str.append(result);
                } else if (action.getObjectType().equals(Constants.USERS)) {
                    List<User> result = database.queryUsersActive(action.getCriteria(),
                            action.getNumber(), asc);
                    str.append(result);
                }
            }
            arrayResult.add(fileWriter.writeFile(action.getActionId(), "", str.toString()));
        }

        fileWriter.closeJSON(arrayResult);
    }
}
