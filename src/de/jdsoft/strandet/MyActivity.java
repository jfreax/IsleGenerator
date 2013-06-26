package de.jdsoft.strandet;

import android.app.Activity;
import android.os.Bundle;
import com.marcrh.graph.Point;
import com.marcrh.graph.Range;
import com.marcrh.graph.Utils;
import com.marcrh.graph.delaunay.Voronoi;

public class MyActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

//        voronoi = new Voronoi();
//
//        int top = -canvas.getWidth()/2;
//        int left = -canvas.getHeight()/2;
//        int width = canvas.getWidth();
//        int height = canvas.getHeight();
//
//        Range r = new Range(new Point(top+10,left+10),new Point(top+width-10,left+height-10));
//        points = Utils.generateRandomPoints(nPoints.getValue(), r);
//        voronoi.generate(points, r);
    }
}
