package com.copro.lots.domain;

import com.copro.lots.LotMute;
import java.time.LocalDate;

public class Lot {

    private final NumeroLot numero;
    private final NatureLot nature;
    private final Tantiemes tantiemes;
    private CoproprietaireId proprietaire;

    public Lot(NumeroLot numero, NatureLot nature, Tantiemes tantiemes, CoproprietaireId proprietaire) {
        this.numero = numero;
        this.nature = nature;
        this.tantiemes = tantiemes;
        this.proprietaire = proprietaire;
    }

    public LotMute muter(CoproprietaireId acquereur, LocalDate date) {
        var ancien = this.proprietaire;
        this.proprietaire = acquereur;
        return new LotMute(numero.value(), ancien.value(), acquereur.value(), date);
    }

    public NumeroLot numero() {
        return numero;
    }

    public NatureLot nature() {
        return nature;
    }

    public Tantiemes tantiemes() {
        return tantiemes;
    }

    public CoproprietaireId proprietaire() {
        return proprietaire;
    }
}
