package de.jdsoft.stranded.map.planet.generator;


import de.jdsoft.stranded.map.Constants;
import de.jdsoft.stranded.map.planet.entity.Tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class Biome extends Generator implements Constants {

    // Wet | High
    static final public int[][] BIOME_MAP = {
            {Constants.SUBTROPICAL_DESERT, Constants.TEMPERATE_DESERT, Constants.TEMPERATE_DESERT, Constants.SCORCHED},
            {Constants.GRASSLAND, Constants.GRASSLAND, Constants.TEMPERATE_DESERT, Constants.BARE},
            {Constants.TROPICAL_SEASONAL_FOREST, Constants.GRASSLAND, Constants.SHRUBLAND, Constants.TUNDRA},
            {Constants.TROPICAL_SEASONAL_FOREST, Constants.TEMPERATE_DECIDUOUS_FOREST, Constants.SHRUBLAND, Constants.SNOW},
            {Constants.TROPICAL_RAIN_FOREST, Constants.TEMPERATE_DECIDUOUS_FOREST, Constants.TAIGA, Constants.SNOW},
            {Constants.TROPICAL_RAIN_FOREST, Constants.TEMPERATE_RAIN_FOREST, Constants.TAIGA, Constants.SNOW}
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
            tile.setBiome(BIOME_MAP[tile.getWet()][(int) Tile.getNormalizedHeight(tile.getHeight(), maxHeight)]);
        }
    }


    public void ComputeWet(ArrayList<Tile> tiles) {
        for( Tile tile : tiles ) {
            // TODO lakes!
            if( tile.getRiver() == null ) {
                continue;
            }

            random.setSeed(System.nanoTime());

            // Set start wet
            float currentWet = 3.5f;
            if( tile.isRiverSource() ) {
                currentWet = 5.9f;
            }
            tile.setWet((int)currentWet);

            HashSet<Tile> visited = new HashSet<Tile>();
            visited.add(tile);

            LinkedList<Tile> toVisit = new LinkedList<Tile>();
            toVisit.addAll(tile.neighbors);

            boolean end = false;
            while( !end ) {
                end = true;
                LinkedList<Tile> toVisitNext = new LinkedList<Tile>();

                for( Tile neighbor : toVisit ) {
                    // Test if already visited
                    if( visited.contains(neighbor) || neighbor.getType() == Tile.WATER ) {
                        continue;
                    }
                    // Mark as visited
                    visited.add(neighbor);

                    //Log.e("Strandet", "" + random.nextGaussian());
                    //if( random.nextInt(tile.getRiver().getPoints().size()*3) <= 1 ) {
                    //    currentWet -= Math.abs(random.nextGaussian()) / 30.f;
                    //}
                    currentWet -= random.nextFloat() / 20.f;

                    if( currentWet <= 0 ) {
                        break;
                    }

                    end = false;

                    if ( currentWet > neighbor.getWet() ) {
                        neighbor.setWet((int)currentWet);
                    }

                    toVisitNext.addAll(neighbor.neighbors);
                }
                toVisit = toVisitNext;
            }
        }
    }
}
