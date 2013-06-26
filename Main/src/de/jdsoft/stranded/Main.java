package de.jdsoft.stranded;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.marcrh.graph.Point;
import de.jdsoft.stranded.Entity.Tile;
import de.jdsoft.stranded.Map.TileManager;


public class Main extends Game {
    Matrix4 matrix = new Matrix4();


    TileManager tileManager;

    //OrthographicCamera camera;
    PerspectiveCamera cam;
    Model model;
    ModelInstance instance;
    ModelBatch modelBatch;

    CameraInputController camController;

    private Mesh mesh;
    private ShaderProgram shaderProgram;


    ShapeRenderer shapeRenderer;

    Lights lights;


    @Override
    public void create() {
        //camera = new OrthographicCamera();
        //camera.setToOrtho(false, 800, 480);

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


        lights = new Lights();
        lights.ambientLight.set(0.4f, 0.4f, 0.4f, 1f);
        lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        modelBatch = new ModelBatch();

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0,0,0);
        cam.near = 0.1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(5f, 5f, 5f,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);

        Gdx.graphics.getGL20().glEnable(GL20.GL_TEXTURE_2D);
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.graphics.getGL20().glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


        tileManager = new TileManager(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render() {
        camController.update();
        cam.update();


        Gdx.graphics.getGL20().glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);

//        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

//        mesh.render(GL20.GL_TRIANGLE_STRIP);

//        for( Tile tile : tileManager.getTiles()) {
//            System.out.println( ""+ tile.getVertices().length);
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//            shapeRenderer.setColor( new Color(tile.getColor(tileManager)) );
//            shapeRenderer.polygon(tile.getVertices());
//            shapeRenderer.end();
//        }

        mesh = new Mesh(true, 3, 3,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));

        mesh.setVertices(new float[] { -0.5f, -0.5f, 0,
                0.5f, -0.5f, 0,
                0,     0.5f, 0 });
        mesh.setIndices(new short[] { 0, 1, 2 });


        shaderProgram.begin();
        shaderProgram.setUniformMatrix("u_worldView", cam.combined);
        //shaderProgram.setUniformi("u_texture", 0);
        mesh.render(shaderProgram, GL20.GL_TRIANGLES);
        shaderProgram.end();



//        modelBatch.begin(cam);
//        modelBatch.render(instance, lights);
//        modelBatch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        model.dispose();
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
