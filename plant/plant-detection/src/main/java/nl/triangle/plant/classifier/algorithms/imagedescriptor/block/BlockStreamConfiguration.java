package nl.triangle.plant.classifier.algorithms.imagedescriptor.block;

import nl.triangle.plant.classifier.algorithms.imagedescriptor.cell.Cell;
import nl.triangle.plant.classifier.algorithms.imagedescriptor.cell.CellFunction;

/**
 * Created by steven on 13-06-16.
 */
public class BlockStreamConfiguration<T extends Cell> {
    private final int cellSize;
    private final int blockWidth;
    private final int blockHeight;
    private final CellFunction<T> cellFunction;

    public BlockStreamConfiguration(int cellSize, int blockWidth, int blockHeight, CellFunction<T> cellFunction) {
        this.cellSize = cellSize;
        this.blockWidth = blockWidth;
        this.blockHeight = blockHeight;
        this.cellFunction = cellFunction;
    }

    public int getCellSize() {
        return cellSize;
    }

    public int getBlockWidth() {
        return blockWidth;
    }
    public int getBlockHeight() {
        return blockHeight;
    }
    public int getImageWidth() { return cellSize * 2 * blockWidth; }
    public int getImageHeight() { return cellSize * 2 * blockHeight; }
    public int getDescriptorSize() { return blockWidth * blockHeight * 4 * 9; }
    public CellFunction<T> getCellFunction() {
        return cellFunction;
    }
}
