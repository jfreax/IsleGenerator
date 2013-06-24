package de.jdsoft.strandet.Render;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import com.marcrh.graph.Point;
import de.jdsoft.strandet.Entity.River;
import de.jdsoft.strandet.Map.TileManager;

import java.util.List;
import java.util.Random;

public class RenderRiver {
    static public void draw(Canvas canvas, TileManager tileManager, River river) {
        List<Point> points = river.getPoints();

        if( points.size() < 2 )
            return;

        Random random = new Random();

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor( river.RIVER_COLOR );
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);

        //paint.setMaskFilter(new BlurMaskFilter(1, BlurMaskFilter.Blur.SOLID));



        //paint.setPathEffect(new CornerPathEffect(10) );
//        Path path = new Path();
//        path.moveTo((float)points.get(points.size()-1).x, (float)points.get(points.size()-1).y);
//        for(int i = points.size()-1; i > 1; i -= 2) {
//
//            //paint.setColor( Color.rgb(49 +(points.size()-i+2)*2, 58+(points.size()-i+2)*2, 92+(points.size()-i+2)*2) );
//            paint.setStrokeWidth((float)Math.ceil(points.get(i).z / 6.f) + 1.f); // TODO divide with maximum river length?
//
//            path.cubicTo((float) points.get(i).x, (float) points.get(i).y, (float) points.get(i - 1).x, (float) points.get(i - 1).y, (float) points.get(i - 2).x, (float) points.get(i - 2).y);
//            canvas.drawPath(path, paint);
//        }


        Path path2 = new Path();
        path2.moveTo((float)points.get(points.size()-1).x, (float)points.get(points.size()-1).y);
        for(int i = points.size()-1; i > 1; i--) {

            //paint.setColor( Color.rgb(49 +(points.size()-i+2)*2, 58+(points.size()-i+2)*2, 92+(points.size()-i+2)*2) );
            paint.setStrokeWidth((float)Math.ceil(points.get(i).z / 4.f) + 1.f); // TODO divide with maximum river length?

            //path.cubicTo((float) points.get(i).x, (float) points.get(i).y, (float) points.get(i - 1).x, (float) points.get(i - 1).y, (float) points.get(i - 2).x, (float) points.get(i - 2).y);
            path2.lineTo((float)points.get(i).x, (float)points.get(i).y);
            canvas.drawPath(path2, paint);
        }

/*
        for(int i = points.size()-1; i > 2; i -= 2) {
            Path path = new Path();
            path.moveTo((float)points.get(i).x, (float)points.get(i).y);

            //paint.setColor( Color.rgb(49 +(points.size()-i+2)*2, 58+(points.size()-i+2)*2, 92+(points.size()-i+2)*2) );
            paint.setStrokeWidth((float)Math.ceil(points.get(i).z / 3.f) + 3.f); // TODO divide with maximum river length?

            //path.quadTo((float) points.get(i).x, (float) points.get(i).y, (float) points.get(i - 1).x, (float) points.get(i - 1).y);

            //path.quadTo((float) points.get(i).x, (float) points.get(i).y, (float) points.get(i - 1).x, (float) points.get(i - 1).y);
            path.quadTo((float) points.get(i - 1).x, (float) points.get(i - 1).y, (float) points.get(i - 2).x, (float) points.get(i - 2).y);

            //path.cubicTo((float) points.get(i + 1).x, (float) points.get(i + 1).y, (float) points.get(i + 2).x, (float) points.get(i + 2).y, (float) points.get(i + 3).x, (float) points.get(i + 3).y);
            //path.lineTo((float)points.get(i+2).x, (float)points.get(i+2).y);

//           if ( i == 0 ) {
//            path.lineTo((float)points.get(i+1).x, (float)points.get(i+1).y);
//            path.lineTo((float)points.get(i+2).x, (float)points.get(i+2).y);
//           }

            // canvas.drawLine((float)points.get(i).x, (float)points.get(i).y, (float)points.get(i+1).x, (float)points.get(i+1).y, paint );
            canvas.drawPath(path, paint);
        }
*/


//        Path path2 = new Path();
//        path2.moveTo((float)points.get(points.size()-3).x, (float)points.get(points.size()-3).y);
//        path2.quadTo((float) points.get(points.size() - 2).x, (float) points.get(points.size() - 2).y, (float) points.get(points.size() - 1).x, (float) points.get(points.size() - 1).y);
//        path2.lineTo((float)points.get(points.size()-1).x, (float)points.get(points.size()-1).y);
//        canvas.drawPath(path2, paint);


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

/*        Path path = new Path();
        boolean first = true;
        for(int i = 0; i < points.size(); i += 2){
            Point point = points.get(i);
            if(first){
                first = false;
                path.moveTo((float)point.x, (float)point.y);
            }

            else if(i < points.size() - 1){
                Point next = points.get(i + 1);
                path.quadTo((float)point.x, (float)point.y, (float)next.x, (float)next.y);
            }
            else{
                path.lineTo((float)point.x, (float)point.y);
            }
        }

        canvas.drawPath(path, paint);*/
    }
}
