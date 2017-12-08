package com.agtinternational.hobbit.sml.datagen;

import com.agtinternational.hobbit.sdk.CommonConstants;
import com.agtinternational.hobbit.sdk.docker.BuildBasedDockerizer;
import com.agtinternational.hobbit.sdk.docker.builders.common.DynamicDockerFileBuilder;
import org.hobbit.core.Constants;

import static com.agtinternational.hobbit.sml.datagen.Constants.FORMAT_INPUT_NAME;
import static com.agtinternational.hobbit.sml.datagen.Constants.GENERATOR_SEED;
import static org.hobbit.core.Constants.RABBIT_MQ_HOST_NAME_KEY;

public class SMLDockersBuilder extends DynamicDockerFileBuilder {

    public SMLDockersBuilder(Class value){
        super("SMLDockersBuilder");
        imageNamePrefix("git.project-hobbit.eu:4567/smirnp/sml-");
        jarFilePath("mm-datagen-1.0-SNAPSHOT.jar");
        buildDirectory("target");
        dockerWorkDir("/usr/src/sml-datagen");
        //runnerClass(org.hobbit.core.run.ComponentStarter.class, value);
        runnerClass(value);
    }

    @Override
    public BuildBasedDockerizer build() throws Exception{
        throw new Exception("Direct build is prohibited!");
    };

}


//public class DataGeneratorDockerBuilder extends DynamicDockerFileBuilder {
//    public DataGeneratorDockerBuilder() {
//        super("SMLBenchmarkDockerizer");
//
//        imageName("sml-data-generator");
//        containerName("sml-data-generator-cont");
//        runnerClass(DataGenerator.class);
//
//        repoPath("git.project-hobbit.eu:4567/smirnp");
//        jarFilePath("sml-datagen-1.0-SNAPSHOT.jar");
//        buildDirectory("target");
//        dockerWorkDir("/usr/src/sml-datagen");
//        addNetworks(CommonConstants.networks);
//        addEnvironmentVariable(RABBIT_MQ_HOST_NAME_KEY, (String)System.getenv().get(Constants.RABBIT_MQ_HOST_NAME_KEY));
//        addEnvironmentVariable(Constants.GENERATOR_ID_KEY, (String)System.getenv().get(Constants.GENERATOR_ID_KEY));
//        addEnvironmentVariable(Constants.GENERATOR_COUNT_KEY, (String)System.getenv().get(Constants.GENERATOR_COUNT_KEY));
//        addEnvironmentVariable(GENERATOR_SEED, (String)System.getenv().get(GENERATOR_SEED));
//        addEnvironmentVariable(FORMAT_INPUT_NAME, (String)System.getenv().get(FORMAT_INPUT_NAME));
//    }
//
//    @Override
//    public BuildBasedDockerizer build() throws Exception {
//
//        return super.build();
//    }
//}