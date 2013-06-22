package de.jdsoft.strandet;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import com.marcrh.graph.Point;

import java.util.ArrayList;
import java.util.List;

public class Tile {

    public static final int WATER = 0;
    public static final int LAND = 1;
    public static final int COAST = 2;


    private List<Point> points;
    private int type;
    private int color;

    public ArrayList<Tile> neighbors;

    public Tile(List<Point> points) {
        this.points = points;
        this.neighbors = new ArrayList<Tile>();

        setType(1);
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

        canvas.drawPath(path, paint);
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setType(int type) {
        this.type = type;
        switch (type) {
            case WATER:
                color = Color.rgb(49, 58, 92);
                break;
            case LAND:
                color = Color.rgb(195, 212, 170);
                break;
            case COAST:
                color = Color.rgb(227, 232, 202);
                break;
        }
    }

    public int getType() {
        return type;
    }

}
