package com.copro.charges.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Montant(BigDecimal value) {

    public Montant auProrata(Tantiemes tantiemes) {
        var ratio = BigDecimal.valueOf(tantiemes.valeur())
                .divide(BigDecimal.valueOf(tantiemes.base()), 8, RoundingMode.HALF_UP);
        return new Montant(value.multiply(ratio).setScale(2, RoundingMode.HALF_UP));
    }
}
