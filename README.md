# Molding Machines Data Generator
It is able to generate unlimited streams of measurements simulating measurements of the sensors deployed on molding machines.
## API
Typical usage to generate files of data looks like:
```Java
try (Writer writer = Files.newBufferedWriter(Paths.get(filePath), charset)) {
            new DataGenerator.Builder()
                    .generatorTask(generatorTask)
                    .generationDelayNanos(1)
                    .initialDelayMillis(0)
                    .dataPointsListener(new DataPointToWriter(writer, formatter))
                    .seed(123L)
                    .charset(charset)
                    .gapBetweenMeasurements(Duration.ofSeconds(1))
                    .startingDateTime(getStartingDateTime())
                    .build()
                    .run();
        }
```
The formatter used above can be of two types: to generate RDF or CSV data. Use RDF formatter:
```Java
OutputFormatter formatter = new RdfFormatter(charset);
        formatter.init();
```
or CSV:
```Java
OutputFormatter formatter = new CsvFormatter(charset);
        formatter.init();

```
Generator task specifies how many molding machines and data points will be produced. You can create GeneratorTask yourself or use static method to obtain it:
```Java
GeneratorTask.newGeneratorTask(Machines machines, int dataPointCount);
```
## Demo Visualization
ChartinClientDemo is used to visualize data generation in real-time. Run start.sh to start it.
