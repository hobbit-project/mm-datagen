package org.hobbit.sml.datagen.docker;


import org.hobbit.sdk.docker.BuildBasedDockerizer;
import org.hobbit.sdk.docker.builders.DynamicDockerFileBuilder;

public class CommonDockersBuilder extends DynamicDockerFileBuilder {

    public static String GIT_REPO_PATH = "git.project-hobbit.eu:4567/smirnp/";
    public static String PROJECT_NAME = "sml-v1-mimicking-";

    public static final String DATAGEN_IMAGE_NAME = GIT_REPO_PATH+PROJECT_NAME +"datagen";


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
