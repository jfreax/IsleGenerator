package de.jdsoft.strandet.Drawing;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import com.marcrh.graph.Point;

import java.util.List;

public class River {
    private List<Point> points;

    public void draw(Canvas canvas) {
        if( points.size() < 2 )
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
        paint.setColor(Color.BLUE );
        paint.setAntiAlias(true);

        canvas.drawPath(path, paint);
    }
}
