package com.copro.lots;

import java.time.LocalDate;
import org.springframework.modulith.events.Externalized;

@Externalized("copro.lots.LotMute")
public record LotMute(
        String numeroLot,
        String ancienProprietaire,
        String nouveauProprietaire,
        LocalDate dateMutation
) {
}
