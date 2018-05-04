package org.hobbit.sml.datagen;

import com.agt.ferromatikdata.GeneratorTasks;
import com.agt.ferromatikdata.client.FerromatikDatasetModelFacade;
import com.agt.ferromatikdata.core.GeneratorTask;
import com.agt.ferromatikdata.formatting.CsvFormatter;
import com.agt.ferromatikdata.formatting.OutputFormatter;
import com.agt.ferromatikdata.formatting.RdfFormatter;
import org.hobbit.core.components.AbstractDataGenerator;
import org.hobbit.core.rabbit.SimpleFileSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.agt.ferromatikdata.anomalydetector.GeneratedDatasetConstants.CHARSET;
import static org.hobbit.core.Constants.DATA_QUEUE_NAME_KEY;
import static org.hobbit.sml.datagen.Constants.OUTPUT_FORMAT_KEY;

/**
 * @author Pavel Smirnov
 */

public class DataGenerator extends AbstractDataGenerator {

    private static final Logger logger = LoggerFactory.getLogger(DataGenerator.class);
    private com.agt.ferromatikdata.dataframe.DataGenerator dataGenerator;
    private long bytesSent;
    private OutputFormatter outputFormatter;
    private final List<String> tuplesToSend = new ArrayList<>();
    private String queueName = DATA_QUEUE_NAME_KEY;
    //private final TerminationMessageProtocol.OutputSender outputSender = new TerminationMessageProtocol.OutputSender();

    @Override
    public void init() throws Exception {
        // Always init the super class first!
        super.init();
        logger.debug("Init()");

        if(!System.getenv().containsKey(Constants.GENERATOR_SEED_KEY))
            throw new Exception(Constants.GENERATOR_SEED_KEY +" is not defined");

        if(!System.getenv().containsKey(OUTPUT_FORMAT_KEY))
            throw new Exception(OUTPUT_FORMAT_KEY +" is not defined");

        if(!System.getenv().containsKey(Constants.GENERATOR_POPULATION_KEY))
            throw new Exception(Constants.GENERATOR_POPULATION_KEY +" is not defined");

        if(!System.getenv().containsKey(DATA_QUEUE_NAME_KEY))
            throw new Exception(DATA_QUEUE_NAME_KEY+" is not defined");

        queueName = System.getenv().get(DATA_QUEUE_NAME_KEY);

        GeneratorTask task =  GeneratorTasks.newOneMachineTask(Integer.parseInt(System.getenv().get(Constants.GENERATOR_POPULATION_KEY)), FerromatikDatasetModelFacade.deserializeDefault());

        outputFormatter = newOutputFormatter();
        dataGenerator = new com.agt.ferromatikdata.dataframe.DataGenerator.Builder()
                .generatorTask(task)
                .generationDelayNanos(1000)
                .initialDelayMillis(0)
                .dataPointsListener(newDataPointsListener())
                .seed(Integer.parseInt(System.getenv().get(Constants.GENERATOR_SEED_KEY)))
                .charset(CHARSET)
                .gapBetweenMeasurements(Duration.ofSeconds(1))
                .startingDateTime(getStartingDateTime())
                .build();
        String test="123";
    }

    //@Override
    public void generateData() throws Exception {
        logger.debug("generateData()");
        int dataGeneratorId = getGeneratorId();
        int numberOfGenerators = getNumberOfGenerators();

        dataGenerator.run();
        logger.debug("generation finished");
        sendData();
    }

    public void sendData() throws IOException {
        String str = String.join("", tuplesToSend.toArray(new String[0]));

        byte[] bytesToSend = str.getBytes();
        logger.debug("Sending "+String.valueOf(bytesToSend.length)+" bytes to rabbitMQ (queueName="+queueName+")");

        InputStream is = new ByteArrayInputStream(bytesToSend);
        SimpleFileSender sender = SimpleFileSender.create(this.outgoingDataQueuefactory, queueName);
        sender.streamData(is, queueName+".rdf");
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
                tuplesToSend.add(outputDataPoint);
                bytesSent += bytesToSend.length;
            } catch (Exception e) {
                logger.error("Exception", e);
            }
        };
    }


    private OutputFormatter newOutputFormatter() throws Exception {
        int formatInt = Integer.parseInt(System.getenv().get(OUTPUT_FORMAT_KEY));
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