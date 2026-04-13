package com.kouasseu.copro.assemblee.domain;

/**
 * Value Object : procuration donnée à un mandataire pour voter à l'AG.
 *
 * <p>Conformément à l'article 22 de la loi du 10 juillet 1965,
 * un copropriétaire absent peut donner pouvoir à un autre copropriétaire
 * ou au syndic pour voter en son nom.
 *
 * <p>Ce concept n'existe que dans le contexte "AG". Les autres contextes
 * n'ont aucune notion de représentation ou de procuration.
 *
 * @param mandataire      identifiant du copropriétaire porteur du pouvoir
 * @param numeroLotMandant numéro du lot dont le propriétaire donne procuration
 */
public record Pouvoir(String mandataire, String numeroLotMandant) {

    public Pouvoir {
        if (mandataire == null || mandataire.isBlank()) {
            throw new IllegalArgumentException("Le mandataire est obligatoire");
        }
        if (numeroLotMandant == null || numeroLotMandant.isBlank()) {
            throw new IllegalArgumentException("Le numéro du lot mandant est obligatoire");
        }
    }
}
