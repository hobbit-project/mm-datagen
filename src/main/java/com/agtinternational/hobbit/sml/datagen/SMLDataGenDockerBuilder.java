package com.agtinternational.hobbit.sml.datagen;

import com.agtinternational.hobbit.sdk.CommonConstants;
import com.agtinternational.hobbit.sdk.docker.builders.DataGeneratorDockerBuilder;
import com.agtinternational.hobbit.sdk.docker.builders.common.DynamicDockerFileBuilder;

import java.nio.file.Paths;

import static com.agtinternational.hobbit.sml.datagen.Constants.FORMAT_INPUT_NAME;
import static com.agtinternational.hobbit.sml.datagen.Constants.GENERATOR_POPULATION;
import static com.agtinternational.hobbit.sml.datagen.Constants.GENERATOR_SEED;
import static org.hobbit.core.Constants.GENERATOR_COUNT_KEY;
import static org.hobbit.core.Constants.GENERATOR_ID_KEY;
import static org.hobbit.core.Constants.HOBBIT_SESSION_ID_KEY;

public class SMLDataGenDockerBuilder extends DataGeneratorDockerBuilder{

    public SMLDataGenDockerBuilder(DynamicDockerFileBuilder builder){
        super(builder);
        imageNamePrefix(builder.getImageNamePrefix());

        addEnvironmentVariable(HOBBIT_SESSION_ID_KEY, (String)System.getenv().get(HOBBIT_SESSION_ID_KEY));
        addEnvironmentVariable(GENERATOR_ID_KEY, (String)System.getenv().get(GENERATOR_ID_KEY));
        addEnvironmentVariable(GENERATOR_COUNT_KEY, (String)System.getenv().get(GENERATOR_COUNT_KEY));

        addEnvironmentVariable(GENERATOR_SEED, (String)System.getenv().get(GENERATOR_SEED));
        addEnvironmentVariable(GENERATOR_POPULATION, (String)System.getenv().get(GENERATOR_POPULATION));
        addEnvironmentVariable(FORMAT_INPUT_NAME, (String)System.getenv().get(FORMAT_INPUT_NAME));
    }
}
