package de.jdsoft.strandet.Generator;


import android.util.Log;
import com.marcrh.graph.Point;
import de.jdsoft.strandet.Drawing.Tile;

import java.util.*;


public class Islands extends Generator {

    private float absoluteMaxHeight = 0;

    private int noOfTiles;
    private ArrayList<Tile> landTiles;
    private Random randomHeight = new Random();

    public Islands(int width, int height) {
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

        for( int i = random.nextInt(12)+5; i >= 0; i-- ) {
            generateBonusMountain(tiles, random.nextInt(7)+3, 0.3f);
        }

        generateBonusLakes(tiles);
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
                    neighbor.setSpecificType(Tile.BEACH);

                    landTiles.add(neighbor);

                    nextNexts.addLast(neighbor);
                }
            } else { // This is land, so the neighbor should be land to, but its heigher

                // Set new height
                float maxHeight = 0.f;
                for( Tile neighbor2 : tile.neighbors) {
                    maxHeight = Math.max(maxHeight, neighbor2.getHeight());
                }
                neighbor.setHeight( maxHeight + ( (float)(randomHeight.nextGaussian()*2.f) - 1.2f) );

                if( neighbor.getHeight() > absoluteMaxHeight ) {
                    absoluteMaxHeight = neighbor.getHeight();
                }

                // Make land!
                neighbor.setType(Tile.LAND);
                landTiles.add(neighbor);

                nextNexts.addLast(neighbor);
            }
        }

        nextNexts.addAll(nextNexts);
        return nextNexts;
    }

    private boolean first = true;
    private boolean isOcean(Tile tile) {
        if ( first ) {
            first = false;
            return false;
        }


        // probability to generate new island
        if( random.nextInt(1000) <= 8) {
            return false;
        }

        for( Tile neighbor : tile.neighbors) {
            if( neighbor.getType() == Tile.LAND ) {
                return false;
            }
        }

        return true;
    }


    private void generateBonusMountain(ArrayList<Tile> tiles, int mountainHeight, float decay) {
        float incHeight = mountainHeight;

        // Select random land tile
        Tile randomTile = null;
        for(int i = tiles.size()-1; i > 0; i--) {
            int rand = random.nextInt(tiles.size()-1);

            randomTile = tiles.get(rand);
            if( randomTile.getType() == Tile.LAND) {
                if( randomTile.getHeight() < getAbsoluteMaxHeight() - incHeight) {
                    break;
                }
            }
        }

        // No fitting tile found
        if( randomTile == null ) {
            return;
        }

        LinkedList<Tile> toVised = new LinkedList<Tile>();
        toVised.add(randomTile);
        toVised.addAll(randomTile.neighbors);

        HashSet<Tile> visited = new HashSet<Tile>();
        visited.add(randomTile);


        while(true) {
            LinkedList<Tile> toVisedNext = new LinkedList<Tile>();

            for( Tile neighbor : toVised ) {

                if( visited.contains(neighbor) ) {
                    continue;
                }

                // Mark as visited
                visited.add(neighbor);

                neighbor.incHeight(incHeight);
                toVisedNext.addAll(neighbor.neighbors);
                
                if(neighbor.getHeight() > getAbsoluteMaxHeight()) {
                    setAbsoluteMaxHeight(neighbor.getHeight());
                }
            }

            incHeight -= random.nextFloat() * decay;

            if(incHeight <= 0.) {
                break;
            }

            toVised = toVisedNext;
        }
    }

    private void generateBonusLakes( ArrayList<Tile> tiles ) {
        float lakeSize = 5;

        // Select "random" land tile
        Tile randomTile = null;
        for( Tile tile : tiles) {
            if( tile.getType() == Tile.WATER ) {
                continue;
            }

            boolean foundGood = true;
            for( Tile neighbor : tile.neighbors ) {
                if( neighbor.getType() == Tile.WATER || neighbor.getSpecificType() == Tile.BEACH ) {
                    foundGood = false;
                    break;
                }
            }
            // Take only maybe
            if( foundGood ) {
                randomTile = tile;
                break;
            }
        }

        // No fitting tile found
        if( randomTile == null ) {
            return;
        }

        float lakeHeight = randomTile.getHeight();

        LinkedList<Tile> toVised = new LinkedList<Tile>();
        toVised.add(randomTile);
        toVised.addAll(randomTile.neighbors);

        HashSet<Tile> visited = new HashSet<Tile>();
        //visited.add(randomTile);

        for(int i = random.nextInt(4)+1; i >= 0; i--) {
            LinkedList<Tile> toVisedNext = new LinkedList<Tile>();

            for( Tile neighbor : toVised ) {

                if( visited.contains(neighbor) ) {
                    continue;
                }

                if( neighbor.getType() == Tile.WATER || neighbor.getSpecificType() == Tile.BEACH ) {
                    continue;
                }

                if ( Math.abs(neighbor.getHeight() - lakeHeight) > 0.1f * absoluteMaxHeight ) {
                    continue;
                }

                // Mark as visited
                visited.add(neighbor);

                if( random.nextFloat() > 0.3f) {
                    toVisedNext.addAll(neighbor.neighbors);
                }

                neighbor.setType(Tile.WATER);
                neighbor.setSpecificType(Tile.LAKE);
            }

            toVised = toVisedNext;
        }
    }

    public ArrayList<Tile> getLandTiles() {
        return landTiles;
    }

    public float getAbsoluteMaxHeight() {
        return absoluteMaxHeight;
    }

    public void setAbsoluteMaxHeight(float absoluteMaxHeight) {
        this.absoluteMaxHeight = absoluteMaxHeight;
    }
}
