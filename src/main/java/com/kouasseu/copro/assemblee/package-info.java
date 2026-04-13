/**
 * Bounded Context "Assemblée Générale".
 *
 * <p>Gère les droits de vote, la présence et les pouvoirs lors des AG.
 * Dans ce contexte, un lot est avant tout un <em>droit de vote</em>.
 *
 * <p><strong>Frontière Spring Modulith :</strong>
 * Même contrainte que {@code charges} : accès autorisé à l'API publique de {@code lots}
 * (uniquement {@link com.kouasseu.copro.lots.LotMute}) mais l'interne reste inaccessible.
 *
 * <pre>
 * ✅ Autorisé   : LotMute            (com.kouasseu.copro.lots         — API publique)
 * ❌ INTERDIT   : Lot                (com.kouasseu.copro.lots.domain  — interne)
 * ❌ INTERDIT   : LotChargeable      (com.kouasseu.copro.charges.domain — autre contexte, interne)
 * </pre>
 *
 * <p><strong>Langage ubiquitaire de ce contexte :</strong>
 * <ul>
 *   <li>"changerDeTitulaire" (pas "muter" ni "transfererCompte")</li>
 *   <li>"DroitDeVote", "Voix", "Votant", "Pouvoir" (procuration)</li>
 *   <li>"estRepresente" — notion centrale de l'AG</li>
 * </ul>
 */
@ApplicationModule(
    allowedDependencies = {"lots"}
    // Accès autorisé à l'API publique de "lots" pour écouter LotMute.
    // Aucune dépendance directe vers "charges" — les deux contextes restent
    // totalement indépendants l'un de l'autre.
)
package com.kouasseu.copro.assemblee;

import org.springframework.modulith.ApplicationModule;
