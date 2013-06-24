package de.jdsoft.strandet;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class ClearGLSurfaceView extends GLSurfaceView {
    public ClearGLSurfaceView(Context context) {
        super(context);
        mRenderer = new OGLRenderer();
        setRenderer(mRenderer);
    }

    public boolean onTouchEvent(final MotionEvent event) {
        queueEvent(new Runnable(){
            public void run() {
                mRenderer.setColor(event.getX() / getWidth(),
                        event.getY() / getHeight(), 1.0f);
            }});
        return true;
    }

    OGLRenderer mRenderer;
}
