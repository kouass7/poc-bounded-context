package com.copro.lots.application;

import com.copro.lots.domain.CoproprietaireId;
import com.copro.lots.domain.LotRepository;
import com.copro.lots.domain.NumeroLot;
import java.time.LocalDate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GestionDesLots {

    private final LotRepository repository;
    private final ApplicationEventPublisher events;

    public GestionDesLots(LotRepository repository, ApplicationEventPublisher events) {
        this.repository = repository;
        this.events = events;
    }

    public void enregistrerMutation(String numeroLot, String acquereurId, LocalDate date) {
        var lot = repository.parNumero(new NumeroLot(numeroLot));
        var event = lot.muter(new CoproprietaireId(acquereurId), date);
        events.publishEvent(event);
    }
}
