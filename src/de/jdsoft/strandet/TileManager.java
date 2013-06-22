package de.jdsoft.strandet;


import com.marcrh.graph.Point;
import com.marcrh.graph.Range;
import com.marcrh.graph.Utils;
import com.marcrh.graph.delaunay.Voronoi;
import de.jdsoft.strandet.Drawing.River;
import de.jdsoft.strandet.Drawing.Tile;
import de.jdsoft.strandet.Generator.Islands;
import de.jdsoft.strandet.Generator.Rivers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TileManager {

    private Voronoi voronoi;
    private ArrayList<Point> points;

    private ArrayList<Tile> tiles;

    private int width;
    private int height;
    private ArrayList<Tile> landTiles;
    private ArrayList<River> rivers;

    public TileManager(int width, int height) {
        this.width = width;
        this.height = height;

        voronoi = new Voronoi();

        int top = 0;
        int left = 0;

        Range r = new Range(new Point(top,left),new Point(top+width, left+height));
        points = Utils.generateRandomPoints(5000, r);
        voronoi.generate(points, r);

        Initialize();
    }

    private void Initialize() {
        tiles = new ArrayList<Tile>();

        for( int i=0; i < points.size(); i++ ) {
            List<Point> p = voronoi.getRegion(i).getPoints();

            Tile tile = new Tile(p);
            tiles.add(tile);
        }

        // Find all neighbors
        HashMap<Point, Tile> nei = new HashMap<Point, Tile>();
        for( Tile tile : tiles) {
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

        Rivers riverMaker = new Rivers(width, height);
        riverMaker.Compute(landTiles);
        rivers = riverMaker.getRivers();
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public ArrayList<River> getRivers() {
        return rivers;
    }
}
