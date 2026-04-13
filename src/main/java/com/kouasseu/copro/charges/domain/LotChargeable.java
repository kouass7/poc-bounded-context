package com.kouasseu.copro.charges.domain;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Entity du contexte "Charges" : le lot vu comme une <em>unité de débit</em>.
 *
 * <p><strong>Modèle autonome — même concept, représentation différente :</strong>
 * <pre>
 * com.kouasseu.copro.lots.domain.Lot          → le lot = une propriété (nature, tantièmes bruts)
 * com.kouasseu.copro.charges.domain.LotChargeable → le lot = une source de revenus (quotes-parts par clé)
 * com.kouasseu.copro.assemblee.domain.DroitDeVote → le lot = un droit de vote (voix, présence)
 * </pre>
 *
 * <p>Ce modèle ne connaît PAS :
 * <ul>
 *   <li>La {@code NatureLot} (APPARTEMENT, CAVE...) — hors périmètre ici</li>
 *   <li>La notion de "mutation" — dans "charges", on "transfère un compte"</li>
 * </ul>
 */
public class LotChargeable {

    private final NumeroLot numero;

    /**
     * Débiteur actuel : le copropriétaire à qui sera adressé l'appel de fonds.
     * Mis à jour lors d'une mutation de lot (réception de l'événement {@code LotMute}).
     */
    private CoproprietaireId debiteur;

    /**
     * Quotes-parts par clé de répartition.
     * Un lot peut avoir des tantièmes différents selon les parties communes :
     * ex: 250/10000 pour les charges générales, 0/10000 pour l'ascenseur (RDC).
     */
    private final Map<CleDeRepartition, Tantiemes> quotesParts;

    public LotChargeable(NumeroLot numero, CoproprietaireId debiteur,
                         Map<CleDeRepartition, Tantiemes> quotesParts) {
        this.numero = Objects.requireNonNull(numero);
        this.debiteur = Objects.requireNonNull(debiteur);
        this.quotesParts = new EnumMap<>(Objects.requireNonNull(quotesParts));
    }

    /**
     * Calcule la quote-part de ce lot pour un appel de fonds.
     *
     * <p>Si le lot n'est pas assujetti à la clé de répartition de l'appel
     * (ex: un RDC pour l'ascenseur), retourne un montant nul.
     */
    public Montant calculerQuotePart(AppelDeFonds appel) {
        var tantiemes = quotesParts.get(appel.cle());
        if (tantiemes == null) {
            // Ce lot n'est pas assujetti à cette clé de répartition
            return Montant.de(0.0);
        }
        return appel.montantTotal().auProrata(tantiemes);
    }

    /**
     * Transfère le compte vers un nouveau débiteur.
     *
     * <p><strong>Langage ubiquitaire :</strong> dans le contexte "Charges", on dit
     * "transférer le compte" — pas "muter" (vocabulaire de "Lots") ni
     * "changer de titulaire" (vocabulaire de "AG"). Chaque contexte a son vocabulaire.
     *
     * <p>Cette méthode est appelée par {@code SurMutationDeLot} en réaction à
     * l'événement {@code LotMute} publié par le contexte "Lots".
     */
    public void transfererCompte(CoproprietaireId nouveauDebiteur) {
        this.debiteur = Objects.requireNonNull(nouveauDebiteur, "Le nouveau débiteur est obligatoire");
    }

    public NumeroLot getNumero() { return numero; }
    public CoproprietaireId getDebiteur() { return debiteur; }
    public Map<CleDeRepartition, Tantiemes> getQuotesParts() { return Map.copyOf(quotesParts); }
}
