package de.jdsoft.strandet.Generator;


import com.marcrh.graph.Point;
import de.jdsoft.strandet.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;


public class SimpleIsland extends Generator {


    public SimpleIsland(int width, int height, int noOfTiles) {
        super(width, height, noOfTiles);
    }


    public void FillTypes(ArrayList<Tile> tiles) {

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

        // Destroy mini islands and mini lakes
//        for( Tile tile : tiles) {
//////            int waterNeighbors = 0;
//////            int notWaterNeighbors = 0;
//////            for( Tile neighbor : tile.neighbors) {
//////                if( neighbor.getType() != Tile.WATER) {
//////                    notWaterNeighbors++;
//////                } else {
//////                    waterNeighbors++;
//////                }
//////            }
//////            if ( notWaterNeighbors <= 3 ) {
//////                tile.setType(Tile.WATER);
//////            }
//////            if ( !tile.isOnBorder() && waterNeighbors <= 2 ) {
//////                tile.setType(Tile.LAND);
//////            }
////
//            if ( tile.waterNeighbors  >=  ) {
//                tile.setType(Tile.WATER);
//            }
//        }

//            if( tile.getType() == Tile.WATER) {
//                for( Tile neighbor : tile.neighbors) {
//                    if( neighbor.getType() != Tile.WATER) {
//                        if( isOcean(tile)) {
//                            neighbor.setType( Tile.WATER );
//                        } else if( maybeStrand(tile) ) {
//
//                        }
//                    }
//                }
//            }
        //}
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
                    //neighbor.setHeight(tile.getHeight() + 1);

                    nextNexts.addFirst(neighbor);

                } else { // No water, this is land
                    //neighbor.setHeight(tile.getHeight() + 1);
                    neighbor.setType(Tile.LAND);

                    nextNexts.addLast(neighbor);
                }


            } else { // This is land, so the neighbor should be land to, but its heigher
                int maxHight = 0;
                for( Tile neighbor2 : tile.neighbors) {
                    maxHight = Math.max(maxHight, neighbor2.getHeight());
                }
                neighbor.setHeight( maxHight + 1 );
                neighbor.setType(Tile.LAND);

                nextNexts.addLast(neighbor);
            }
        }

        nextNexts.addAll(nextNexts);
        return nextNexts;
    }


    private boolean isOcean(Tile tile) {

        //if ( random.nextFloat() > 0.996f) {

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


    private boolean maybeStrand(Tile tile) {
        return false;
    }
}
