package org.hobbit.sml.datagen;


import org.hobbit.sdk.docker.BuildBasedDockerizer;
import org.hobbit.sdk.docker.builders.common.DynamicDockerFileBuilder;

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
