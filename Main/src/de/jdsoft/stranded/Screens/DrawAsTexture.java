package de.jdsoft.stranded.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.marcrh.graph.Point;
import de.jdsoft.stranded.Entity.Tile;
import de.jdsoft.stranded.Map.TileManager;
import de.jdsoft.stranded.Utils.BlurUtils;

import java.util.List;

public class DrawAsTexture implements Screen {

    TileManager tileManager;
    int mapWidth = 512;
    int mapHeight = 512;

    PerspectiveCamera cam;
    CameraInputController camController;

    Pixmap pixmap;
    Texture texture;
    SpriteBatch batch;
    TextureRegion region;

    ShaderProgram shader;

    // To draw sphere
    double PI = 3.1415926535897;
    float space = 5;
    int verticesCount = (180 / (int)space) * (360 / (int)space) * 4 * 6;

    Mesh mesh;



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

        Gdx.graphics.getGL20().glDisable(GL20.GL_DEPTH_TEST);



        // Enable face culling- be careful with spriteBatch, might cull sprites as well!
        Gdx.graphics.getGL20().glEnable(GL20.GL_CULL_FACE);

        // What faces to remove with the face culling.
        Gdx.graphics.getGL20().glCullFace(GL20.GL_FRONT);

        // Create texture
        computeTexture();

        // Setup shader
        SetupShader();

        // Sphere mesh
        mesh = new Mesh(true, verticesCount, verticesCount / 6,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, "a_color"),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoord0"));

        computeMesh();

    }

    float angle = 0.f;

    @Override
    public void render(float delta) {
        camController.update();
        cam.update();

        angle += delta*3;
        angle %= 360;


        Gdx.graphics.getGL20().glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glDepthMask(false);
        Gdx.graphics.getGL20().glDisable(GL10.GL_BLEND);

        //batch.setProjectionMatrix(cam.combined);
//        batch.begin();
//        batch.draw(texture, 0, 0);
//        batch.end();



        Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);
        texture.bind();

        shader.begin();
        shader.setUniformMatrix("u_projTrans", cam.combined.rotate(0,0,1.f, angle));
        shader.setUniformi("u_texture", 0);
        mesh.render(shader, GL20.GL_TRIANGLE_STRIP);
        shader.end();
    }

    void SetupShader() {
        ShaderProgram.pedantic = false;
        shader = new ShaderProgram(
                Gdx.files.internal("default.vert").readString(),
                Gdx.files.internal("default.frag").readString());
        if(!shader.isCompiled()) {
            Gdx.app.log("Problem loading shader:", shader.getLog());
        }
        batch.setShader(shader);
    }


    private void computeTexture() {

        // Create new tilemanager and create new random map
        tileManager = new TileManager(mapWidth, mapHeight);

        pixmap = new Pixmap(mapWidth, mapHeight, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);

        // Create a texture to contain the pixmap
        texture = new Texture(mapWidth, mapHeight, Pixmap.Format.RGBA8888); // Pixmap.Format.RGBA8888);

        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);

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

        Pixmap blurred = BlurUtils.blur(pixmap, 1, 1, true);

        // Blit the composited overlay to a texture
        texture.draw(blurred, 0, 0);
        batch = new SpriteBatch();

        blurred.dispose();
        //pixmap.dispose();
    }


    private void computeMesh() {
        // Compute sphere vertices
        int n = 0;
        float R = 5, H = 0, K = 0, Z = 0;
        float[] vertices = new float[verticesCount];
        for( float b = 0; b <= 180 - space; b+=space) {
            for( float a = 0; a <= 360 - space; a+=space) {

                vertices[n++] = R * (float)Math.sin((a) / 180.f * PI) * (float)Math.sin((b) / 180.f * PI) - H;
                vertices[n++] = R * (float)Math.cos((a) / 180.f * PI) * (float)Math.sin((b) / 180.f * PI) + K;
                vertices[n++] = R * (float)Math.cos((b) / 180.f * PI) - Z;
                vertices[n++] = new Color(Color.rgba8888(1f, 1, 1, 0)).toFloatBits();
                vertices[n++] = b / 180.f;
                vertices[n++] = a / 360.f;

                vertices[n++] = R * (float)Math.sin((a) / 180.f * PI) * (float)Math.sin((b + space) / 180.f * PI) - H;
                vertices[n++] = R * (float)Math.cos((a) / 180.f * PI) * (float)Math.sin((b + space) / 180.f * PI) + K;
                vertices[n++] = R * (float)Math.cos((b + space) / 180.f * PI) - Z;
                vertices[n++] = new Color(Color.rgba8888(1, 1, 1, 0)).toFloatBits();
                vertices[n++] = (b + space) / 180.f;
                vertices[n++] = a / 360.f;

                vertices[n++] = R * (float)Math.sin((a + space) / 180.f * PI) * (float)Math.sin((b) / 180.f * PI) - H;
                vertices[n++] = R * (float)Math.cos((a + space) / 180.f * PI) * (float)Math.sin((b) / 180.f * PI) + K;
                vertices[n++] = R * (float)Math.cos((b) / 180.f * PI) - Z;
                vertices[n++] = new Color(Color.rgba8888(1, 1, 1, 0)).toFloatBits();
                vertices[n++] = b / 180.f;
                vertices[n++] = (a + space) / 360.f;

                vertices[n++] = R * (float)Math.sin((a + space) / 180.f * PI) * (float)Math.sin((b + space) / 180.f * PI) - H;
                vertices[n++] = R * (float)Math.cos((a + space) / 180.f * PI) * (float)Math.sin((b + space) / 180.f * PI) + K;
                vertices[n++] = R * (float)Math.cos((b + space) / 180.f * PI) - Z;
                vertices[n++] = new Color(Color.rgba8888(1, 1, 1, 0)).toFloatBits();
                vertices[n++] = (b + space) / 180.f;
                vertices[n++] = (a + space) / 360.f;
            }
        }

        // Sphere indices
        short[] indices = new short[vertices.length / 6];
        for( short ind = 0; ind < indices.length; ind++ ) {
            indices[ind] = ind;
        }

        mesh.setVertices(vertices, 0, vertices.length);
        mesh.setIndices(indices, 0, indices.length);
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
