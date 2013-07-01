package de.jdsoft.stranded.render.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class PlanetShader extends BaseShader {
    protected final BaseShader.Input u_projTrans	= register(new Input(GLOBAL_UNIFORM, "u_projTrans"));
    protected final Input u_worldTrans	= register(new Input(GLOBAL_UNIFORM, "u_worldTrans"));
    protected final Input u_time = register(new Input(GLOBAL_UNIFORM, "u_time"));

    protected final ShaderProgram program;

    public PlanetShader() {
        super();
        program = new ShaderProgram(
                Gdx.files.internal("shader/planet_effect.vertex.glsl").readString(),
                Gdx.files.internal("shader/planet_effect.fragment.glsl").readString());
        if (!program.isCompiled())
            throw new GdxRuntimeException("Couldn't compile shader " + program.getLog());
    }

    @Override
    public void init () {
        super.init(program, 0, 0, 0);
    }

    @Override
    public int compareTo (Shader other) {
        return 0;
    }

    @Override
    public boolean canRender (Renderable instance) {
        return true;
    }

    @Override
    public void begin (Camera camera, RenderContext context) {
        this.camera = camera;
        this.context = context;
        program.begin();

        set(u_projTrans, camera.combined);
    }

    final Vector3 dir = new Vector3();
    final Vector3 tmp = new Vector3();
    final Vector3 tmp2 = new Vector3();
    final Vector3 planetEffectPosition = new Vector3();

    @Override
    public void render (Renderable renderable) {
        context.setBlending(true, GL10.GL_ONE, GL10.GL_ONE);

        // Transform world view to make this a billboard
        renderable.worldTransform.getTranslation(planetEffectPosition);
        dir.set(camera.position).sub(planetEffectPosition).nor();

        Quaternion rotation = new Quaternion();
        renderable.worldTransform.getRotation(rotation);

        tmp.set(camera.up.cpy()).crs(dir).nor();
        tmp2.set(dir).crs(tmp).nor();
        rotation.setFromAxes(tmp.x, tmp2.x, dir.x, tmp.y, tmp2.y, dir.y, tmp.z, tmp2.z, dir.z);

        renderable.worldTransform.set(rotation);

        // Set world transformation matrix
        set(u_worldTrans, renderable.worldTransform);

        // Get time attribute
        //TimeAttribute attr = (TimeAttribute)renderable.material.get(TimeAttribute.ID);
        //set(u_time, attr == null ? 0.0f : attr.value);

        // Render mesh
        renderable.mesh.render(program, renderable.primitiveType, renderable.meshPartOffset, renderable.meshPartSize);
    }

    @Override
    public void end () {
        program.end();
    }

    @Override
    public void dispose () {
        super.dispose();
        program.dispose();
    }

}