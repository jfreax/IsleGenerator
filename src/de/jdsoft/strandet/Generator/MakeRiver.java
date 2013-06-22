package de.jdsoft.strandet.Generator;


import android.util.Log;
import com.marcrh.graph.Point;
import de.jdsoft.strandet.Drawing.River;
import de.jdsoft.strandet.Drawing.Tile;

import java.util.*;

public class MakeRiver extends Generator {

    private int noOfTiles;

    private ArrayList<River> rivers;
    private HashSet<Tile> visited;

    public MakeRiver(int width, int height) {
        super(width, height);

        rivers = new ArrayList<River>();
    }

    @Override
    public void Compute(ArrayList<Tile> tiles) {
        noOfTiles = tiles.size();

        Collections.shuffle(tiles);
        for( int i = random.nextInt(25)+15; i >= 0; i--) {
            beginNewRiver(tiles.get(i));
        }

//        for( Tile tile : tiles ) {
//            if( (float)random.nextInt(1000) / (float)noOfTiles <= 0.02f ) {
//                beginNewRiver(tile);
//            }
//        }
    }

    private boolean beginNewRiver(Tile tile) {
        if( tile.neighbors.isEmpty() ) {
            return false;
        }

        River river = new River();

        // River source
        List<Point> points = new LinkedList<Point>();
        points.add(tile.getPosition());

        visited = new HashSet<Tile>();
        visited.add(tile);

        int riverWidth = 0;

        // Add rest of river
        // extendRiver(tile, points);
        while(true) {
            int minHeight = Integer.MAX_VALUE;
            Tile minTile = null;
            ArrayList<Tile> tilesWithNewRiver = new ArrayList<Tile>();

            for( Tile neighbor : tile.neighbors) {

                // Test if already visited
                if( visited.contains(neighbor) ) {
                    continue;
                }
                // Mark as visited
                visited.add(neighbor);

                // Is this the end of the river?
                if ( neighbor.getType() == Tile.WATER || neighbor.getRiver() != null ) {
                    // Ignore to short rivers
                    if( points.size() <= 5 ) {
                        return false;
                    }
                    points.add(neighbor.getPosition());

                    // Add river to list
                    river.setPoints(points);
                    rivers.add(river);

                    // Register river on all tiles
                    for( Tile tileRiver : tilesWithNewRiver ) {
                        tileRiver.setRiver(river);
                    }
                    return true;
                }


                if( neighbor.getHeight() < minHeight ) {
                    minHeight = neighbor.getHeight();
                    minTile = neighbor;
                }
            }

            if( minTile != null ) {
                // Save river info on tile
                tilesWithNewRiver.add(minTile);

                // Make river wider
                riverWidth++;

                Point p = new Point(minTile.getPosition().x, minTile.getPosition().y, riverWidth);
                points.add(p);
                tile = minTile;

            } else {
                break;
            }
        }

        return false;
        //rivers.add(new River(points));
    }

    private void extendRiver(Tile tile, List<Point> points) {
        int minHeight = Integer.MAX_VALUE;
        Tile minTile = null;
        for( Tile neighbor : tile.neighbors) {

            if( visited.contains(neighbor) ) {
                continue;
            }

            if ( neighbor.getType() == Tile.WATER ) {
                return;
            }

            if( neighbor.getHeight() < minHeight ) {
                minHeight = neighbor.getHeight();
                minTile = neighbor;
            }
        }

        if( minTile != null ) {
            points.add(minTile.getPosition());
            extendRiver(minTile, points);
        }
    }


    public ArrayList<River> getRivers() {
        return rivers;
    }
}
