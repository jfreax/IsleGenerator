package de.jdsoft.strandet.Drawing;


import android.graphics.*;
import com.marcrh.graph.Point;
import de.jdsoft.strandet.Constants;
import de.jdsoft.strandet.Generator.Biome;
import de.jdsoft.strandet.TileManager;

import java.util.LinkedList;
import java.util.List;

public class Tile implements Comparable, Constants {

    public static final int NONE = 0;
    public static final int WATER = 1;
    public static final int LAND = 2;


    private List<Point> points;

    private int type = Tile.NONE;
    private int wet = 0;
    private int biome = 0;

    private boolean isRiverSource = false;

    // approximately distance from border
    private int distance = 1;
    public int waterNeighbors = 0;

    // River on this tile
    private River river = null;

    // Map height -> 0 is ocean level
    private float height = 0;

    private int color;
    private boolean onBorder;
    public LinkedList<Tile> neighbors;

    public Tile(List<Point> points) {
        this.points = points;
        this.neighbors = new LinkedList<Tile>();

        setType(0);
    }

    public void draw(Canvas canvas, TileManager tileManager) {
        if( points.size() < 2 )
            return;


        Path path = new Path();
        path.moveTo((float)points.get(0).x, (float)points.get(0).y);

        for( int i = 1; i < points.size(); i++ ){
            path.lineTo((float) points.get(i).x, (float) points.get(i).y);
        }

        path.lineTo((float)points.get(0).x, (float)points.get(0).y);
        path.close();

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        //paint.setColor( color );
        paint.setColor(getColor(tileManager));
        //paint.setAntiAlias(true);
        //paint.setMaskFilter(new BlurMaskFilter(1, BlurMaskFilter.Blur.SOLID));

        canvas.drawPath(path, paint);
    }

    private int getColor(TileManager tileManager) {
        if( getType() == WATER ) {
            return Color.rgb(49, 58, 92);
        } else {
            return BIOME_COLOR[ Biome.BIOME_MAP[getWet()][(int)getNormalizedHeight(getHeight(), (int)tileManager.getMaxHeight())] ];
        }
    }


    public List<Point> getPoints() {
        return points;
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
        switch (type) {
            case NONE:
                color = Color.rgb(0, 0, 0);
                break;
            case WATER:
                color = Color.rgb(49, 58, 92);
                break;
            case LAND:
                setLandColor();
                break;
        }
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
        color = Color.rgb(10 + getBiome()*8, 20 + getBiome()*8, getBiome()*8);
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
        return isRiverSource;
    }

    public void isRiverSource(boolean riverSource) {
        isRiverSource = riverSource;
    }

    public int getBiome() {
        return biome;
    }

    public void setBiome(int biome) {
        this.biome = biome;
        setLandColor();
    }

    public static float getNormalizedHeight(float height, float maxHeight) {
        float norm = (float)Math.ceil( height * (3.f / maxHeight));

        return Math.min(norm, 3.0f);
    }
}
