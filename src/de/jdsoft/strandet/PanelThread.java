package de.jdsoft.strandet;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

class PanelThread extends Thread {
    private SurfaceHolder _surfaceHolder;
    private DrawingPanel _panel;
    private boolean _run = false;


    public PanelThread(SurfaceHolder surfaceHolder, DrawingPanel panel) {
        _surfaceHolder = surfaceHolder;
        _panel = panel;
    }

    public void setRunning(boolean run) { //Allow us to stop the thread
        _run = run;
    }

    @Override
    public void run() {
        Canvas c;
        while (_run) {     //When setRunning(false) occurs, _run is
            c = null;      //set to false and loop ends, stopping thread

            try {
                c = _surfaceHolder.lockCanvas(null);
                synchronized (_surfaceHolder) {

                    //Insert methods to modify positions of items in onDraw()
                    _panel.postInvalidate();

                }
            } finally {
                if (c != null) {
                    _surfaceHolder.unlockCanvasAndPost(c);
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }
}