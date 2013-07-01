package de.jdsoft.stranded.map.planet.generator;


import de.jdsoft.stranded.map.planet.entity.Tile;

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
