package com.copro.charges.application;

import com.copro.charges.domain.CoproprietaireId;
import com.copro.charges.domain.LotChargeableRepository;
import com.copro.charges.domain.NumeroLot;
import com.copro.lots.LotMute;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
public class SurMutationDeLot {

    private final LotChargeableRepository repository;

    public SurMutationDeLot(LotChargeableRepository repository) {
        this.repository = repository;
    }

    @ApplicationModuleListener
    public void reagir(LotMute event) {
        var lot = repository.parNumero(new NumeroLot(event.numeroLot()));
        lot.transfererCompte(new CoproprietaireId(event.nouveauProprietaire()));
    }
}
