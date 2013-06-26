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


    @Override
    public void create() {
        //check we can use GLES2
        if (!Gdx.app.getGraphics().isGL20Available()) {
            throw new GdxRuntimeException("GLES2 Not Available!");
        }

        //create shader program
        String vertexShader = "attribute vec4 a_position;    \n" +
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

        shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
        if (shaderProgram.isCompiled() == false) {
            Gdx.app.log("ShaderError", shaderProgram.getLog());
            System.exit(0);
        }

/*

        lights = new Lights();
        lights.ambientLight.set(0.4f, 0.4f, 0.4f, 1f);
        lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
*/


        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(-10f, 0f, 10f);
        //cam.lookAt(Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getHeight() / 2);
        cam.lookAt(0, 0, 0);
        cam.near = 0.1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        Gdx.graphics.getGL20().glEnable(GL20.GL_TEXTURE_2D);
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.graphics.getGL20().glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


        tileManager = new TileManager(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render() {
        camController.update();
        cam.update();

        Gdx.graphics.getGL20().glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);


        mesh = new Mesh(true, 30, 30,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, "a_color"));

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
                vertices[i++] = 0f; // (float) p.z * 1;
                vertices[i++] = (new Color(tile.getColor(tileManager))).toFloatBits();
            }

            mesh.setVertices(vertices, 0, vertices.length);
            mesh.setIndices(indices, 0, indices.length);


            shaderProgram.begin();
            shaderProgram.setUniformMatrix("u_worldView", cam.combined);
            mesh.render(shaderProgram, GL20.GL_TRIANGLE_FAN);
            shaderProgram.end();

        }

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
