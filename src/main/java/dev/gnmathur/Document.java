package dev.gnmathur;

import java.io.Serializable;

public record Document(long id, String title, String abstractText) implements Serializable { }
