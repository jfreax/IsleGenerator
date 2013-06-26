package de.jdsoft.stranded;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.jdsoft.stranded.Map.TileManager;


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
                "   v_color = vec4(1, 1, 1, 1); \n" +
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
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0,0,0);
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


        mesh = new Mesh(true, 3, 3,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));

        mesh.setVertices(new float[] { -0.5f, -0.5f, 0,
                0.5f, -0.5f, 0,
                0,     0.5f, 0 });
        mesh.setIndices(new short[] { 0, 1, 2 });


        shaderProgram.begin();
        shaderProgram.setUniformMatrix("u_worldView", cam.combined);
        mesh.render(shaderProgram, GL20.GL_TRIANGLES);
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
