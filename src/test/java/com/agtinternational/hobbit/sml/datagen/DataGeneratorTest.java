package com.agtinternational.hobbit.sml.datagen;

import com.agtinternational.hobbit.sdk.ComponentsExecutor;
import com.agtinternational.hobbit.sdk.EnvironmentVariables;
import com.agtinternational.hobbit.sdk.docker.AbstractDockerizer;

import com.agtinternational.hobbit.sdk.docker.RabbitMqDockerizer;
import com.agtinternational.hobbit.sdk.docker.builders.DataGeneratorDockerBuilder;
import com.agtinternational.hobbit.sdk.docker.builders.SystemAdapterDockerBuilder;
import com.agtinternational.hobbit.sdk.utils.CommandQueueListener;
import com.agtinternational.hobbit.sdk.utils.commandreactions.StartBenchmarkWhenSystemAndBenchmarkReady;
import com.agtinternational.hobbit.sdk.utils.commandreactions.TerminateServicesWhenBenchmarkControllerFinished;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.agtinternational.hobbit.sml.datagen.Constants.*;

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

        rabbitMqDockerizer = RabbitMqDockerizer.builder().build();

        String experimentId = "http://example.com/exp1";
        String systemUri = "http://agt.com/systems#sys122";

        setupCommunicationEnvironmentVariables(rabbitMqDockerizer.getHostName(), "session1");
        setupBenchmarkEnvironmentVariables(experimentId);
        setupGeneratorEnvironmentVariables(1,1);
        setupSystemEnvironmentVariables(systemUri);

        environmentVariables.set(GENERATOR_SEED,"123");
        environmentVariables.set(GENERATOR_POPULATION, "30");
        environmentVariables.set(FORMAT_INPUT_NAME, "0");


        String systemContainerId = "1234kj34k";

    }

    public void preStart() throws InterruptedException {
        rabbitMqDockerizer.run();
        commandQueueListener.setCommandReactions(new StartGeneratorWhenReady());

        componentsExecutor.submit(commandQueueListener);
        commandQueueListener.waitForInitialisation();
    }

    @Test
    public void startStop() throws Exception {

        preStart();
        componentsExecutor.submit(new DataGenerator());
        componentsExecutor.submit(new SystemAdapter());
        commandQueueListener.waitForTermination();
        Assert.assertFalse(componentsExecutor.anyExceptions());
        after();
    }

    @Test
    public void buildImage() throws Exception {

        AbstractDockerizer dockerizer = new SMLDataGenDockerBuilder(new SMLDockersBuilder(DataGeneratorRunner.class).init()).build();
        dockerizer.prepareImage();
        Assert.assertNull(dockerizer.anyExceptions());
    }

    @Test
    public void startStopDockerized() throws Exception {
        preStart();
        boolean useCachedImage = false;
        componentsExecutor.submit(new SMLDataGenDockerBuilder(new SMLDockersBuilder(DataGeneratorRunner.class).init()).useCachedImage(useCachedImage).build());
        componentsExecutor.submit(new SystemAdapterDockerBuilder(new SMLDockersBuilder(SystemAdapter.class).init()).useCachedImage(useCachedImage).build());
        commandQueueListener.waitForTermination();
        Assert.assertFalse(componentsExecutor.anyExceptions());
        after();
    }

    //@After
    public void after() throws Exception {
        rabbitMqDockerizer.stop();
    }
}
