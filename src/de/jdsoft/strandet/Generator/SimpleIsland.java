package de.jdsoft.strandet.Generator;


import com.marcrh.graph.Point;
import de.jdsoft.strandet.Drawing.Tile;
import de.jdsoft.strandet.TileManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;


public class SimpleIsland extends Generator {

    private int noOfTiles;
    private ArrayList<Tile> landTiles;

    public SimpleIsland(int width, int height) {
        super(width, height);

        landTiles = new ArrayList<Tile>();
    }


    public void Compute(ArrayList<Tile> tiles) {
        noOfTiles = tiles.size();

        LinkedList<Tile>nexts = new LinkedList<Tile>();

        for( Tile tile : tiles) {
            for( Point p : tile.getPoints() ) {
                if( p.x == 0 || p.y == 0 || p.x == width || p.y == height ) {
                    tile.isOnBorder(true);
                    tile.setType(Tile.WATER);
                    tile.setDistance(0);
                    nexts.add(tile);
                }
            }
        }

        FillRecursive(nexts);
    }


    private void FillRecursive(LinkedList<Tile> nexts ) {
        LinkedList<Tile> nextNexts = new LinkedList<Tile>();

        for( Tile tile : nexts) {
            nextNexts.addAll(changeType(tile));
        }

        Collections.sort(nextNexts);
        if( !nextNexts.isEmpty() ) {
            FillRecursive(nextNexts);
        }
    }


    private LinkedList<Tile> changeType(Tile tile) {
        LinkedList<Tile> nextNexts = new LinkedList<Tile>();

        for( Tile neighbor : tile.neighbors ) {

            if( tile.getType() == Tile.WATER ) {
                neighbor.waterNeighbors++;
            }

            // Only not visited neighbors
            if( neighbor.getType() != Tile.NONE ) {
                continue;
            }

            // This tile is water
            if( tile.getType() == Tile.WATER ) {

                // Maybe the neighbor should be water to?
                if( isOcean(neighbor)) {
                    neighbor.setType( Tile.WATER );
                    neighbor.setDistance(tile.getDistance() + 1);

                    nextNexts.addFirst(neighbor);

                } else { // No water, this is land
                    neighbor.setType(Tile.LAND);
                    landTiles.add(neighbor);

                    nextNexts.addLast(neighbor);
                }
            } else { // This is land, so the neighbor should be land to, but its heigher

                // Set new height
                int maxHeight = 0;
                for( Tile neighbor2 : tile.neighbors) {
                    maxHeight = Math.max(maxHeight, neighbor2.getHeight());
                }
                neighbor.setHeight( maxHeight + ((int)(random.nextGaussian()*2) -1) );

                // Make land!
                neighbor.setType(Tile.LAND);
                landTiles.add(neighbor);

                nextNexts.addLast(neighbor);
            }
        }

        nextNexts.addAll(nextNexts);
        return nextNexts;
    }


    private boolean isOcean(Tile tile) {
        // probability to generate new island
        if ( (float)random.nextInt(noOfTiles) / (float)noOfTiles <= 0.005f ) {
            return false;
        }

        for( Tile neighbor : tile.neighbors) {
            if( neighbor.getType() == Tile.LAND ) {

                return (float)random.nextInt(noOfTiles) / (float)noOfTiles <= 0.1f;
            }
        }

        return true;
    }

    public ArrayList<Tile> getLandTiles() {
        return landTiles;
    }

}
