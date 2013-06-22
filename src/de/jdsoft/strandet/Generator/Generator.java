package de.jdsoft.strandet.Generator;


import de.jdsoft.strandet.Drawing.Tile;
import de.jdsoft.strandet.TileManager;

import java.util.ArrayList;
import java.util.Random;

abstract public class Generator {
    protected Random random = new Random();

    protected int width;
    protected int height;

    public Generator(int width, int height) {
        this.width = width;
        this.height = height;
    }

    abstract public void Compute(ArrayList<Tile> tiles);
}
