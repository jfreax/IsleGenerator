package de.jdsoft.stranded.map.planet;


import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.materials.FloatAttribute;
import com.badlogic.gdx.utils.Disposable;
import de.jdsoft.stranded.model.PlanetModel;

public class Planet implements Disposable {

    final public ModelInstance planetModel;

    public Planet() {
        planetModel = PlanetModel.create();
    }

    public void setTime( float time ) {
        FloatAttribute flattr = (FloatAttribute)(planetModel.materials.first().get(FloatAttribute.Shininess));
        flattr.value = time;
    }

    public void dispose() {

    }
}
