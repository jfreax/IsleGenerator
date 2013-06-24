package de.jdsoft.strandet.Entity;


import android.graphics.*;
import com.marcrh.graph.Point;
import de.jdsoft.strandet.Constants;

import java.util.List;
import java.util.Random;

public class River implements Constants {
    protected List<Point> points;


    public River(List<Point> points) {
        this.points = points;
    }

    // Empty, do not forget to set points!
    public River() {
    }

    public void draw(Canvas canvas) {};


    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }
}
