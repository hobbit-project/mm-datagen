package org.hobbit.sml.datagen.docker;

import org.hobbit.sdk.docker.builders.DynamicDockerFileBuilder;
import org.hobbit.sdk.docker.builders.hobbit.DataGenDockerBuilder;

import static org.hobbit.core.Constants.*;
import static org.hobbit.sml.datagen.Constants.*;

public class SMLDataGenDockerBuilder extends DataGenDockerBuilder {

    public SMLDataGenDockerBuilder(DynamicDockerFileBuilder builder){
        super(builder);

        builder.addEnvironmentVariable(GENERATOR_SEED_KEY, (String)System.getenv().get(GENERATOR_SEED_KEY));
        builder.addEnvironmentVariable(GENERATOR_POPULATION_KEY, (String)System.getenv().get(GENERATOR_POPULATION_KEY));
        builder.addEnvironmentVariable(OUTPUT_FORMAT_KEY, (String)System.getenv().get(OUTPUT_FORMAT_KEY));
        builder.addEnvironmentVariable(DATA_QUEUE_NAME_KEY, (String)System.getenv().get(DATA_QUEUE_NAME_KEY));



    }
}
