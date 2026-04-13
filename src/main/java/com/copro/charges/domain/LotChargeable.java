package com.copro.charges.domain;

import java.util.Map;

public class LotChargeable {

    private final NumeroLot numero;
    private CoproprietaireId debiteur;
    private final Map<CleDeRepartition, Tantiemes> quotesParts;

    public LotChargeable(NumeroLot numero, CoproprietaireId debiteur, Map<CleDeRepartition, Tantiemes> quotesParts) {
        this.numero = numero;
        this.debiteur = debiteur;
        this.quotesParts = quotesParts;
    }

    public Montant calculerQuotePart(AppelDeFonds appel) {
        var tantiemes = quotesParts.get(appel.cle());
        return appel.montantTotal().auProrata(tantiemes);
    }

    public void transfererCompte(CoproprietaireId nouveauDebiteur) {
        this.debiteur = nouveauDebiteur;
    }

    public NumeroLot numero() {
        return numero;
    }

    public CoproprietaireId debiteur() {
        return debiteur;
    }
}
