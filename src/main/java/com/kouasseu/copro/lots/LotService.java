package com.kouasseu.copro.lots;

import java.time.LocalDate;

/**
 * Facade publique du module "Lots" — contrat exposé aux appelants externes.
 *
 * <p>Cette interface est dans le package racine ({@code com.kouasseu.copro.lots}) :
 * elle fait partie de l'<strong>API publique</strong> du module.
 *
 * <p>Les appelants externes (ex: un contrôleur REST, un runner de démo) injectent
 * {@code LotService} et non {@code GestionDesLots} (l'implémentation interne).
 * Spring Modulith empêche l'injection directe de {@code GestionDesLots} depuis
 * l'extérieur du module car elle est dans {@code com.kouasseu.copro.lots.application}
 * (sous-package = interne).
 *
 * <pre>
 * // ✅ Autorisé — utilise l'interface publique
 * {@literal @}Autowired LotService lotService;
 *
 * // ❌ Interdit — accède à l'implémentation interne (ApplicationModules.verify() échoue)
 * {@literal @}Autowired GestionDesLots gestionDesLots;
 * </pre>
 */
public interface LotService {

    /**
     * Enregistre la mutation de propriété d'un lot.
     *
     * @param numeroLot   identifiant du lot (ex: "A12")
     * @param acquereurId identifiant du nouvel acquéreur
     * @param date        date effective de la mutation
     */
    void enregistrerMutation(String numeroLot, String acquereurId, LocalDate date);
}
