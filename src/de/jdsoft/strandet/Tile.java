package de.jdsoft.strandet;


import android.graphics.*;
import com.marcrh.graph.Point;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Tile implements Comparable {

    public static final int NONE = 0;
    public static final int WATER = 1;
    public static final int LAND = 2;


    private List<Point> points;
    private int type = Tile.NONE;

    // approximately distance from border
    private int distance = 1;

    // Map height -> 0 is ocean level
    private int height = 0;

    private int color;
    public LinkedList<Tile> neighbors;

    public Tile(List<Point> points) {
        this.points = points;
        this.neighbors = new LinkedList<Tile>();

        setType(0);
    }

    public void draw(Canvas canvas){
        if(points.size()<2)
            return;

        Path path = new Path();
        path.moveTo((float)points.get(0).x, (float)points.get(0).y);

        for(int i=1;i<points.size();i++){
            path.lineTo((float)points.get(i).x, (float)points.get(i).y);
        }

        path.lineTo((float)points.get(0).x, (float)points.get(0).y);
        path.close();

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor( color );
        paint.setAntiAlias(true);

        canvas.drawPath(path, paint);
    }

    public List<Point> getPoints() {
        return points;
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
        switch (getHeight()) {
            case 1:
                color = Color.rgb(227, 232, 202);
                break;
            case 2:
                color = Color.rgb(207, 232, 182);
                break;
            default:
                color = Color.rgb(195, 212, 170);
                break;
        }
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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public int compareTo(Object another) {
        Tile anotherTile = (Tile)another;
        if( anotherTile.getType() == Tile.WATER ) {
            return 1;
        } else if ( anotherTile.getHeight() < getHeight()) {
            return 1;
        } else {
            return -1;
        }
    }
}
