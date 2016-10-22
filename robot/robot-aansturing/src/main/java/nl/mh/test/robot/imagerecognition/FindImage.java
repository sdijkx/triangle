package nl.mh.test.robot.imagerecognition;

import nl.mh.test.robot.domain.Coordinates;
import nl.mh.test.robot.domain.PhotoCoordinates;
import nl.mh.test.robot.imagerecognition.converters.Converter;
import nl.mh.test.robot.imagerecognition.loopstrategy.LoopStrategy;
import org.apache.commons.math3.util.Pair;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.StreamSupport.stream;

/**
 * Created by Marc on 22-6-2016.
 */
public class FindImage {
    private static Logger LOG = LoggerFactory.getLogger(FindImage.class);
    private final LoopStrategy loopStrategy;
    private final String netwerkFile;
    private final Converter converter;
    private final double threshold;
    private long last;

    public FindImage(LoopStrategy loopStrategy, String netwerkFile, Converter converter, double threshold) {
        this.loopStrategy = loopStrategy;
        this.netwerkFile = netwerkFile;
        this.converter = converter;
        this.threshold = threshold;
    }




    public PhotoCoordinates findBest(Coordinates coordinates) throws ImageNotFoundException {
        long start = System.currentTimeMillis();
        BasicNetwork network = loadNetwork();

        List<Pair<Double,ImageData>> list = StreamSupport.stream(loopStrategy.spliterator(), false)
                .map(sub -> new Pair<Double,ImageData>(network.compute(converter.convert(sub)).getData(0), sub))
                .sorted((p1, p2) -> p2.getKey().compareTo(p1.getKey()))
                .collect(Collectors.toList());


        ImageData found = list.get(0).getValue();

        return new PhotoCoordinates(found.getX(), found.getY());

    }



    public PhotoCoordinates findImage(Coordinates coordinates) throws ImageNotFoundException {
        long start = System.currentTimeMillis();
        BasicNetwork network = loadNetwork();

//        List<Pair<Double,ImageData>> list = StreamSupport.stream(loopStrategy.spliterator(), false)
//                .map(sub -> new Pair<Double,ImageData>(network.compute(converter.convert(sub)).getData(0), sub))
//                .sorted((p1, p2) -> p1.getKey().compareTo(p2.getKey()))
//                .collect(Collectors.toList());

       // list.stream().forEach(i -> System.out.println("List value --> x:" + i.getX() + " ,y:"+ i.getY() + " -- "+ network.compute(converter.convert(i)).getData(0)));


        ImageData found = StreamSupport.stream(loopStrategy.spliterator(), false)
                .filter(sub -> network.compute(converter.convert(sub)).getData(0) > threshold)
                .findAny()
                .orElseThrow(() -> new ImageNotFoundException("No image found at threshold: " + threshold));

        LOG.debug("value --> " + network.compute(converter.convert(found)).getData(0));

        return new PhotoCoordinates(found.getX(), found.getY());

    }


    public BasicNetwork loadNetwork() {
        LOG.debug("Loading network");
        return (BasicNetwork) EncogDirectoryPersistence.loadResourceObject(netwerkFile);
    }


}
