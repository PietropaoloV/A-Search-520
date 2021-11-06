package Entity;

import Utility.Point;
import Utility.Sentiment;
import Utility.Terrain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Collection of {@link Entity.GridCell} instances representing the knowledge
 * base.
 */
public class Grid {
    private GridCell[] grid; // represent as flat array to make deep-copying easier
    private int xSize;
    private int ySize;


    /**
     * Constructs the grid with the specified parameters.
     * 
     * @param xSize       Width of grid
     * @param ySize       Height of grid
     */
    public Grid(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.grid = generateGrid(xSize, ySize);
    }

    /**
     * Copy constructor.
     * 
     * @param other Grid from which this grid is initialized.
     * @param deep  Whether the copy should be deep or shallow.
     */
    public Grid(Grid other, boolean deep) {
        this.xSize = other.getXSize();
        this.ySize = other.getYSize();
        this.grid = other.getGrid().clone();

        if (deep) {
            for (int i = 0; i < grid.length; i++) {
                this.grid[i] = (GridCell) other.grid[i].clone();
            }
        }
    }

    private boolean generateIsBlocked() {
        return Math.random() * 100 < 30;
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < xSize && y < ySize;
    }

    private GridCell[] setGoal(GridCell[] grid, ArrayList<Integer> freeCellIndexes){
        int location = (int) Math.random() * freeCellIndexes.size();
        int index = freeCellIndexes.get(location);
        grid[index].setGoal(true);
        return grid;
    }

    private GridCell[] generateGrid(int dimX, int dimY) {
        int size = dimX * dimY;
        GridCell[] grid = new GridCell[size];
        ArrayList<Integer> freeListIndexes = new ArrayList<>();
        for (int y = 0; y < dimY; y++) {
            for (int x = 0; x < dimX; x++) {
                int index = y * dimX + x;

                // determine if cell is blocked
                boolean isBlocked = (index == 0) ? false : generateIsBlocked();
                GridCell gc = new GridCell(x, y, false,  this);
                Terrain terrain = Terrain.Blocked;
                gc.setTerrain(terrain);
                gc.setProbGoal(1d/((double) size));
                if(!isBlocked){
                   double terrainType =  Math.random() * 90;
                   if (terrainType <= 90 ){terrain = Terrain.Forest;}
                   if (terrainType <= 60 ){ terrain = Terrain.Hilly;}
                   if (terrainType <= 30){ terrain = Terrain.Flat;}
                   gc.setTerrain(terrain);
                   freeListIndexes.add(index);
                }
                grid[index] = gc;
            }
        }
        return setGoal(grid, freeListIndexes);
    }

    /**
     * Retrieves the GridCell at (x, y) <strong>FOR READ-ONLY USAGE</strong>.
     * 
     * @param x x-coord of GridCell
     * @param y y-coord of GridCell
     * @return The GridCell instance
     */
    public GridCell getCell(int x, int y) {
        if (inBounds(x, y)) {
            return grid[y * getXSize() + x];
        }
        return null;
    }

    /**
     * Retrieves the GridCell at coord <strong>FOR READ-ONLY USAGE</strong>.
     * 
     * @param coord The coordinate of the GridCell
     * @return The GridCell instance
     */
    public GridCell getCell(Point coord) {
        return getCell(coord.f1, coord.f2);
    }

    /**
     * Retrieves the GridCell at coord <strong>FOR READING OR WRITING</strong>.
     * Checks if the GridCell actually belongs to this grid and if not, creates a
     * copy to preserve the original instance.
     * 
     * @param coord The coordinate of the GridCell
     * @return The GridCell instance
     */
    public GridCell setCell(Point coord) {
        GridCell cell = getCell(coord);
        if (cell != null && cell.getOwner() != this) { // the cell belongs to a different grid
            GridCell copy = cell.clone(); // clone it to avoid mangling the other grid
            copy.setOwner(this); // make this grid the new owner
            grid[coord.f2 * getXSize() + coord.f1] = copy;
        }
        return getCell(coord);
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

    public void forEachNeighbour(Point coord, Consumer<GridCell> action) {
        Arrays.stream(coord.get8Neighbours()).forEach(point -> {
            GridCell cell = setCell(point);
            if (cell != null)
                action.accept(cell);
        });
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
                    if (this.getCell(x, y).isBlocked()) {
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
