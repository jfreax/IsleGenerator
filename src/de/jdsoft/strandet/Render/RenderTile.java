package de.jdsoft.strandet.Render;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import com.marcrh.graph.Point;
import de.jdsoft.strandet.Entity.Tile;
import de.jdsoft.strandet.TileManager;

import java.util.List;

public class RenderTile {

    static public void draw(Canvas canvas, TileManager tileManager, Tile tile) {
        List<Point> points = tile.getPoints();

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
        paint.setColor(tile.getColor(tileManager));
        //paint.setAntiAlias(true);
        //paint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.SOLID));

        //paint.setShadowLayer(10, -40, 40, Color.rgb(0,0,0));
        //paint.setShader(new Shader());

//        if( goesDownFrom > 0.01f ) {
//            int aEnd = (int)(((goesDownFrom - getHeight()) / tileManager.maxHeightDifference) * 50);
//            paint.setColor(colorDarker( getColor(tileManager), ((goesDownFrom - getHeight()) / tileManager.maxHeightDifference)*10 ));
//
//        }
//
//        if( goesDownFrom > 0.f) {
//        paint.setColor(Color.rgb((int)(goesDownFrom - getHeight())*100, 0, 0));
//
//        }

        canvas.drawPath(path, paint);

/*         if( goesDownFrom > 0.01f ) {
             Paint paint2 = new Paint();
             paint.setStyle(Paint.Style.FILL);

             Point pStart = Collections.max(points);
             Point pEnd   = Collections.min(points);

             int aStart   = (int)(((getHeight() - goesUpTo) / tileManager.maxHeightDifference) * 50);
             int aEnd = (int)(((goesDownFrom - getHeight()) / tileManager.maxHeightDifference) * 50);


             paint2.setShader(new LinearGradient((float)pStart.x, (float)pStart.y, (float)pEnd.x, (float)pEnd.y,
                         Color.argb(aEnd, 0, 0, 0) , Color.argb(aStart, 0, 0, 0) , Shader.TileMode.CLAMP));
             //paint.setShader(new LinearGradient((float)points.get(0).x, (float)points.get(0).y, (float)points.get(points.size()-1).x, (float)points.get(points.size()-1).y, Color.rgb(89, 98, 132) , Color.rgb(49, 58, 92) , Shader.TileMode.CLAMP));
             canvas.drawPath(path, paint2);
        }*/

    }
}
