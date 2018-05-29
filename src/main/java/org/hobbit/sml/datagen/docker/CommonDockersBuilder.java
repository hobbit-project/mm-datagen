package org.hobbit.sml.datagen.docker;


import org.hobbit.sdk.docker.BuildBasedDockerizer;
import org.hobbit.sdk.docker.builders.DynamicDockerFileBuilder;

public class CommonDockersBuilder extends DynamicDockerFileBuilder {




    public CommonDockersBuilder(Class value, String imageName) throws Exception {
        super("CommonDockersBuilder");
        imageName(imageName);
        jarFilePath("target/mm-datagen-1.0-SNAPSHOT.jar");
        buildDirectory(".");
        dockerWorkDir("/usr/src/sml-datagen");
        //runnerClass(org.hobbit.core.run.ComponentStarter.class, value);
        runnerClass(value);
    }


}
