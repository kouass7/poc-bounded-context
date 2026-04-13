/**
 * Bounded Context "Charges & Appels de fonds".
 *
 * <p><strong>Frontière Spring Modulith :</strong>
 * {@code allowedDependencies = {"lots"}} autorise l'utilisation de
 * <strong>l'API publique de {@code lots}</strong> uniquement — c'est-à-dire
 * les classes du package racine {@code com.kouasseu.copro.lots}.
 *
 * <p><strong>Ce que Spring Modulith garantit malgré cette dépendance déclarée :</strong>
 * <pre>
 * ✅ Autorisé   : LotMute        (com.kouasseu.copro.lots         — package racine = API publique)
 * ❌ INTERDIT   : Lot            (com.kouasseu.copro.lots.domain  — sous-package = INTERNE)
 * ❌ INTERDIT   : NumeroLot      (com.kouasseu.copro.lots.domain  — sous-package = INTERNE)
 * ❌ INTERDIT   : GestionDesLots (com.kouasseu.copro.lots.application — sous-package = INTERNE)
 * ❌ INTERDIT   : LotRepository  (com.kouasseu.copro.lots.domain  — sous-package = INTERNE)
 * </pre>
 *
 * <p>Si un développeur ajoute {@code import com.kouasseu.copro.lots.domain.Lot} dans ce module
 * → {@code ApplicationModules.verify()} <strong>fait échouer le build immédiatement</strong>.
 *
 * <p><strong>Langage ubiquitaire de ce contexte :</strong>
 * <ul>
 *   <li>"transfererCompte" (pas "muter" qui appartient au contexte Lots)</li>
 *   <li>"LotChargeable" (pas "Lot")</li>
 *   <li>"AppelDeFonds", "CleDeRepartition", "QuotePart"</li>
 * </ul>
 */
@ApplicationModule(
    allowedDependencies = {"lots"}
    // Seule dépendance autorisée : le package racine de "lots" (LotMute, LotService).
    // L'API interne de "lots" (Lot, NumeroLot, etc.) reste inaccessible.
)
package com.kouasseu.copro.charges;

import org.springframework.modulith.ApplicationModule;
