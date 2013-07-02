package de.jdsoft.stranded.render.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.materials.*;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class PlanetShader extends BaseShader {
    protected final BaseShader.Input u_projTrans	= register(new Input(GLOBAL_UNIFORM, "u_projTrans"));
    protected final Input u_worldTrans	= register(new Input(GLOBAL_UNIFORM, "u_worldTrans"));
    protected final Input u_time = register(new Input(GLOBAL_UNIFORM, "u_time"));

    protected Camera camera;

    public PlanetShader() {
        super();
    }

    @Override
    public void init () {
        ShaderProgram newShader = new ShaderProgram(
                Gdx.files.internal("shader/planet_effect.vertex.glsl").readString(),
                Gdx.files.internal("shader/planet_effect.fragment.glsl").readString());
        if (!newShader.isCompiled())
            throw new GdxRuntimeException("Couldn't compile shader " + newShader.getLog());

        super.init(newShader, 0, 0, 0);
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
    public void begin( final Camera camera, final RenderContext context) {
        super.begin(camera, context);

        this.camera = camera;
        this.context = context;
        program.begin();

        set(u_projTrans, camera.combined);
    }

    final Vector3 dir = new Vector3();
    final Vector3 tmp = new Vector3();
    final Vector3 tmp2 = new Vector3();
    final Vector3 planetEffectPosition = new Vector3();
    final Vector3 translation = new Vector3();

    Quaternion rotation = new Quaternion();

    @Override
    public void render (Renderable renderable) {
        context.setBlending(true, GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

        // Transform world view to make this a billboard
        renderable.worldTransform.getTranslation(translation);
        renderable.worldTransform.getTranslation(planetEffectPosition);
        dir.set(camera.position).sub(planetEffectPosition).nor();

        renderable.worldTransform.getRotation(rotation);


        tmp.set(camera.up.cpy()).crs(dir).nor();
        tmp2.set(dir).crs(tmp).nor();
        rotation.setFromAxes(tmp.x, tmp2.x, dir.x, tmp.y, tmp2.y, dir.y, tmp.z, tmp2.z, dir.z);

        renderable.worldTransform.set(rotation);
        renderable.worldTransform.setTranslation(translation);

//        Matrix4 bla = (Matrix4)renderable.userData;
//        if( bla != null) {
//            renderable.worldTransform.set(bla);
//        }

        // Set world transformation matrix
        set(u_worldTrans, renderable.worldTransform);


        // Get time attribute
        //TimeAttribute attr = (TimeAttribute)renderable.material.get(TimeAttribute.ID);
        //set(u_time, attr == null ? 0.0f : attr.value);

        // Render mesh
        renderable.mesh.render(program, renderable.primitiveType, renderable.meshPartOffset, renderable.meshPartSize);
    }


    private static String createPrefix(final long mask, final long attributes, boolean lighting, boolean fog, int numDirectional, int numPoint, int numSpot) {
        String prefix = "";
        if (((attributes & VertexAttributes.Usage.Color) == VertexAttributes.Usage.Color) || ((attributes & VertexAttributes.Usage.ColorPacked) == VertexAttributes.Usage.ColorPacked))
            prefix += "#define colorFlag\n";
        if ((attributes & VertexAttributes.Usage.Normal) == VertexAttributes.Usage.Normal) {
            prefix += "#define normalFlag\n";
            if (lighting) {
                prefix += "#define lightingFlag\n";
                prefix += "#define ambientCubemapFlag\n";
                prefix += "#define numDirectionalLights "+numDirectional+"\n";
                prefix += "#define numPointLights "+numPoint+"\n";

                if (fog) {
                    prefix += "#define fogFlag\n";
                }
            }
        }

        if ((mask & BlendingAttribute.Type) == BlendingAttribute.Type)
            prefix += "#define "+BlendingAttribute.Alias+"Flag\n";
        if ((mask & TextureAttribute.Diffuse) == TextureAttribute.Diffuse)
            prefix += "#define "+TextureAttribute.DiffuseAlias+"Flag\n";
        if ((mask & TextureAttribute.Normal) == TextureAttribute.Normal)
            prefix += "#define "+TextureAttribute.NormalAlias+"Flag\n";
        if ((mask & ColorAttribute.Diffuse) == ColorAttribute.Diffuse)
            prefix += "#define "+ColorAttribute.DiffuseAlias+"Flag\n";
        if ((mask & ColorAttribute.Specular) == ColorAttribute.Specular)
            prefix += "#define "+ColorAttribute.SpecularAlias+"Flag\n";
        if ((mask & FloatAttribute.Shininess) == FloatAttribute.Shininess)
            prefix += "#define "+FloatAttribute.ShininessAlias+"Flag\n";
        if ((mask & FloatAttribute.AlphaTest) == FloatAttribute.AlphaTest)
            prefix += "#define "+FloatAttribute.AlphaTestAlias+"Flag\n";

        return prefix;
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