package org.hobbit.sml.datagen;

public class DataGeneratorRunner {
    public static void main(String[] args) throws Exception {
        DataGenerator dataGenerator = new DataGenerator();
        dataGenerator.init();
        dataGenerator.generateData();
        dataGenerator.sendData();
        dataGenerator.close();
    }
}
