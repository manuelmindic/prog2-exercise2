package at.ac.fhcampuswien.fhmdb.api;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.*;
import com.google.gson.*;

public class MovieAPI {
    private static final String URL = "http://localhost:8080/movies"; //local because API is not accessible
    private static final String queryURL = "query=";
    private static final String genreURL = "genre=";
    private static final String releaseYearURL = "releaseYear=";
    private static final String ratingFromURL = "ratingFrom=";

    private static String buildUrl(String query, Genre genre, String releaseYear, String ratingFrom) {
        StringBuilder urlBuild = new StringBuilder(URL);

        if ((query != null && !query.isEmpty()) || genre != null || releaseYear != null || ratingFrom != null) {
            urlBuild.append("?");
            if (query != null && !query.isEmpty()) {
                urlBuild.append(queryURL).append(query).append("&");
            }
            if (genre != null) {
                urlBuild.append(genreURL).append(genre).append("&");
            }
            if (releaseYear != null) {
                urlBuild.append(releaseYearURL).append(releaseYear).append("&");
            }
            if (ratingFrom != null) {
                urlBuild.append(ratingFromURL).append(ratingFrom).append("&");
            }
        }
        return urlBuild.toString();
    }

    public static List<Movie> getAllMovies(String query, Genre genre, String releaseYear, String ratingFrom){
        String urlBuild = buildUrl(query, genre, releaseYear, ratingFrom);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(urlBuild)
                .removeHeader("User-Agent")
                .addHeader("User-Agent", "http.agent")
                .build();

        try(Response response = client.newCall(request).execute()){
            String responseBody = response.body().string();
            Gson gson = new Gson();
            Movie[] movies = gson.fromJson(responseBody, Movie[].class);

            return Arrays.asList(movies);
        }
        catch (Exception e){
            System.err.println("exception");
            e.printStackTrace(System.err);
        }
        return new ArrayList<>();
    }

    public static List<Movie> getAllMovies(){
        return getAllMovies(null, null, null, null);
    }
}