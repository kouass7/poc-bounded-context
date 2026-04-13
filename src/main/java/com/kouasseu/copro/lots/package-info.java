/**
 * Bounded Context "Lots & Copropriétaires" — source de vérité sur les lots.
 *
 * <p><strong>Frontière Spring Modulith :</strong>
 * {@code allowedDependencies = {}} signifie que ce module n'importe RIEN
 * des autres contextes. Il est la source de vérité — les autres contextes
 * l'écoutent via des événements, pas via des imports directs.
 *
 * <p><strong>API publique de ce module (package racine) :</strong>
 * <ul>
 *   <li>{@link com.kouasseu.copro.lots.LotMute}   — Domain Event publié lors d'une mutation</li>
 *   <li>{@link com.kouasseu.copro.lots.LotService} — Facade applicative pour les appels externes</li>
 * </ul>
 *
 * <p><strong>Interne (sous-packages) — non accessible depuis les autres modules :</strong>
 * <ul>
 *   <li>{@code com.kouasseu.copro.lots.domain}       → Lot, NumeroLot, Tantiemes, NatureLot...</li>
 *   <li>{@code com.kouasseu.copro.lots.application}  → GestionDesLots, LotRepositoryEnMemoire</li>
 * </ul>
 */
@ApplicationModule(
    allowedDependencies = {}
    // Ce module NE dépend d'AUCUN autre contexte.
    // Si un développeur ajoute "import com.kouasseu.copro.charges.*" ici
    // → ApplicationModules.verify() échoue au build.
)
package com.kouasseu.copro.lots;

import org.springframework.modulith.ApplicationModule;
