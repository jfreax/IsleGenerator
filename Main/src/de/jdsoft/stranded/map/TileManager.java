package de.jdsoft.stranded.map;


import com.marcrh.graph.Point;
import com.marcrh.graph.Range;
import com.marcrh.graph.Utils;
import com.marcrh.graph.delaunay.Region;
import com.marcrh.graph.delaunay.Voronoi;
import de.jdsoft.stranded.map.generator.Rivers;
import de.jdsoft.stranded.map.entity.River;
import de.jdsoft.stranded.map.entity.Tile;
import de.jdsoft.stranded.map.generator.Biome;
import de.jdsoft.stranded.map.generator.Islands;

import java.util.ArrayList;
import java.util.HashMap;

public class TileManager {

    private Voronoi voronoi;
    private ArrayList<Point> points;

    private ArrayList<Tile> tiles;

    private int width;
    private int height;
    private ArrayList<Tile> landTiles;
    private ArrayList<River> rivers;
    private float maxHeight;

    public float maxHeightDifference;

    public TileManager(int width, int height) {
        this.width = width;
        this.height = height;

        voronoi = new Voronoi();

        int top = -0;
        int left = 0;

        Range r = new Range(new Point(top,left),new Point(top+width, left+height));
        points = Utils.generateRandomPoints(15000, r);
        voronoi.generate(points, r);

        Initialize();
    }

    private void Initialize() {
        tiles = new ArrayList<Tile>();

        for( int i=0; i < points.size(); i++ ) {
            Region r = voronoi.getRegion(i);

            Tile tile = new Tile(r);
            tiles.add(tile);
        }
//        voronoi.getRegion(1).getEdgePoints(0)
//
//        List<Triad> triads = voronoi.getTriads();
//        for (Triad triad : triads) {
//            List<Point> p = new ArrayList<Point>();
//            p.add(points.get(triad.a));
//            p.add(points.get(triad.b));
//            p.add(points.get(triad.c));
//
//            Tile tile = new Tile(p);
//            tiles.add(tile);
//        }

        // Find all neighbors
        HashMap<Point, Tile> nei = new HashMap<Point, Tile>();
        for( Tile tile : tiles) {
//            for( int i = tile.getRegion().getSize()-1; i >= 0; i-- ) {
//                Point p = tile.getRegion().getPoint(i);
//                Point p2 = tile.getRegion().getPoint(i+1 > tile.getRegion().getSize()-1 ? 0 : i+1);
//                if( nei.containsKey(p)) {
//                    if( nei.containsKey(p2) ) {
//                        if( nei.get(p) == nei.get(p2)) {
//                    nei.get(p).neighbors.add(tile);
//                    tile.neighbors.add(nei.get(p));
//                        }
//                    } else {
//                        nei.put(p, tile);
//                    }
//                } else {
//                    nei.put(p, tile);
//                }
//            }
            for( Point p : tile.getPoints()) {
                if( nei.containsKey(p)) {
                    nei.get(p).neighbors.add(tile);
                    tile.neighbors.add(nei.get(p));
                } else {
                    nei.put(p, tile);
                }
            }
        }

        Islands generator = new Islands(width, height);
        generator.Compute(tiles);
        landTiles = generator.getLandTiles();
        maxHeight = generator.getAbsoluteMaxHeight();
        maxHeightDifference = generator.maxHeightDifference;

        Rivers riverMaker = new Rivers(width, height);
        riverMaker.Compute(landTiles);
        rivers = riverMaker.getRivers();

        Biome biome = new Biome(width, height, maxHeight);
        biome.Compute(landTiles);

/*        ArrayList<Triad> triads = voronoi.getTriads();
        for( Triad tr : triads ) {
            Triad neighbor1 = tr.getNeighbour( triads, Triad.TR_EDGE_AB );
            Triad neighbor2 = tr.getNeighbour( triads, Triad.TR_EDGE_BC );
            Triad neighbor3 = tr.getNeighbour( triads, Triad.TR_EDGE_AC );
            float mean = (float) (tr.z + neighbor1.z + neighbor2.z + neighbor3.z) / 4.f;

            tr.z = mean;
        }*/
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public ArrayList<River> getRivers() {
        return rivers;
    }

    public float getMaxHeight() {
        return maxHeight;
    }
}
