package com.agtinternational.hobbit.sml.datagen;

import com.agt.ferromatikdata.GeneratorTasks;
import com.agt.ferromatikdata.client.FerromatikDatasetModelFacade;
import com.agt.ferromatikdata.core.GeneratorTask;
import com.agt.ferromatikdata.formatting.CsvFormatter;
import com.agt.ferromatikdata.formatting.OutputFormatter;
import com.agt.ferromatikdata.formatting.RdfFormatter;
import org.hobbit.core.Commands;
import org.hobbit.core.components.AbstractDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static com.agtinternational.hobbit.sdk.CommonConstants.CHARSET;
import static com.agtinternational.hobbit.sml.datagen.Constants.FORMAT_INPUT_NAME;

/**
 * @author Pavel Smirnov
 */

public class DataGenerator extends AbstractDataGenerator {
    private static final Logger logger = LoggerFactory.getLogger(DataGenerator.class);
    private com.agt.ferromatikdata.dataframe.DataGenerator dataGenerator;
    private long bytesSent;
    private OutputFormatter outputFormatter;
    //private final TerminationMessageProtocol.OutputSender outputSender = new TerminationMessageProtocol.OutputSender();

    @Override
    public void init() throws Exception {
        // Always init the super class first!
        super.init();
        logger.debug("Init()");

        if(!System.getenv().containsKey(Constants.GENERATOR_SEED))
            throw new Exception(Constants.GENERATOR_SEED+" is not defined");

        if(!System.getenv().containsKey(FORMAT_INPUT_NAME))
            throw new Exception(FORMAT_INPUT_NAME+" is not defined");

        if(!System.getenv().containsKey(Constants.GENERATOR_POPULATION))
            throw new Exception(Constants.GENERATOR_POPULATION+" is not defined");

        GeneratorTask task =  GeneratorTasks.newOneMachineTask(Integer.parseInt(System.getenv().get(Constants.GENERATOR_POPULATION)), FerromatikDatasetModelFacade.deserializeDefault());

        outputFormatter = newOutputFormatter();
        dataGenerator = new com.agt.ferromatikdata.dataframe.DataGenerator.Builder()
                .generatorTask(task)
                .generationDelayNanos(1000)
                .initialDelayMillis(0)
                .dataPointsListener(newDataPointsListener())
                .seed(Integer.parseInt(System.getenv().get(Constants.GENERATOR_SEED)))
                .charset(CHARSET)
                .gapBetweenMeasurements(Duration.ofSeconds(1))
                .startingDateTime(getStartingDateTime())
                .build();
        String test="123";
    }

    @Override
    protected void generateData() throws Exception {
        logger.debug("generateData()");
        int dataGeneratorId = getGeneratorId();
        int numberOfGenerators = getNumberOfGenerators();

        dataGenerator.run();
        logger.debug("generation finished");
        sendToCmdQueue(Commands.DATA_GENERATION_FINISHED);
    }

    @Override
    public void close() throws IOException {
        // Free the resources you requested here
        logger.debug("close()");
        // Always close the super class after yours!
        super.close();
    }

    private com.agt.ferromatikdata.dataframe.DataGenerator.DataPointsListener newDataPointsListener() throws Exception {
        return dataPointSource -> {
            try {
                String outputDataPoint = dataPointSource.formatUsing(outputFormatter);
                byte[] bytesToSend = outputDataPoint.getBytes();
                sendDataToSystemAdapter(bytesToSend);
                bytesSent += bytesToSend.length;
                logger.debug(String.valueOf(bytesToSend.length)+" bytes sent to rabbitMQ");
            } catch (Exception e) {
                logger.error("Exception", e);
            }
        };
    }


    private OutputFormatter newOutputFormatter() throws Exception {
        int formatInt = Integer.parseInt(System.getenv().get(FORMAT_INPUT_NAME));
        //int formatInt = 0;
        OutputFormatter formatter = (formatInt == 0) ? new RdfFormatter(CHARSET) : new CsvFormatter(CHARSET);
        formatter.init();
        return formatter;
    }

    private static LocalDateTime getStartingDateTime(){
        int year = 2017;
        int month = 1;
        int dayOfMonth = 1;
        int hour = 1;
        int minute = 0;
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute);
    }

}