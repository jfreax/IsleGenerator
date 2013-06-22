package de.jdsoft.strandet.Generator;


import de.jdsoft.strandet.Tile;

import java.util.ArrayList;
import java.util.Random;

abstract public class Generator {
    protected Random random = new Random();


    protected int width;
    protected int height;
    protected int noOfTiles;

    public Generator(int width, int height, int noOfTiles) {
        this.width = width;
        this.height = height;
        this.noOfTiles = noOfTiles;
    }

    abstract public void FillTypes(ArrayList<Tile> tiles);
}
