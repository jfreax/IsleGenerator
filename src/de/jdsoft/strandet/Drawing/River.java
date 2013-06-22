package de.jdsoft.strandet.Drawing;


import android.graphics.*;
import com.marcrh.graph.Point;

import java.util.List;

public class River {
    private List<Point> points;


    public River(List<Point> points) {
        this.points = points;
    }

    // Empty, do not forget to set points!
    public River() {
    }

    public void draw(Canvas canvas) {
        if( points.size() < 2 )
            return;

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor( Color.rgb(49, 58, 92) );
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);


        for(int i = 0; i < points.size()-2; i++) {
            paint.setColor( Color.rgb(49 +(points.size()-i)*5, 58+(points.size()-i)*5, 92+(points.size()-i)*5) );
            Path path = new Path();
            path.moveTo((float)points.get(i).x, (float)points.get(i).y);
            path.lineTo((float)points.get(i+1).x, (float)points.get(i+1).y);
            path.lineTo((float)points.get(i+2).x, (float)points.get(i+2).y);
            canvas.drawPath(path, paint);
           // canvas.drawLine((float)points.get(i).x, (float)points.get(i).y, (float)points.get(i+1).x, (float)points.get(i+1).y, paint );
        }

/*        Path path = new Path();
        path.moveTo((float)points.get(0).x, (float)points.get(0).y);

        for(int i = 0; i < points.size(); i++){
            path.lineTo((float)points.get(i).x, (float)points.get(i).y);

        }


//        for(int i=points.size()-1;i >= 0;i--){
//            path.lineTo((float)points.get(i).x, (float)points.get(i).y);
//        }

        //path.lineTo((float)points.get(0).x, (float)points.get(0).y);
        //path.close();

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor( Color.rgb(49, 58, 92) );
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);


        paint.setShader(new LinearGradient((float)points.get(0).x, (float)points.get(0).y, (float)points.get(points.size()-1).x, (float)points.get(points.size()-1).y, Color.rgb(89, 98, 132) , Color.rgb(49, 58, 92) , Shader.TileMode.CLAMP));

        canvas.drawPath(path, paint);*/
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }
}
