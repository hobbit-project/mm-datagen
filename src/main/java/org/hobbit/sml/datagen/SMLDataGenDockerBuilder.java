package org.hobbit.sml.datagen;


import org.hobbit.sdk.docker.builders.DataGeneratorDockerBuilder;
import org.hobbit.sdk.docker.builders.common.DynamicDockerFileBuilder;

import static org.hobbit.core.Constants.*;
import static org.hobbit.sml.datagen.Constants.FORMAT_INPUT_NAME;
import static org.hobbit.sml.datagen.Constants.GENERATOR_POPULATION;
import static org.hobbit.sml.datagen.Constants.GENERATOR_SEED;

public class SMLDataGenDockerBuilder extends DataGeneratorDockerBuilder {

    public SMLDataGenDockerBuilder(DynamicDockerFileBuilder builder){
        super(builder);
        imageNamePrefix(builder.getImageNamePrefix());

        addEnvironmentVariable(HOBBIT_SESSION_ID_KEY, (String)System.getenv().get(HOBBIT_SESSION_ID_KEY));
        addEnvironmentVariable(GENERATOR_ID_KEY, (String)System.getenv().get(GENERATOR_ID_KEY));
        addEnvironmentVariable(GENERATOR_COUNT_KEY, (String)System.getenv().get(GENERATOR_COUNT_KEY));

        addEnvironmentVariable(GENERATOR_SEED, (String)System.getenv().get(GENERATOR_SEED));
        addEnvironmentVariable(GENERATOR_POPULATION, (String)System.getenv().get(GENERATOR_POPULATION));
        addEnvironmentVariable(FORMAT_INPUT_NAME, (String)System.getenv().get(FORMAT_INPUT_NAME));

        addEnvironmentVariable(DATA_QUEUE_NAME_KEY, (String)System.getenv().get(DATA_QUEUE_NAME_KEY));

    }
}
