package de.jdsoft.stranded.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.materials.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.marcrh.graph.Point;
import de.jdsoft.stranded.Entity.Tile;
import de.jdsoft.stranded.Map.TileManager;

import java.util.List;

public class DrawAsTexture implements Screen {

    TileManager tileManager;
    int mapWidth = 512;
    int mapHeight = 512;

    PerspectiveCamera cam;
    CameraInputController camController;

    Pixmap pixmap;
    Texture texture;

    private Lights lights;

    public ModelBatch modelBatch;
    public Model model;
    public ModelInstance instance;

    ShaderProgram shader;


    @Override
    public void show() {
        // Set camera
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0,0,0);
        cam.near = 0.1f;
        cam.far = 300f;
        cam.update();

        // Initialize camera controler
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        // Enable opengl features
        Gdx.graphics.getGL20().glEnable(GL20.GL_TEXTURE_2D);
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.graphics.getGL20().glBlendFunc(GL20.GL_SRC_COLOR, GL20.GL_ONE);
        //Gdx.graphics.getGL20().glEnable(GL20.GL_DEPTH_TEST);
//
//        Gdx.graphics.getGL20().glDisable(GL20.GL_DEPTH_TEST);

        //Gdx.gl.glDepthMask(false);
        //Gdx.graphics.getGL20().glDisable(GL10.GL_BLEND);


        // Enable face culling- be careful with spriteBatch, might cull sprites as well!
        Gdx.graphics.getGL20().glEnable(GL20.GL_CULL_FACE);

        // What faces to remove with the face culling.
        Gdx.graphics.getGL20().glCullFace(GL20.GL_FRONT);

        // Create texture
        computeTexture();


        lights = new Lights();
        lights.ambientLight.set(0.4f, 0.4f, 0.4f, 1f);
        lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        modelBatch = new ModelBatch(Gdx.files.internal("shader/default.vertex.glsl"), Gdx.files.internal("shader/default.fragment.glsl"));
        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createSphere(10.f, 10.f, 10.f, 50, 50,
                new Material(TextureAttribute.createDiffuse(texture)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        instance = new ModelInstance(model);

    }

    float angle = 0.f;

    @Override
    public void render(float delta) {
        camController.update();

        angle += delta*30;
        angle %= 360;

        Gdx.graphics.getGL20().glClearColor(0.2f, 0.2f, 0.2f, 0.4f);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);


        modelBatch.begin(cam);
        modelBatch.render(instance, lights);

        modelBatch.end();
    }

    void SetupShader() {
        ShaderProgram.pedantic = false;
        shader = new ShaderProgram(
                Gdx.files.internal("shader_dev/planet.vert").readString(),
                Gdx.files.internal("shader_dev/planet_texture.frag").readString());
        if(!shader.isCompiled()) {
            Gdx.app.log("Problem loading shader:", shader.getLog());
        }
    }


    private void computeTexture() {

        // Create new tilemanager and create new random map
        tileManager = new TileManager(mapWidth, mapHeight);

        pixmap = new Pixmap(mapWidth, mapHeight, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);

        List<Tile> tiles = tileManager.getTiles();
        for( Tile tile : tiles) {

            pixmap.setColor(tile.getColor(tileManager));

            List<Point> points = tile.getPoints();
            Point startPoint = points.get(0);
            for( int i = 1; i < points.size()-1; i++ ) {
                pixmap.fillTriangle(
                        (int)startPoint.x,      (int)startPoint.y,
                        (int)points.get(i).x,   (int)points.get(i).y,
                        (int)points.get(i+1).x, (int)points.get(i+1).y);
            }
        }

        //Pixmap blurred = BlurUtils.blur(pixmap, 1, 1, true);

        // Create a texture to contain the pixmap
        //texture = new Texture(mapWidth, mapHeight, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);
        texture = new Texture(pixmap, true);

        texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);


        //blurred.dispose();
        pixmap.dispose();
    }

    @Override
    public void resize(int width, int height) {
        cam.viewportHeight = height;
        cam.viewportWidth = width;
    }


    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

}
