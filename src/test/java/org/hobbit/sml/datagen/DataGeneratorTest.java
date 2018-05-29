package org.hobbit.sml.datagen;

import org.hobbit.core.components.Component;
import org.hobbit.core.mimic.DockerBasedMimickingAlg;
import org.hobbit.sdk.EnvironmentVariablesWrapper;
import org.hobbit.sdk.KeyValue;
import org.hobbit.sdk.docker.AbstractDockerizer;

import org.hobbit.sdk.docker.RabbitMqDockerizer;
import org.hobbit.sdk.utils.CommandQueueListener;

import org.hobbit.sdk.utils.ComponentsExecutor;
import org.hobbit.sdk.utils.commandreactions.MultipleCommandsReaction;
import org.hobbit.sml.datagen.docker.CommonDockersBuilder;
import org.hobbit.sml.datagen.docker.SMLDataGenDockerBuilder;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static org.hobbit.core.Constants.DATA_QUEUE_NAME_KEY;
import static org.hobbit.sml.datagen.Constants.*;

/**
 * @author Pavel Smirnov
 */

public class DataGeneratorTest extends EnvironmentVariablesWrapper {

    private AbstractDockerizer rabbitMqDockerizer;
    private ComponentsExecutor componentsExecutor;
    private CommandQueueListener commandQueueListener;

    private SMLDataGenDockerBuilder dataGenBuilder;

    public void init() throws Exception {

        componentsExecutor = new ComponentsExecutor();
        commandQueueListener = new CommandQueueListener();

        rabbitMqDockerizer = RabbitMqDockerizer.builder().hostName("rabbit").build();

        String experimentId = "http://example.com/exp1";
        String systemUri = "http://agt.com/systems#sys122";

        setupCommunicationEnvironmentVariables(rabbitMqDockerizer.getHostName(), "session1");
        //setupBenchmarkEnvironmentVariables(experimentId);
        setupGeneratorEnvironmentVariables(1,1);
        //setupSystemEnvironmentVariables(systemUri);

//        environmentVariables.set(GENERATOR_SEED_KEY,"123");
//        environmentVariables.set(GENERATOR_POPULATION_KEY, "5000");
//        environmentVariables.set(OUTPUT_FORMAT_KEY, "0");
//        environmentVariables.set(DATA_QUEUE_NAME_KEY, "test1");

        dataGenBuilder = new SMLDataGenDockerBuilder(new CommonDockersBuilder(DataGeneratorRunner.class, DATAGEN_IMAGE_NAME).useCachedImage(true));

    }



    @Test
    @Ignore
    public void buildImage() throws Exception {
        init();
        AbstractDockerizer dockerizer = dataGenBuilder.build();
        dockerizer.prepareImage();
        Assert.assertNull(dockerizer.anyExceptions());
    }

    @Test
    public void checkHeath() throws Exception {
        exec(false);
    }

    @Test
    public void checkHeathDockerized() throws Exception {
        exec(true);
    }

    public void exec(Boolean dockerize) throws Exception {

        init();

        rabbitMqDockerizer.run();
        commandQueueListener.setCommandReactions(new StartGeneratorWhenReady());

        componentsExecutor.submit(commandQueueListener);
        commandQueueListener.waitForInitialisation();

        Component dataGen = new DataGenerator();

        if(dockerize){
            boolean useCachedImage = true;
            dataGen = dataGenBuilder.build();
        }



        commandQueueListener.setCommandReactions(
                new MultipleCommandsReaction.Builder(componentsExecutor, commandQueueListener)
                        .customContainerImage(dataGen, DATAGEN_IMAGE_NAME)
                        .build()
        );

        DockerBasedMimickingAlg alg = new DockerBasedMimickingAlg(commandQueueListener, DATAGEN_IMAGE_NAME);
        alg.generateData("/mnt/share/Projects/mm-datagen", createParameters());

        commandQueueListener.waitForTermination();
        Assert.assertFalse(componentsExecutor.anyExceptions());
        after();
    }



    //@After
    public void after() throws Exception {
        rabbitMqDockerizer.stop();
    }

    public static String[] createParameters(){
        KeyValue kv = new KeyValue();
        kv.setValue(GENERATOR_SEED_KEY,"123");
        kv.setValue(GENERATOR_POPULATION_KEY, "3000");
        kv.setValue(OUTPUT_FORMAT_KEY, "0");
        //kv.setValue(DATA_QUEUE_NAME_KEY, "test1");
        return kv.mapToArray();
    }
}
