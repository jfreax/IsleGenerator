package de.jdsoft.strandet.Generator;


import com.marcrh.graph.Point;
import de.jdsoft.strandet.Tile;

import java.util.ArrayList;
import java.util.LinkedList;


public class SimpleIsland extends Generator {

    public SimpleIsland(int width, int height) {
        super(width, height);
    }

    @Override
    public void FillTypes(ArrayList<Tile> tiles) {

        LinkedList<Tile> next = new LinkedList<Tile>();

        for( Tile tile : tiles) {
            for( Point p : tile.getPoints() ) {
                if( p.x == 0 || p.y == 0 || p.x == width || p.y == height ) {
                    tile.setType(Tile.WATER);
                    tile.setDistance(0);
                    next.add(tile);
                }
            }
        }

        for( Tile tile : next) {
            if( changeType(tile) ) {
                break;
            }
        }
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

    private boolean changeType(Tile tile) {
        boolean ret = false;

        for( Tile neighbor : tile.neighbors ) {
            if( neighbor.getType() != Tile.NONE ) {
                continue;
            }

            // This tile is water
            if( tile.getType() == Tile.WATER ) {

                // Maybe the neighbor should be water to?
                if( isOcean(neighbor, tile.getDistance())) {
                    neighbor.setType( Tile.WATER );
                    neighbor.setDistance(tile.getDistance() + 1);
                    ret = true;
                } else { // No water, this is land
                    neighbor.setHeight(1);
                    neighbor.setType(Tile.LAND);
                    ret = true;
                }


            } else { // This is land, so the neighbor should be land to, but its heigher
                neighbor.setHeight( tile.getHeight() + 1 );
                neighbor.setType(Tile.LAND);
            }

            changeType(neighbor);
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

        return ret;
    }


    private boolean isOcean(Tile tile, int depth) {
        //for( Tile nei : tile.neighbors ) {
        //    if( nei.getType() == Tile.WATER ) {
                double w = 1.f / Math.log(depth + 3);
                if( random.nextFloat() < 1.f / depth) {
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
