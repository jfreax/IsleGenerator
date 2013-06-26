package de.jdsoft.strandet.Render;


import android.graphics.Color;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import com.marcrh.graph.Point;
import com.marcrh.graph.delaunay.Triad;
import de.jdsoft.strandet.Entity.Tile;
import de.jdsoft.strandet.Map.TileManager;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class OGLRenderer implements GLSurfaceView.Renderer {

    private TileManager tileManager;
    private PointF surfaceSize;
    public int no = 0;

    private ShortBuffer indexBuffer;


    public OGLRenderer() {
        surfaceSize = new PointF();
    }


    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);  // Set color's clear-value to black
        gl.glClearDepthf(1.0f);            // Set depth's clear-value to farthest
        gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
        gl.glDepthFunc(GL10.GL_LEQUAL);    // The type of depth testing to do
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
        gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
        gl.glDisable(GL10.GL_DITHER);      // Disable dithering for better performance

    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //gl.glViewport(0, 0, width, height);

        surfaceSize.set(width, height);

        gl.glViewport(0, 0, width, height);

        // make adjustments for screen ratio
        float ratio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);        // set matrix to projection mode
        gl.glLoadIdentity();                        // reset the matrix to its default state
        gl.glFrustumf(-ratio, ratio, -1, 1, 1, 100);  // apply the projection matrix

        //gl.glViewport(0, 0, (int)surfaceSize.x, (int)surfaceSize.y);

/*
        float ratio = width / height;
        float zoom = 1.0f;


        // Sets the current view port to the new size.
        gl.glViewport(0, 0, width, height);
        // Select the projection matrix
        gl.glMatrixMode(GL10.GL_PROJECTION);
        // Reset the projection matrix
        gl.glLoadIdentity();
        // Calculate the aspect ratio of the window
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, -1, 1.0f);
        //gl.glFrustumf(-ratio, ratio, -1, 1, zoom, 25);

        // Select the modelview matrix
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        // Reset the modelview matrix
        gl.glLoadIdentity(); //- See more at: http://blog.uncle.se/2012/02/opengl-es-tutorial-for-android-part-i-setting-up-the-view/#sthash.Z5fVTFqN.dpuf
*/



//        gl.glMatrixMode(GL10.GL_PROJECTION);
//        gl.glLoadIdentity();
//        //GLU.gluOrtho2D(gl, 0, (int)surfaceSize.x, (int)surfaceSize.y, 0);
//        //GLU.gluLookAt(gl,);
//
//        GLU.gluPerspective(gl, 45.0f, width/height, -100, 1);
//
//
//        gl.glMatrixMode(GL10.GL_MODELVIEW);
//        gl.glLoadIdentity();


        tileManager = new TileManager(width, height);
//
//
//        // Disable a few things we are not going to use.
//        gl.glDisable(GL10.GL_LIGHTING);
//        gl.glDisable(GL10.GL_CULL_FACE);
//        gl.glDisable(GL10.GL_DEPTH_BUFFER_BIT);
//        gl.glDisable(GL10.GL_DEPTH_TEST);
////        gl.glClearColor(.5f, .5f, .8f, 1.f);
////        gl.glShadeModel(GL10.GL_SMOOTH);
////
////        float ratio = surfaceSize.x / surfaceSize.y;
////        gl.glViewport(0, 0, (int) surfaceSize.x, (int) surfaceSize.y);
////
//        // Set our field of view.
//        gl.glMatrixMode(GL10.GL_PROJECTION);
//        gl.glLoadIdentity();
//        gl.glFrustumf(
//                -surfaceSize.x / 2, surfaceSize.x / 2,
//                -surfaceSize.y / 2, surfaceSize.y / 2,
//                1, 3);
//
//        // Position the camera at (0, 0, -2) looking down the -z axis.
//        gl.glMatrixMode(GL10.GL_MODELVIEW);
//        gl.glLoadIdentity();
//        // Points rendered to z=0 will be exactly at the frustum's
//        // (farZ - nearZ) / 2 so the actual dimension of the triangle should be
//        // half
//        gl.glTranslatef(9, 0, -1);
//
//        gl.glViewport(0, 0, width, height);
        // for a fixed camera, set the projection too
//        float ratio = (float) width / height;
//        gl.glMatrixMode(GL10.GL_PROJECTION);
//        gl.glLoadIdentity();
//        //gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
//        gl.glFrustumf(-ratio, ratio, -1, 1, 3, 7);

//        float[] ambient = {0.1f, 1, 1, 1};
//        float[] position = {0, 0, 1, 1};
//        float[] direction = {0, 0, -1};
//        gl.glEnable(GL10.GL_LIGHTING);
//        gl.glEnable(GL10.GL_LIGHT1);
//        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, ambient, 0 );
//        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, position, 0);
//        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_SPOT_DIRECTION, direction, 0);
//        gl.glLightf(GL10.GL_LIGHT1, GL10.GL_SPOT_CUTOFF, 30.0f);
    }

    public void onDrawFrame(GL10 gl) {


        gl.glClearColor(mRed, mGreen, mBlue, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

//        gl.glMatrixMode(GL10.GL_MODELVIEW);
//        gl.glLoadIdentity();
//

        // Set GL_MODELVIEW transformation mode
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();                      // reset the matrix to its default state

        // When using GL_MODELVIEW, you must set the camera view
        GLU.gluLookAt(gl, 0, -(no/200.f), -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        //gl.glRotatef(-0.1f, 0, 0, 0);
        gl.glTranslatef(-1.5f, -2.25f, 0.f);



        //gl.glFrontFace(GL10.GL_CW);


        List<Tile> tiles = tileManager.getTiles();

//        float[] vertices = {  // Vertices of the triangle
//                0.0f,  0.5f, 1.0f, // 0. top
//                -1.0f, -1.0f, 1.0f, // 1. left-bottom
//                1.0f, -1.0f, 1.0f,  // 2. right-bottom
//                1.0f, 1.0f, 1.0f  // 2. right-bottom
//
//        };
        //float[] vertices = tiles.get(0).getVertices();

        //Random random = new Random();
        for( Tile tile : tiles) {
        //Tile tile = tiles.get(no);

        //float[] vertices = new float[tile.getRegion().getTriads().size()*3];
        float[] vertices = new float[tile.getPoints().size()*3];

        ArrayList<Triad> triads = tile.getRegion().getTriads();
        //Collections.sort(triads);

//        int i = 0;
//        for(Triad tr : triads) {
//            vertices[i++] = (float)tr.x;
//            vertices[i++] = (float)tr.y;
//            vertices[i++] = 1.0f;
//        }

        int color = tile.getColor(tileManager);
        gl.glColor4f(Color.red(color) / 255.f, Color.green(color) / 255.f, Color.blue(color) / 255.f, 1.0f);

        List<Point> points = tile.getPoints();

        //    Collections.sort(points);

        //short[] indices = new short[vertices.length-3];
        short[] indices = new short[tile.getPoints().size()];
        for( short ind = 0; ind < indices.length; ind++ ) {
            indices[ind] = ind;
        }
//        for( short j = 0; j < (vertices.length % 3); j++ ) {
//            indices[vertices.length+j] = j;
//        }
        //indices[0] = 0;


//        for( short ind = 0, indValue = 1; ind < indices.length; ind += 3 ) {
//            indices[ind] = 0;
//            indices[ind+1] = indValue++;
//            indices[ind+2] = indValue;
//        }


        //Collections.sort(points);

        int i = 0;
        for( Point p : points ) {
            vertices[i++] = ((float) p.x / surfaceSize.x) * 3;
            vertices[i++] = ((float) p.y / surfaceSize.x) * 3;
            vertices[i++] = -(float) p.z * 1;
        }
                //gl.glColor4f(tile.getPoints().get(0)., random.nextFloat(), random.nextFloat(), random.nextFloat());


        int div = 1;
        float[] triangles = {
                0, 0, 0,
                0, (short) (surfaceSize.y / div), 0,
                (short) (surfaceSize.x), (short) (surfaceSize.y), 0,
        };

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder()); // Use native byte order
        FloatBuffer vertexBuffer = vbb.asFloatBuffer(); // Convert byte buffer to float
        vertexBuffer.put(vertices);         // Copy data into buffer
        vertexBuffer.position(0);           // Rewind

        // Setup index-array buffer. Indices in byte.
        //short[] indices = {0, 1, 2};
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indexBuffer = ibb.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);

        gl.glPushMatrix();

        // Enable vertex-array and define the buffers
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

        // Draw the primitives via index-array
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, indices.length*2, GL10.GL_UNSIGNED_BYTE, indexBuffer);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glPopMatrix();
        }

/*        final int div = 1;
        short[] triangles = {
                0, 0, 0,
                0, (short) (surfaceSize.y / div), 0,
                (short) (surfaceSize.x / div), (short) (surfaceSize.y / div), 0,
        };
        ShortBuffer triangleBuffer = ShortBuffer.wrap(triangles);

        //gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _vertexBuffer);

        //gl.glDrawElements(GL10.GL_TRIANGLES, _nrOfVertices, GL10.GL_UNSIGNED_SHORT, _indexBuffer);

        gl.glPushMatrix();

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        //gl.glTranslatef(offset.x, offset.y, 0);

        gl.glColor4f(1.0f, 0.3f, 0.0f, .5f);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_SHORT, 0, triangleBuffer);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 3);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glPopMatrix();*/
    }

    public void setColor(float r, float g, float b) {
        mRed = r;
        mGreen = g;
        mBlue = b;
    }

    private float mRed;
    private float mGreen;
    private float mBlue;
}
