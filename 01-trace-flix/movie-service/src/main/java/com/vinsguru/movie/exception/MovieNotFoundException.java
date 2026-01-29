package com.vinsguru.movie.exception;

public class MovieNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Movie [id=%d] is not found!";

    public MovieNotFoundException(Integer movieId) {
        super(MESSAGE.formatted(movieId));
    }
}
