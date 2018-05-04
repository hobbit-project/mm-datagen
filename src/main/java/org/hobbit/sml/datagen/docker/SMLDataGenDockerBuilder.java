package org.hobbit.sml.datagen.docker;

import org.hobbit.sdk.docker.builders.DynamicDockerFileBuilder;
import org.hobbit.sdk.docker.builders.hobbit.DataGenDockerBuilder;

import static org.hobbit.core.Constants.*;
import static org.hobbit.sml.datagen.Constants.FORMAT_INPUT_NAME;
import static org.hobbit.sml.datagen.Constants.GENERATOR_POPULATION;
import static org.hobbit.sml.datagen.Constants.GENERATOR_SEED;

public class SMLDataGenDockerBuilder extends DataGenDockerBuilder {

    public SMLDataGenDockerBuilder(DynamicDockerFileBuilder builder){
        super(builder);

        builder.addEnvironmentVariable(GENERATOR_SEED, (String)System.getenv().get(GENERATOR_SEED));
        builder.addEnvironmentVariable(GENERATOR_POPULATION, (String)System.getenv().get(GENERATOR_POPULATION));
        builder.addEnvironmentVariable(FORMAT_INPUT_NAME, (String)System.getenv().get(FORMAT_INPUT_NAME));
        builder.addEnvironmentVariable(DATA_QUEUE_NAME_KEY, (String)System.getenv().get(DATA_QUEUE_NAME_KEY));



    }
}
