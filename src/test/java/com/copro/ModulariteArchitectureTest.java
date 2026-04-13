package com.copro;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModulariteArchitectureTest {

    @Test
    void verifierArchitectureModulaire() {
        ApplicationModules.of(CoproApplication.class).verify();
    }
}
