package org.hobbit.sml.datagen;

import org.hobbit.core.components.Component;
import org.hobbit.sdk.ComponentsExecutor;
import org.hobbit.sdk.EnvironmentVariables;
import org.hobbit.sdk.docker.AbstractDockerizer;

import org.hobbit.sdk.docker.RabbitMqDockerizer;
import org.hobbit.sdk.docker.builders.SystemAdapterDockerBuilder;
import org.hobbit.sdk.utils.CommandQueueListener;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hobbit.core.Constants.DATA_QUEUE_NAME_KEY;
import static org.hobbit.sml.datagen.Constants.*;

/**
 * @author Pavel Smirnov
 */

public class DataGeneratorTest extends EnvironmentVariables {

    private AbstractDockerizer rabbitMqDockerizer;
    private ComponentsExecutor componentsExecutor;
    private CommandQueueListener commandQueueListener;

    @Before
    public void before() throws Exception {

        componentsExecutor = new ComponentsExecutor();
        commandQueueListener = new CommandQueueListener();

        rabbitMqDockerizer = RabbitMqDockerizer.builder().hostName("192.168.56.20").build();

        String experimentId = "http://example.com/exp1";
        String systemUri = "http://agt.com/systems#sys122";

        setupCommunicationEnvironmentVariables(rabbitMqDockerizer.getHostName(), "session1");
        //setupBenchmarkEnvironmentVariables(experimentId);
        setupGeneratorEnvironmentVariables(1,1);
        //setupSystemEnvironmentVariables(systemUri);

        environmentVariables.set(GENERATOR_SEED,"123");
        environmentVariables.set(GENERATOR_POPULATION, "30");
        environmentVariables.set(FORMAT_INPUT_NAME, "0");
        environmentVariables.set(DATA_QUEUE_NAME_KEY, "test1");

        String systemContainerId = "1234kj34k";

        //rabbitMqDockerizer.run();
        commandQueueListener.setCommandReactions(new StartGeneratorWhenReady());

        componentsExecutor.submit(commandQueueListener);
        commandQueueListener.waitForInitialisation();
    }



    @Test
    public void buildImage() throws Exception {
        AbstractDockerizer dockerizer = new SMLDataGenDockerBuilder(new SMLDockersBuilder(DataGeneratorRunner.class).init()).build();
        dockerizer.prepareImage();
        Assert.assertNull(dockerizer.anyExceptions());
    }

    @Test
    public void checkHeath() throws Exception {
        exec(false);
    }

    @Test
    public void startStopDockerized() throws Exception {
        exec(true);
    }

    public void exec(Boolean dockerize) throws Exception {

        before();

        Component dataGen = new DataGenerator();
        Component system = new SystemAdapter();

        if(dockerize){
            boolean useCachedImage = true;
            dataGen = new SMLDataGenDockerBuilder(new SMLDockersBuilder(DataGeneratorRunner.class).init()).useCachedImage(useCachedImage).build();
            //system = new SystemAdapterDockerBuilder(new SMLDockersBuilder(SystemAdapter.class).init()).useCachedImage(useCachedImage).build();
        }

        componentsExecutor.submit(dataGen);
        componentsExecutor.submit(system);

        commandQueueListener.waitForTermination();
        Assert.assertFalse(componentsExecutor.anyExceptions());
        after();
    }

    //@After
    public void after() throws Exception {
        rabbitMqDockerizer.stop();
    }
}
