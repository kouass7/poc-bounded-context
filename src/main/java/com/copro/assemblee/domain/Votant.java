package com.copro.assemblee.domain;

public class Votant {

    private final boolean present;

    public Votant(boolean present) {
        this.present = present;
    }

    public boolean estPresent() {
        return present;
    }
}
