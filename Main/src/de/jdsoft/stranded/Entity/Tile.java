package de.jdsoft.stranded.Entity;


import com.badlogic.gdx.graphics.Color;
import com.marcrh.graph.Point;
import com.marcrh.graph.delaunay.Region;
import de.jdsoft.stranded.Generator.Biome;
import de.jdsoft.stranded.Map.TileManager;
import de.jdsoft.stranded.Constants;

import java.util.*;

public class Tile implements Comparable, Constants {

    public static final int NONE = 0;
    public static final int WATER = 1;
    public static final int LAND = 2;
    protected int type = Tile.NONE;

    public static final int RIVER_SOURCE = 5;
    public static final int LAKE = 6;
    public static final int BEACH = 7;
    protected int specificType = Tile.NONE;

    protected List<Point> points;
    protected Region region;
    protected int wet = 0;
    protected int biome = 0;

    public float goesDownFrom = 0.f;
    public float goesUpTo = 0.f;


    // approximately distance from border
    protected int distance = 1;
    public int waterNeighbors = 0;

    // River on this tile
    private River river = null;

    // Map height -> 0 is ocean level
    private float height = 0;

    //private int color;
    private boolean onBorder;
    public LinkedList<Tile> neighbors;

    public Tile(Region region) {
        this.region = region;
        this.points = region.getPoints();
        this.neighbors = new LinkedList<Tile>();

        setType(0);
    }

    public int getColor(TileManager tileManager) {
        if( getType() == WATER ) {
            if( isLake() ) {
                return Constants.LAKE_COLOR;
            } else {
                //return Color.rgb(49, 58, 92);
                return (49 << 24) | (58 << 16) | (92 << 8) | 255;
            }
        } else { // LAND
            if( getSpecificType() == BEACH) {
                return Constants.BEACH_COLOR;
            } else {
                int normHeight = (int)getNormalizedHeight(getHeight(), tileManager.getMaxHeight());
                int wet = getWet();
    //            if(normHeight > 2 ) {
    //                Log.e("Strandet", ""+normHeight + "\t - " + getNormalizedHeight(getHeight(), tileManager.getMaxHeight()));
    //            }
                //return Color.rgb(10 + normHeight*15, 20 + normHeight*15, normHeight*15);
                //return  ((10 + normHeight*15) << 24) | ((20 + normHeight*15) << 16) | ((normHeight*15) << 8) | 255;
                //return Color.rgba8888(10 + normHeight*15, 20 + normHeight*15, normHeight*15, 255);
                //return Color.rgb(10 + getWet()*30, 20 + getWet()*30, getWet()*30);
                //return colorJitter(BIOME_COLOR[ Biome.BIOME_MAP[getWet()][(int)getNormalizedHeight(getHeight(), (int)tileManager.getMaxHeight())] ]);
                int biomeID = Biome.BIOME_MAP[ wet ][ normHeight ];
                return BIOME_COLOR[ biomeID ];
            }
        }
    }

    protected int colorJitter(int color) {
        Random random = new Random();
        color += (random.nextInt(10)-5) << 16;
        color += (random.nextInt(10)-5) << 8;
        color += random.nextInt(10)-5;
        return color;
    }

/*    protected int colorDarker(int color, float factor) {
        float[] hsv = new float[3];
        Color.RGBToHSV(Color.red(color), Color.green(color), Color.blue(color), hsv);

        hsv[0] *= (1+factor);

        //return Color.HSVToColor(hsv);
        return Color.rgb((int)(Color.red(color) + factor), (int)(Color.green(color) + factor), (int)(Color.blue(color) + factor));
    }*/


    public List<Point> getPoints() {
        return points;
    }

    public float[] getVertices() {
        float[] vertices = new float[getPoints().size() * 3];

        int i = 0;
        for( Point p : getPoints()) {
            vertices[i++] = (float)p.x;
            vertices[i++] = (float)p.y;
            vertices[i++] = (float)p.x;
        }

        return vertices;
    }


    public Point getPosition() {
        long gesX = 0L;
        long gesY = 0L;
        for( Point p : points ) {
            gesX += p.x;
            gesY += p.y;
        }
        return new Point(gesX / points.size(), gesY / points.size(), 0);
    }


    public void setType(int type) {
        this.type = type;
//        switch (type) {
//            case NONE:
//                color = Color.rgb(0, 0, 0);
//                break;
//            case WATER:
//                color = Color.rgb(49, 58, 92);
//                break;
//            case LAND:
//                setLandColor();
//                break;
//        }
    }

    private void setLandColor() {
//        switch (getHeight()) {
//            case 1:
//                color = Color.rgb(227, 232, 202);
//                break;
//            case 2:
//                color = Color.rgb(207, 232, 182);
//                break;
//            default:
//                color = Color.rgb(195, 212, 170);
//                break;
//        }

        //color = Color.rgb(10 + (int)getHeight()*3, 20 + (int)getHeight()*3, (int)getHeight()*3);
        //color = Color.rgb(10 + getWet()*3, 20 + getWet()*3, getWet()*3);
        //color = Color.rgb(10 + getBiome()*8, 20 + getBiome()*8, getBiome()*8);
    }

    public int getType() {
        return type;
    }

    public void setDistance(int depth) {
        if( depth < 1 )
            this.distance = 1;
        this.distance = depth;
    }

    public int getDistance() {
        return distance;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = Math.max(height, 0.f);
    }


    public int compareTo(Object another) {
        Tile anotherTile = (Tile)another;
        if( anotherTile.getType() == Tile.WATER ) {
            if ( getType() == Tile.WATER) {
                return 0;
            } else {
                return 1;
            }
        } if ( getType() == Tile.WATER) {
            return -1;
        }

        if ( anotherTile.getHeight() > getHeight()) {
            return 1;
        } else if ( anotherTile.getHeight() == getHeight()) {
            return 0;
        } else {
            return -1;
        }
    }

    public boolean isOnBorder() {
        return onBorder;
    }

    public void isOnBorder(boolean b) {
        onBorder = b;
    }

    public River getRiver() {
        return river;
    }

    public void setRiver(River river) {
        this.river = river;
    }

    public int getWet() {
        return wet;
    }

    public void setWet(int wet) {
        this.wet = wet < 0 ? 0 : wet;
        this.wet = this.wet > 5 ? 5 : this.wet;
        setLandColor();
    }

    public boolean isRiverSource() {
        return getSpecificType() == RIVER_SOURCE;
    }

    public int getBiome() {
        return biome;
    }

    public void setBiome(int biome) {
        this.biome = biome;
        setLandColor();
    }

    public static float getNormalizedHeight(float height, float maxHeight) {
        //float norm = (float)Math.ceil( height * (3.f / maxHeight));
        float norm = (height / maxHeight) * 3.9f;

        return Math.min(norm, 3.9f);
    }

    public void incHeight(float i) {
        setHeight(getHeight()+i);
    }

    public boolean isLake() {
        return getSpecificType() == LAKE;
    }

    public int getSpecificType() {
        return specificType;
    }

    public void setSpecificType(int specificType) {
        this.specificType = specificType;
    }

    public Region getRegion() {
        return region;
    }
}
