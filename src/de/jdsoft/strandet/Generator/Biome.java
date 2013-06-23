package de.jdsoft.strandet.Generator;


import de.jdsoft.strandet.Drawing.Tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class Biome extends Generator {

    static final int SNOW = 0;
    static final int TUNDRA = 1;
    static final int BARE = 2;
    static final int SCORCHED = 3;
    static final int TAIGA = 4;
    static final int SHRUBLAND = 5;
    static final int TEMPERATE_RAIN_FOREST = 6;
    static final int TEMPERATE_DECIDUOUS_FOREST = 7;
    static final int GRASSLAND = 8;
    static final int TEMPERATE_DESERT = 9;
    static final int TROPICAL_RAIN_FOREST = 10;
    static final int TROPICAL_SEASONAL_FOREST = 11;
    static final int SUBTROPICAL_DESERT = 12;

    // Wet | High
    static final int[][] BIOME_MAP = {
            {SUBTROPICAL_DESERT, TEMPERATE_DESERT, TEMPERATE_DESERT, SCORCHED},
            {GRASSLAND, GRASSLAND, TEMPERATE_DESERT, BARE},
            {TROPICAL_SEASONAL_FOREST, GRASSLAND, SHRUBLAND, TUNDRA},
            {TROPICAL_SEASONAL_FOREST, TEMPERATE_DECIDUOUS_FOREST, SHRUBLAND, SNOW},
            {TROPICAL_RAIN_FOREST, TEMPERATE_DECIDUOUS_FOREST, TAIGA, SNOW},
            {TROPICAL_RAIN_FOREST, TEMPERATE_RAIN_FOREST, TAIGA, SNOW}
    };


    private Random random = new Random();
    private int maxHeight;

    public Biome(int width, int height, int maxHeight) {
        super(width, height);

        this.maxHeight = maxHeight;
    }

    @Override
    public void Compute(ArrayList<Tile> tiles) {
        ComputeWet(tiles);

        for( Tile tile : tiles ) {
            int normHeight = (int)Math.ceil( (float) tile.getHeight() * (3.f / (float) maxHeight));
            normHeight = normHeight > 3 ? 3 : normHeight;
            tile.setBiome(BIOME_MAP[tile.getWet()][normHeight]);
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

                    if( random.nextInt(tile.getRiver().getPoints().size()*5) <= 1 ) {
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
