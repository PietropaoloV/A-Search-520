package Entity;

import Utility.Point;
import Utility.Sentiment;

import java.util.function.Consumer;

public class Grid {
    private GridCell[] grid; // represent as flat array to make deep-copying easier
    private int xSize;
    private int ySize;

    public Grid(int xSize, int ySize, int probability) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.grid = generateGrid(xSize, ySize, probability);
    }

    // deep copy constructor
    public Grid(Grid other) {
        this.xSize = other.getXSize();
        this.ySize = other.getYSize();
        this.grid = new GridCell[xSize * ySize];

        for (int i = 0; i < grid.length; i++) {
            this.grid[i] = (GridCell) other.grid[i].clone();
        }
    }

    private boolean generateIsBlocked(int probabilityOfBlocked) {
        return Math.random() * 100 < probabilityOfBlocked;
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < xSize && y < ySize;
    }

    private GridCell[] generateGrid(int dimX, int dimY, int probabilityOfBlocked) {
        GridCell[] grid = new GridCell[dimX * dimY];

        for (int y = 0; y < dimY; y++) {
            for (int x = 0; x < dimX; x++) {
                int index = y * dimX + x;

                int numAdj = 0; // determine how many neighbours the new cell has
                for (Point neighbour : new Point(x, y).get8Neighbours()) {
                    if (inBounds(neighbour.f1, neighbour.f2))
                        numAdj++;
                }

                // determine if cell is blocked
                boolean isBlocked = (index == 0 || index == dimX * dimY - 1) ? false
                        : generateIsBlocked(probabilityOfBlocked);

                grid[index] = new GridCell(x, y, numAdj, isBlocked);
            }
        }

        // iterate through grid one more time to get C_x for each cell
        for (int y = 0; y < dimY; y++) {
            for (int x = 0; x < dimX; x++) {
                int index = y * dimX + x;
                if (grid[index].isBlocked()) {
                    for (Point adj : new Point(x, y).get8Neighbours()) {
                        if (inBounds(adj.f1, adj.f2)) {
                            grid[adj.f2 * dimX + adj.f1].addNumSensedBlocked(1);
                        }
                    }
                }
            }
        }

        return grid;
    }

    public GridCell getCell(int x, int y) {
        if (inBounds(x, y)) {
            return grid[y * getXSize() + x];
        }
        return null;
    }

    public GridCell getCell(Point coord) {
        return getCell(coord.f1, coord.f2);
    }

    public GridCell[] getGrid() {
        return grid;
    }

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }

    // confirms an unknown cell as blocked/empty and updates KB accordingly
    public void setSentiment(Point coord, Sentiment sent) {
        GridCell cell = getCell(coord);
        if (cell.getBlockSentiment() == sent) return; // shortcut check

        // first reset current cell's sentiment if already set
        switch (cell.getBlockSentiment()) {
            case Free:
                forEachNeighbour(coord, nbr -> nbr.addNumAdjEmpty(-1));
                break;
            case Blocked:
                forEachNeighbour(coord, nbr -> nbr.addNumAdjBlocked(-1));
                break;
            case Unsure:
                break;
        }

        // set cell to new sentiment and update neighbours
        cell.setBlockSentiment(sent);
        switch (sent) {
            case Free:
                forEachNeighbour(coord, nbr -> nbr.addNumAdjEmpty(1));
                break;
            case Blocked:
                forEachNeighbour(coord, nbr -> nbr.addNumAdjBlocked(1));
                break;
            case Unsure:
                break;
        }
    }

    public void forEachNeighbour(Point coord, Consumer<GridCell> action) {
        for(Point adj : coord.get8Neighbours()) {
            GridCell cell = getCell(adj);
            if (cell != null) action.accept(cell);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Entity.Grid{");
        for (int x = 0; x < xSize; x++) {
            builder.append("\n");
            for (int y = 0; y < ySize; y++) {
                if (x == 0 && y == 0) {
                    builder.append("S");
                } else if (x == xSize - 1 && y == ySize - 1) {
                    builder.append("G");
                } else {
                    if (this.getCell(new Point(x, y)).isBlocked()) {
                        builder.append("X");
                    } else {
                        builder.append(" ");
                    }
                }
                builder.append(",");
            }
        }
        builder.append("\n}");
        return builder.toString();
    }
}
