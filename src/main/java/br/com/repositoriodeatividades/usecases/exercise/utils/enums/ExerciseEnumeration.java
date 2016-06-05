package br.com.repositoriodeatividades.usecases.exercise.utils.enums;

public enum ExerciseEnumeration {

    BULLET("^\\u2022", "•"),
    NUMBER_BAR("^\\d\\)","^\\d"),
    NUMBER_DOT("^\\d\\.", "^\\d"),
    NUMBER_HYPHEN("^\\d-", "^\\d"),
    NUMBER_SPACE_HYPHEN("^\\d\\s*-", "^\\d"),
    CHAR_BAR("^\\A[^\\W\\d_]{1}\\)", "^\\A[^\\W\\d_]{1}"),
    CHAR_DOT("^\\A[^\\W\\d_]{1}\\.", "^\\A[^\\W\\d_]{1}"),
    CHAR_HYPHEN("^\\A[^\\W\\d_]{1}-", "\\A[^\\W\\d_]{1}"),
    PARENTHESIS("\\(\\)", "\\(\\)"),
    NONE("ˆ\\w", "ˆ\\w");

    private String fullEnumeration;
    private String shortEnumeration;

    ExerciseEnumeration (String fullEnumeration, String shortEnumeration) {
        this.fullEnumeration = fullEnumeration;
        this.shortEnumeration = shortEnumeration;
    }

    public String getFullEnumeration() {
        return fullEnumeration;
    }

    public String getShortEnumeration() {
        return shortEnumeration;
    }
}
