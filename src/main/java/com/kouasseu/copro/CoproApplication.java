package com.kouasseu.copro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée de l'application.
 *
 * <p>Spring Modulith découvre les modules à partir de ce package racine ({@code com.kouasseu.copro}).
 * Chaque sous-package de premier niveau ({@code lots}, {@code charges}, {@code assemblee})
 * devient un module vérifié par {@code ApplicationModules.of(CoproApplication.class)}.
 *
 * <pre>
 * com.kouasseu.copro/               ← racine de l'application
 * ├── lots/                ← Bounded Context "Lots & Copropriétaires"
 * ├── charges/             ← Bounded Context "Charges & Appels de fonds"
 * └── assemblee/           ← Bounded Context "Assemblée Générale"
 * </pre>
 */
@SpringBootApplication
public class CoproApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoproApplication.class, args);
    }
}
