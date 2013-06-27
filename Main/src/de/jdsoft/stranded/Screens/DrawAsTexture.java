package de.jdsoft.stranded.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import de.jdsoft.stranded.Map.TileManager;

public class DrawAsTexture implements Screen {

    TileManager tileManager;

    PerspectiveCamera cam;
    CameraInputController camController;

    Pixmap pixmap;
    Texture texture;
    SpriteBatch batch;
    TextureRegion region;

    private OrthographicCamera oCam;



    @Override
    public void show() {
        // Set camera
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0.f, 0.f, 10f);
        //cam.lookAt(Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getHeight() / 2);
        cam.lookAt(0.f, 0.f, 0);
        cam.near = 0.1f;
        cam.far = 300f;
        cam.update();

        // Initialize camera controler
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        // Enable opengl features
        Gdx.graphics.getGL20().glEnable(GL20.GL_TEXTURE_2D);
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.graphics.getGL20().glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        //Gdx.graphics.getGL20().glEnable(GL20.GL_DEPTH_TEST);

        // Enable face culling- be careful with spriteBatch, might cull sprites as well!
        //Gdx.graphics.getGL20().glEnable(GL20.GL_CULL_FACE);

        // What faces to remove with the face culling.
        //Gdx.graphics.getGL20().glCullFace(GL20.GL_FRONT);


        // Create new tilemanager and create new random map
        tileManager = new TileManager(360, 360);

        // Initialize
        pixmap = new Pixmap(800, 480, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);

        // Create a texture to contain the pixmap
        texture = new Texture(1024, 1024, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);

        pixmap.setColor(1.0f, 0.0f, 0.0f, 1.0f); // Red
        pixmap.drawLine(0, 0, 100, 100);

        pixmap.setColor(0.0f, 0.0f, 1.0f, 1.0f); // Blue
        pixmap.drawLine(100, 100, 200, 0);

        pixmap.setColor(0.0f, 1.0f, 0.0f, 1.0f); // Green
        pixmap.drawLine(100, 0, 100, 100);

        pixmap.setColor(1.0f, 1.0f, 1.0f, 1.0f); // White
        pixmap.drawCircle(400, 300, 100);

        // Blit the composited overlay to a texture
        texture.draw(pixmap, 0, 0);
        region = new TextureRegion(texture, 0, 0, 800, 480);
        batch = new SpriteBatch();

        Pixmap pixmap = new Pixmap(512, 1024, Pixmap.Format.RGBA8888);
        for (int y = 0; y < pixmap.getHeight(); y++) { // 1024
            for (int x = 0; x < pixmap.getWidth(); x++) { // 512
                pixmap.getPixel(x, y);
            }
        }
        pixmap.dispose();
    }


    @Override
    public void render(float delta) {
        camController.update();
        cam.update();

        Gdx.graphics.getGL20().glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        batch.draw(region, 0, 0);
        batch.end();
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
