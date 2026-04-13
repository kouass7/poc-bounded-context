package com.kouasseu.copro.lots.domain;

/**
 * Énumération métier : type physique d'un lot dans la copropriété.
 *
 * <p>La nature d'un lot est pertinente UNIQUEMENT dans le contexte "Lots" :
 * <ul>
 *   <li>Le contexte "Charges" ne s'en soucie pas — il travaille avec des clés
 *       de répartition ({@code CleDeRepartition})</li>
 *   <li>Le contexte "AG" ne s'en soucie pas non plus — il travaille avec des
 *       droits de vote ({@code Voix})</li>
 * </ul>
 *
 * <p>C'est l'illustration directe du principe Bounded Context : le même "lot"
 * physique est vu sous des angles différents selon le contexte métier.
 */
public enum NatureLot {
    APPARTEMENT,
    CAVE,
    PARKING,
    LOCAL_COMMERCIAL
}
