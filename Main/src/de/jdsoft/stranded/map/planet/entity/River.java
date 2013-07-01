package de.jdsoft.stranded.map.planet.entity;


import com.marcrh.graph.Point;
import de.jdsoft.stranded.map.Constants;

import java.util.List;

public class River implements Constants {
    protected List<Point> points;


    public River(List<Point> points) {
        this.points = points;
    }

    // Empty, do not forget to set points!
    public River() {
    }



    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }
}
