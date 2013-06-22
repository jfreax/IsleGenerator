package de.jdsoft.strandet;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.marcrh.graph.Point;
import com.marcrh.graph.Range;
import com.marcrh.graph.Utils;
import com.marcrh.graph.delaunay.Voronoi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DrawingPanel extends SurfaceView implements SurfaceHolder.Callback {

    private PanelThread thread;
    TileManager manager;

    Random random = new Random();


    public DrawingPanel(Context context) {
        super(context);
        Initialize();
    }

    public DrawingPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        Initialize();
    }

    public DrawingPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Initialize();
    }

    private void Initialize() {
        getHolder().addCallback(this);
    }

    @Override
    public void onDraw(Canvas canvas) {

        //canvas.scale(1.0f, 0.5f);
        //canvas.rotate(45);
        //canvas.translate(getHeight()/2, 0);

        for(Tile tile : manager.getTiles() ) {
            tile.draw(canvas);
        }

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setWillNotDraw(false); //Allows us to use invalidate() to call onDraw()

        thread = new PanelThread(getHolder(), this); //Start the thread that
        thread.setRunning(true);                     //will make calls to
        thread.start();                              //onDraw()

        manager = new TileManager(getWidth(), getHeight());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            thread.setRunning(false);                //Tells thread to stop
            thread.join();                           //Removes thread from mem.
        } catch (InterruptedException e) {
        }
    }

}