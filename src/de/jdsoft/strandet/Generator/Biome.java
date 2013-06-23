package de.jdsoft.strandet.Generator;


import de.jdsoft.strandet.Constants;
import de.jdsoft.strandet.Drawing.Tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class Biome extends Generator implements Constants {

    // Wet | High
    static final public int[][] BIOME_MAP = {
            {SUBTROPICAL_DESERT, TEMPERATE_DESERT, TEMPERATE_DESERT, SCORCHED},
            {GRASSLAND, GRASSLAND, TEMPERATE_DESERT, BARE},
            {TROPICAL_SEASONAL_FOREST, GRASSLAND, SHRUBLAND, TUNDRA},
            {TROPICAL_SEASONAL_FOREST, TEMPERATE_DECIDUOUS_FOREST, SHRUBLAND, SNOW},
            {TROPICAL_RAIN_FOREST, TEMPERATE_DECIDUOUS_FOREST, TAIGA, SNOW},
            {TROPICAL_RAIN_FOREST, TEMPERATE_RAIN_FOREST, TAIGA, SNOW}
    };


    private Random random = new Random();
    private float maxHeight;

    public Biome(int width, int height, float maxHeight) {
        super(width, height);

        this.maxHeight = maxHeight;
    }

    @Override
    public void Compute(ArrayList<Tile> tiles) {
        ComputeWet(tiles);

        for( Tile tile : tiles ) {
            tile.setBiome(BIOME_MAP[tile.getWet()][(int)Tile.getNormalizedHeight(tile.getHeight(), maxHeight)]);
        }
    }


    public void ComputeWet(ArrayList<Tile> tiles) {
        for( Tile tile : tiles ) {
            // TODO lakes!
            if( !tile.isRiverSource() ) {
                continue;
            }

            random.setSeed(System.nanoTime());

            int currentWet = 5;
            tile.setWet(currentWet);

            HashSet<Tile> visited = new HashSet<Tile>();
            visited.add(tile);

            LinkedList<Tile> toVisit = new LinkedList<Tile>();
            LinkedList<Tile> toVisitNext = new LinkedList<Tile>();

            toVisit.addAll(tile.neighbors);

            boolean end = false;
            while( !end ) {
                end = true;

                for( Tile neighbor : toVisit ) {
                    // Test if already visited
                    if( visited.contains(neighbor) || neighbor.getType() == Tile.WATER ) {
                        continue;
                    }
                    // Mark as visited
                    visited.add(neighbor);

                    if( random.nextInt(tile.getRiver().getPoints().size()*2) <= 1 ) {
                        currentWet -= 1;
                    }

                    if( currentWet == 0 ) {
                        break;
                    }

                    end = false;
                    if ( currentWet > neighbor.getWet() ) {
                        neighbor.setWet(currentWet);
                    } else {
                        neighbor.setWet(neighbor.getWet()+1);
                    }
                    toVisitNext.addAll(neighbor.neighbors);

                }
                toVisit = (LinkedList<Tile>)toVisitNext.clone();
            }
        }
    }
}
