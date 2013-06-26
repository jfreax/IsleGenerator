package de.jdsoft.stranded.Generator;


import de.jdsoft.stranded.Entity.Tile;

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
