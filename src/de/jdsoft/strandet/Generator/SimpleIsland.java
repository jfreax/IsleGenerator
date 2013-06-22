package de.jdsoft.strandet.Generator;


import com.marcrh.graph.Point;
import de.jdsoft.strandet.Tile;

import java.util.ArrayList;
import java.util.LinkedList;


public class SimpleIsland extends Generator {


    public SimpleIsland(int width, int height) {
        super(width, height);
    }


    public void FillTypes(ArrayList<Tile> tiles) {

        LinkedList<Tile>nexts = new LinkedList<Tile>();

        for( Tile tile : tiles) {
            for( Point p : tile.getPoints() ) {
                if( p.x == 0 || p.y == 0 || p.x == width || p.y == height ) {
                    tile.setType(Tile.WATER);
                    tile.setDistance(0);
                    nexts.add(tile);
                }
            }
        }

        FillRecursive(nexts);

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
            nextNexts.addAll( changeType(tile) );
        }

        if( !nextNexts.isEmpty() ) {
            FillRecursive(nextNexts);
        }
    }

    private LinkedList<Tile> changeType(Tile tile) {
        LinkedList<Tile> nextNexts = new LinkedList<Tile>();
        LinkedList<Tile> nextNextsLast = new LinkedList<Tile>();

        for( Tile neighbor : tile.neighbors ) {

            // Only not visited neighbors
            if( neighbor.getType() != Tile.NONE ) {
                continue;
            }


            // This tile is water
            if( tile.getType() == Tile.WATER ) {

                // Maybe the neighbor should be water to?
                if( isOcean(neighbor, tile.getDistance())) {
                    neighbor.setType( Tile.WATER );
                    neighbor.setDistance(tile.getDistance() + 1);

                    nextNexts.addFirst(neighbor);

                } else { // No water, this is land
                    neighbor.setHeight(1);
                    neighbor.setType(Tile.LAND);

                    nextNexts.addLast(neighbor);
                }


            } else { // This is land, so the neighbor should be land to, but its heigher
                neighbor.setHeight( tile.getHeight() + 1 );
                neighbor.setType(Tile.LAND);

                nextNextsLast.addLast(neighbor);
            }

            //changeType(neighbor);
            //if( neighbor.getType() == Tile.NONE ) {

                // Maybe its an ocean tile?
//                if( isOcean(neighbor, tile.getDistance())) {
//                    neighbor.setType( Tile.WATER );
//                    neighbor.setDistance(tile.getDistance() + 1);
//                    changeType(neighbor);
//                    ret = true;
//                }
            //}
        }

        nextNexts.addAll(nextNextsLast);
        return nextNexts;
    }


    private boolean isOcean(Tile tile, int depth) {
        //for( Tile nei : tile.neighbors ) {
        //    if( nei.getType() == Tile.WATER ) {
                double w = 1.f / Math.log(depth + 3);
                if( random.nextFloat() < 1.f / Math.log(depth + 3) - 0.2f) {
                    return true;
                }
        //    }
        //}

        return false;
    }


    private boolean maybeStrand(Tile tile) {
        return false;
    }
}
