package de.jdsoft.stranded;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.marcrh.graph.Point;
import de.jdsoft.stranded.Entity.Tile;
import de.jdsoft.stranded.Map.TileManager;

import java.util.List;


public class Main extends Game {
    TileManager tileManager;

    PerspectiveCamera cam;
    CameraInputController camController;

    private Mesh mesh;
    private ShaderProgram shaderProgram;

    Lights lights;

    double PI = 3.1415926535897;
    float space = 10;
    int verticesCount = (180 / (int)space) * (360 / (int)space) * 4 * 4;


    @Override
    public void create() {
        // Check we can use GLES2
        if (!Gdx.app.getGraphics().isGL20Available()) {
            throw new GdxRuntimeException("GLES2 Not Available!");
        }

        // Create shader program
        String verticesShader = "attribute vec4 a_position;    \n" +
                "attribute vec4 a_color;\n" +
                "uniform mat4 u_worldView;\n" +
                "varying vec4 v_color;" +
                "void main()                  \n" +
                "{                            \n" +
                "   v_color = a_color; \n" +
                "   gl_Position =  u_worldView * a_position;  \n"      +
                "}                            \n" ;
        String fragmentShader = "#ifdef GL_ES\n" +
                "precision mediump float;\n" +
                "#endif\n" +
                "varying vec4 v_color;\n" +
                "void main()                                  \n" +
                "{                                            \n" +
                "  gl_FragColor = v_color;\n" +
        "}";

        shaderProgram = new ShaderProgram(verticesShader, fragmentShader);
        if (shaderProgram.isCompiled() == false) {
            Gdx.app.log("ShaderError", shaderProgram.getLog());
            System.exit(0);
        }

        // Create empty mesh
        mesh = new Mesh(true, verticesCount+1, verticesCount/4 + 5,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, "a_color"));


/*

        lights = new Lights();
        lights.ambientLight.set(0.4f, 0.4f, 0.4f, 1f);
        lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
*/

        // Set camera
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(3.f, 3.f, 10f);
        //cam.lookAt(Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getHeight() / 2);
        cam.lookAt(3.f, 3.f, 0);
        cam.near = 0.1f;
        cam.far = 300f;
        cam.update();

        // Initialize camera controler
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        // Enable opengl features
        Gdx.graphics.getGL20().glEnable(GL20.GL_TEXTURE_2D);
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.graphics.getGL20().glBlendFunc(GL20.GL_ONE, GL20.GL_ONE);
        //Gdx.graphics.getGL20().glEnable(GL20.GL_DEPTH_TEST);

        // Enable face culling- be careful with spriteBatch, might cull sprites as well!
        Gdx.graphics.getGL20().glEnable(GL20.GL_CULL_FACE);

        // What faces to remove with the face culling.
        Gdx.graphics.getGL20().glCullFace(GL20.GL_FRONT);


        // Create new tilemanager and create new random map
        tileManager = new TileManager(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render() {
        camController.update();
        cam.update();

        Gdx.graphics.getGL20().glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);

//        mesh.setVertices(new float[] { -0.5f, -0.5f, 0,
//                0.5f, -0.5f, 0,
//                0,     0.5f, 0 });
//        mesh.setIndices(new short[] { 0, 1, 2 });
//
//
//        shaderProgram.begin();
//        shaderProgram.setUniformMatrix("u_worldView", cam.combined);
//        mesh.render(shaderProgram, GL20.GL_TRIANGLES);
//        shaderProgram.end();

/*
        List<Tile> tiles = tileManager.getTiles();
        for( Tile tile : tiles) {

            short[] indices = new short[tile.getPoints().size()];
            for( short ind = 0; ind < indices.length; ind++ ) {
                indices[ind] = ind;
            }

            List<Point> points = tile.getPoints();
            int i = 0;
            float[] vertices = new float[tile.getPoints().size()*4];
            for( Point p : points ) {
                vertices[i++] = (float) p.x / 100;
                vertices[i++] = (float) p.y / 100;
                vertices[i++] = (float) Math.sqrt( p.z*p.z*p.z ); // * 2.4f;
                vertices[i++] = (new Color(tile.getColor(tileManager))).toFloatBits();
            }

            mesh.setVertices(vertices, 0, vertices.length);
            mesh.setIndices(indices, 0, indices.length);


            shaderProgram.begin();
            shaderProgram.setUniformMatrix("u_worldView", cam.combined);
            mesh.render(shaderProgram, GL20.GL_TRIANGLE_FAN);
            shaderProgram.end();

        }*/

//        float r = 1.f;
//        int i = 0;
//        float[] vertices = new float[4000];
//        for( float thita = 0.f; thita < 6.28f; thita += 0.1f ) {
//            for( float fy = 0.f; fy < 3.14f; fy += 0.2f ) {
//                vertices[i++] = 0.f;
//                vertices[i++]= r * (float)(Math.sin(thita) * Math.cos(fy));
//                vertices[i++]= r * (float)(Math.sin(thita) * Math.sin(fy));
//                vertices[i++]= r * (float)Math.cos(thita);
//                vertices[i++]= (int)Math.cos(thita)*255 << 24;
//            }
//        }
        int n = 0;
        float R = 5, H = 0, K = 0, Z = 0;
        float[] vertices = new float[verticesCount];
        for( float b = 0; b <= 180 - space; b+=space){
            for( float a = 0; a <= 360 - space; a+=space){

                vertices[n++] = R * (float)Math.sin((a) / 180.f * PI) * (float)Math.sin((b) / 180.f * PI) - H;
                vertices[n++] = R * (float)Math.cos((a) / 180.f * PI) * (float)Math.sin((b) / 180.f * PI) + K;
                vertices[n++] = R * (float)Math.cos((b) / 180.f * PI) - Z;
                vertices[n++] = (2.f * b) / 360.f;
                //vertices[n++] = (a) / 360.f;

                vertices[n++] = R * (float)Math.sin((a) / 180.f * PI) * (float)Math.sin((b + space) / 180.f * PI) - H;
                vertices[n++] = R * (float)Math.cos((a) / 180.f * PI) * (float)Math.sin((b + space) / 180.f * PI) + K;
                vertices[n++] = R * (float)Math.cos((b + space) / 180.f * PI) - Z;
                //vertices[n++] = (2.f * (b + space)) / 360.f;
                vertices[n++] = (a) / 360.f;

                vertices[n++] = R * (float)Math.sin((a + space) / 180.f * PI) * (float)Math.sin((b) / 180.f * PI) - H;
                vertices[n++] = R * (float)Math.cos((a + space) / 180.f * PI) * (float)Math.sin((b) / 180.f * PI) + K;
                vertices[n++] = R * (float)Math.cos((b) / 180.f * PI) - Z;
                //vertices[n++] = (2.f * b) / 360.f;
                vertices[n++] = (a + space) / 360.f;

                vertices[n++] = R * (float)Math.sin((a + space) / 180.f * PI) * (float)Math.sin((b + space) / 180.f * PI) - H;
                vertices[n++] = R * (float)Math.cos((a + space) / 180.f * PI) * (float)Math.sin((b + space) / 180.f * PI) + K;
                vertices[n++] = R * (float)Math.cos((b + space) / 180.f * PI) - Z;
                //vertices[n++] = (2.f * (b + space)) / 360.f;
                vertices[n++] = (a + space) / 360.f;

            }

        }


        short[] indices = new short[vertices.length / 4];
        for( short ind = 0; ind < indices.length; ind++ ) {
            indices[ind] = ind;
            //vertices[ind] = 0.f;
        }

        mesh.setVertices(vertices, 0, vertices.length);
        mesh.setIndices(indices, 0, indices.length);

        shaderProgram.begin();
        shaderProgram.setUniformMatrix("u_worldView", cam.combined);
        mesh.render(shaderProgram, GL20.GL_TRIANGLE_STRIP);
        shaderProgram.end();

    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
