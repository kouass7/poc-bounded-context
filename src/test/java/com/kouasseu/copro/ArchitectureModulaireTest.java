package com.kouasseu.copro;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

/**
 * LE TEST QUI VERROUILLE LES FRONTIÈRES DES BOUNDED CONTEXTS.
 *
 * <p>Ce test n'a pas besoin de démarrer le contexte Spring — il analyse
 * statiquement la structure de packages à partir de la classe principale.
 * Rapide à exécuter, il peut (et doit) être lancé à chaque build CI/CD.
 *
 * <p><strong>Ce test échoue si :</strong>
 * <ul>
 *   <li>{@code charges} ou {@code assemblee} importe une classe de
 *       {@code com.kouasseu.copro.lots.domain} (Lot, NumeroLot, Tantiemes...)</li>
 *   <li>{@code charges} accède à {@code GestionDesLots} dans
 *       {@code com.kouasseu.copro.lots.application}</li>
 *   <li>Un module a une dépendance vers un module non déclaré dans
 *       {@code @ApplicationModule(allowedDependencies)}</li>
 *   <li>Un cycle de dépendances existe entre modules</li>
 *   <li>Un module dépasse les frontières d'accès définies par sa hiérarchie
 *       de packages</li>
 * </ul>
 *
 * <p><strong>Sans ce test :</strong> les frontières de Bounded Context sont
 * une convention d'équipe — respectées si tout le monde y pense.<br>
 * <strong>Avec ce test :</strong> les frontières sont une <em>contrainte
 * architecturale vérifiée automatiquement</em> à chaque build.
 *
 * <p>C'est la différence entre un panneau "interdiction de passer" et un mur.
 */
class ArchitectureModulaireTest {

    /**
     * Instanciation statique : Spring Modulith analyse les packages au chargement
     * de la classe, sans démarrer de contexte Spring.
     * Cela le rend très rapide (< 1s) pour un usage en CI.
     */
    static final ApplicationModules MODULES = ApplicationModules.of(CoproApplication.class);

    @Test
    void verifierFrontieresBoundedContexts() {
        // ══════════════════════════════════════════════════════════════════
        // VÉRIFICATION ARCHITECTURALE
        //
        // Spring Modulith analyse :
        //   1. Les dépendances déclarées dans chaque @ApplicationModule
        //   2. Les imports réels dans le code source compilé
        //   3. La cohérence entre déclarations et imports
        //
        // Tout écart → AssertionError avec un message explicite du type :
        //   "Module 'charges' depends on module 'lots' via
        //    com.kouasseu.copro.lots.domain.Lot but this dependency is not declared"
        // ══════════════════════════════════════════════════════════════════
        MODULES.verify();
    }

    @Test
    void afficherStructureDesModules() {
        // Affiche un résumé de chaque module : dépendances, API publique, classes internes.
        // Utile pour les revues d'architecture et l'onboarding des nouveaux développeurs.
        MODULES.forEach(module -> {
            System.out.println("═".repeat(60));
            System.out.println("Module : " + module.getName());
            System.out.println("Package : " + module.getBasePackage());
            System.out.println(module);
        });
    }
}
