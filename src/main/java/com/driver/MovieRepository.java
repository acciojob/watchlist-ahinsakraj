package com.driver;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MovieRepository {


    HashMap<String, Movie> dbm = new HashMap<>();
    HashMap<String, Director> dbd = new HashMap<>();

    HashMap<String, HashSet<String>> pair = new HashMap<>();
    public void addMovie(Movie movie) {
        dbm.put(movie.getName(), movie);
    }

    public void addDirector(Director director) {
        dbd.put(director.getName(), director);
    }

    public void addMovieDirectorPair(String movie, String director) {
        if(!dbd.containsKey(director) || !dbm.containsKey(movie)) {
            return;
        }
        if(!pair.containsKey(director)) {
            pair.put(director, new HashSet<>());
        }
        pair.get(director).add(movie);
    }

    public ResponseEntity<Movie> getMovieByName(String name) {
        return ResponseEntity.ok(dbm.get(name));
    }

    public ResponseEntity<Director> getDirectorByName(String name) {
        return ResponseEntity.ok(dbd.get(name));
    }

    public ResponseEntity<List<String>> getMoviesByDirectorName(String name) {
        List<String> al = new ArrayList<>();
        if(!pair.containsKey(name) || pair.get(name).isEmpty()) {
            return ResponseEntity.ok(al);
        }
        al = new ArrayList<>(pair.get(name));
        Collections.sort(al);
        return ResponseEntity.ok(al);
    }

    public ResponseEntity<List<String>> findAllMovies() {
        if(dbm.isEmpty()) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        List<String> al = new ArrayList<>(dbm.keySet());
        Collections.sort(al);
        return ResponseEntity.ok(al);
    }

    public void deleteDirectorByName(String name) {
        if(!pair.containsKey(name)) {
            return;
        }
        for(String movie : pair.get(name)) {
            if(dbm.containsKey(movie)) {
                dbm.remove(movie);
            }
        }
        if(dbd.containsKey(name))
        dbd.remove(name);
        if(pair.containsKey(name))
        pair.remove(name);
    }

    public void deleteAllDirectors() {
        if(pair.isEmpty()) {
            return;
        }
        for(String director : pair.keySet()) {
            if(pair.get(director).isEmpty()) {
                continue;
            }
            for(String movie : pair.get(director)) {
                if(dbm.containsKey(movie))
                dbm.remove(movie);
            }
        }
        pair.clear();
        dbd.clear();
    }
}
