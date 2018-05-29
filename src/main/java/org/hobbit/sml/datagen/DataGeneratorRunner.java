package org.hobbit.sml.datagen;

import org.junit.contrib.java.lang.system.EnvironmentVariables;

public class DataGeneratorRunner {

    public static void main(String[] args) throws Exception {
        EnvironmentVariables environmentVariables = new EnvironmentVariables();

        environmentVariables.set("HOBBIT_GENERATOR_ID", "1");
        environmentVariables.set("HOBBIT_GENERATOR_COUNT", "1");
        environmentVariables.set("HOBBIT_RABBIT_HOST", "rabbit");

        DataGenerator dataGenerator = new DataGenerator();
        dataGenerator.init();
        dataGenerator.generateData();
        dataGenerator.close();
    }
}
