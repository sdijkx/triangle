package nl.triangle.plant.classifier.algorithms.imagedescriptor;

import nl.triangle.plant.classifier.algorithms.imagedescriptor.block.Block;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.block.BlockStreamConfiguration;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.block.ContrastNormalizeBlock;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.block.ImageToBlockStream;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.cell.Cell;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.image.ConvolveOpTransform;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.image.ResizeImage;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;

import java.awt.image.BufferedImage;
import java.util.function.Function;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

/**
 * Created by steven on 26-06-16.
 */
public class ImageDescriptorFactory {

    public static ImageDescriptorFactory newInstance() {
        return new ImageDescriptorFactory();
    }

    public ImageDescriptor createHogDescriptor(BlockStreamConfiguration configuration) {
        return (image) ->
            image
                .map(resize(configuration.getImageWidth(),configuration.getImageHeight()))
                .map(convolve(new float[] { 1.0f, 0f, 1f, -1f, 0f, 1f}, 3, 2))
                .map(imageToBlockStream(configuration))
                .map(normalizeBlocks())
                .map(toMLData())
                .orElseThrow(() -> new IllegalStateException());
    }

    private Function<DoubleStream, MLData> toMLData() {
        return (doubleStream -> new BasicMLData(doubleStream.toArray()));
    }

    private <T extends Cell> Function<Stream<Block<Cell>>, DoubleStream> normalizeBlocks() {
        return (blockStream -> new ContrastNormalizeBlock<>().normalize(blockStream));
    }

    private <T extends Cell> Function<BufferedImage, Stream<Block<T>>> imageToBlockStream(BlockStreamConfiguration configuration) {
        return (image) -> new ImageToBlockStream<T>(configuration).toStream(image);
    }

    private Function<BufferedImage, BufferedImage> convolve(float[] matrix, int width, int height) {
        return (image) -> new ConvolveOpTransform(matrix, width, height).transform(image);
    }

    private Function<BufferedImage, BufferedImage> resize(int width, int height) {
        return (image) -> new ResizeImage(width, height).resize(image);
    }
}
