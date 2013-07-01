package de.jdsoft.stranded.map.planet;


import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.materials.FloatAttribute;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import de.jdsoft.stranded.model.PlanetModel;

public class Planet implements Disposable {

    final public ModelInstance planetModel;
    private float time = 0.f;

    // update manually!
    private Vector3 position = new Vector3(0.f, 0.f, 0.f);
    private float angle = 0.1f;

    public Planet() {
        planetModel = PlanetModel.create();

    }


    public void setPosition( Vector3 position ) {
        planetModel.transform.setToTranslation(position);
    }

    public void getPosition(Vector3 position) {
        planetModel.transform.getTranslation(position);
    }

    public float getRadius() {
        return planetModel.transform.getValues()[Matrix3.M00];
    }


    public void update(float delta) {
        time += delta;

        FloatAttribute flattr = (FloatAttribute)(planetModel.materials.first().get(FloatAttribute.Shininess));
        flattr.value = time;

        // Rotate around own axes
        //planetModel.transform.rotate(Vector3.Y, 0.1f);


        // Rotate around given center
        Vector3 tmpVec = new Vector3(0.f, 0.f, 0.f); // Center position
        Vector3 axis = new Vector3(0.0f, 0.0f, 1.0f);

        planetModel.transform.getTranslation(position);

        tmpVec.set(position).sub(new Vector3(0.0f, 0.0f, 0.0f));

        planetModel.transform.translate(-tmpVec.x, -tmpVec.y, -tmpVec.z).inv();
        planetModel.transform.rotate(axis, angle * delta * 20.f);
        planetModel.transform.translate(tmpVec);
    }

    public void dispose() {

    }
}
