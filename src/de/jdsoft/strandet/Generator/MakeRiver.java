package de.jdsoft.strandet.Generator;


import de.jdsoft.strandet.Drawing.River;
import de.jdsoft.strandet.Drawing.Tile;

import java.util.ArrayList;

public class MakeRiver extends Generator {

    private ArrayList<River> rivers;

    public MakeRiver(int width, int height) {
        super(width, height);

        rivers = new ArrayList<River>();
    }

    @Override
    public void Compute(ArrayList<Tile> tiles) {

    }

    public ArrayList<River> getRivers() {
        return rivers;
    }
}
