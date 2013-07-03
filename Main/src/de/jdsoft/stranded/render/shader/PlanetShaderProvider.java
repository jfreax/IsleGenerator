package de.jdsoft.stranded.render.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.materials.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.shaders.GLES10Shader;
import com.badlogic.gdx.graphics.g3d.utils.BaseShaderProvider;


public class PlanetShaderProvider extends BaseShaderProvider {

    @Override
    protected Shader createShader (Renderable renderable) {
        if(renderable.material.has(TextureAttribute.Diffuse)) {
            if (Gdx.graphics.isGL20Available()) {
                return new DefaultShader(
                          Gdx.files.internal("shader/default.vertex.glsl").readString()
                        , Gdx.files.internal("shader/planet.fragment.glsl").readString()
                        , renderable.material, renderable.mesh.getVertexAttributes()
                        , renderable.lights != null
                        , renderable.lights != null && renderable.lights.fog != null
                        , 2, 5, 3
                        , renderable.bones == null ? 0 : 12);
            }
            return new GLES10Shader();
        } else { // Return effect shader
            return new PlanetShader("shader/planet_effect.vertex.glsl", "shader/planet_effect.fragment.glsl");
        }
    }
}
