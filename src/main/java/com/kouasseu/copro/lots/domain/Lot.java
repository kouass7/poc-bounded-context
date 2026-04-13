package com.kouasseu.copro.lots.domain;

import com.kouasseu.copro.lots.LotMute;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Aggregate Root : représentation d'un lot dans le contexte "Lots & Copropriétaires".
 *
 * <p><strong>Responsabilités de cet agrégat :</strong>
 * <ol>
 *   <li>Protéger les invariants — un lot doit toujours avoir un propriétaire valide</li>
 *   <li>Incarner le <em>langage ubiquitaire</em> du contexte "Lots" (ex: {@code muter()})</li>
 *   <li>Produire des Domain Events qui notifient les autres contextes des changements</li>
 * </ol>
 *
 * <p><strong>Ce que cet agrégat NE fait PAS (hors périmètre) :</strong>
 * <ul>
 *   <li>Calculer les charges → rôle de {@code LotChargeable} (contexte Charges)</li>
 *   <li>Gérer des droits de vote → rôle de {@code DroitDeVote} (contexte AG)</li>
 * </ul>
 *
 * <p><strong>Note sur {@link LotMute} :</strong>
 * L'import de {@code LotMute} depuis {@code com.kouasseu.copro.lots} (package racine) est autorisé
 * car on est dans le même module. Ce n'est PAS un couplage inter-contextes.
 */
public class Lot {

    private final NumeroLot numero;
    private final NatureLot nature;
    private Tantiemes tantiemes;
    private CoproprietaireId proprietaire;

    public Lot(NumeroLot numero, NatureLot nature, Tantiemes tantiemes, CoproprietaireId proprietaire) {
        this.numero = Objects.requireNonNull(numero, "Le numéro de lot est obligatoire");
        this.nature = Objects.requireNonNull(nature, "La nature du lot est obligatoire");
        this.tantiemes = Objects.requireNonNull(tantiemes, "Les tantièmes sont obligatoires");
        this.proprietaire = Objects.requireNonNull(proprietaire, "Le propriétaire est obligatoire");
    }

    /**
     * Mute la propriété du lot vers un nouvel acquéreur.
     *
     * <p><strong>Langage ubiquitaire :</strong> dans ce contexte, l'expert métier
     * (notaire, syndic) dit "muter" un lot — pas "transférer" (vocabulaire de
     * {@code charges}) ni "changer de titulaire" (vocabulaire de {@code assemblee}).
     * Chaque Bounded Context a son propre vocabulaire pour le même phénomène.
     *
     * <p><strong>Pattern Domain Event :</strong>
     * L'agrégat produit l'événement et le retourne — il ne le publie pas lui-même.
     * C'est l'Application Service ({@code GestionDesLots}) qui se charge de la
     * publication via {@code ApplicationEventPublisher}. Cela garde le domaine
     * indépendant du mécanisme de messagerie Spring.
     *
     * <p><strong>Types primitifs dans l'événement :</strong>
     * {@link LotMute} utilise des {@code String} et non {@code NumeroLot} /
     * {@code CoproprietaireId} afin que les contextes récepteurs restent
     * totalement indépendants du modèle interne de "Lots".
     *
     * @param acquereur le nouveau propriétaire
     * @param date      date effective de la mutation (acte notarié)
     * @return l'événement de domaine à publier
     */
    public LotMute muter(CoproprietaireId acquereur, LocalDate date) {
        Objects.requireNonNull(acquereur, "L'acquéreur est obligatoire pour une mutation");
        Objects.requireNonNull(date, "La date de mutation est obligatoire");

        var ancienProprietaire = this.proprietaire;
        this.proprietaire = acquereur;

        // Types primitifs dans l'événement → découplage garanti
        return new LotMute(
            numero.value(),
            ancienProprietaire.value(),
            acquereur.value(),
            date
        );
    }

    // ─── Accesseurs (lecture seule depuis l'extérieur) ────────────────────────

    public NumeroLot getNumero() { return numero; }
    public NatureLot getNature() { return nature; }
    public Tantiemes getTantiemes() { return tantiemes; }
    public CoproprietaireId getProprietaire() { return proprietaire; }
}
