package com.copro.assemblee.domain;

public class DroitDeVote {

    private final NumeroLot lot;
    private final Voix nombreDeVoix;
    private final Votant titulaire;
    private final Pouvoir pouvoir;

    public DroitDeVote(NumeroLot lot, Voix nombreDeVoix, Votant titulaire, Pouvoir pouvoir) {
        this.lot = lot;
        this.nombreDeVoix = nombreDeVoix;
        this.titulaire = titulaire;
        this.pouvoir = pouvoir;
    }

    public boolean estRepresente() {
        return titulaire.estPresent() || pouvoir != null;
    }

    public NumeroLot lot() {
        return lot;
    }

    public Voix nombreDeVoix() {
        return nombreDeVoix;
    }

    public Votant titulaire() {
        return titulaire;
    }

    public Pouvoir pouvoir() {
        return pouvoir;
    }
}
