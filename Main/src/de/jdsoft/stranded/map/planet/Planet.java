package de.jdsoft.stranded.map.planet;


import android.renderscript.Matrix4f;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.materials.FloatAttribute;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import de.jdsoft.stranded.model.PlanetModel;

public class Planet implements Disposable {

    final public ModelInstance planetModel;
    private float time = 0.f;
    private Matrix4 transform = new Matrix4();

    // update manually!
    private Vector3 position = new Vector3(0.f, 0.f, 0.f);

    private float angle = 0.1f;
    private Vector3 orbit = new Vector3(0, 0, 0);

    // Temp
    Vector3 tmpVec = new Vector3();

    public Planet() {
        planetModel = PlanetModel.create();
//        planetModel.calculateTransforms();

    }


    public void setPosition( Vector3 position ) {
        planetModel.transform.setToTranslation(position);
    }

    public void getPosition(Vector3 position) {
        planetModel.transform.getTranslation(position);
    }

    public float getRadius() {
        return planetModel.transform.getValues()[Matrix3.M00] * 0.5f;
    }


    public void update(float delta) {
        time += delta;

        FloatAttribute flattr = (FloatAttribute)(planetModel.materials.first().get(FloatAttribute.Shininess));
        flattr.value = time;

        // Rotate around own axes
//        planetModel.transform.rotate(Vector3.Y, 0.1f);


        // Rotate around given center
        Vector3 axis = new Vector3(0.0f, 0.0f, 1.0f);
        Quaternion quaternion = new Quaternion();
//        planetModel.transform.getRotation(quaternion);


        planetModel.transform.getTranslation(position);

        tmpVec.set(orbit).sub(position);

        planetModel.transform.translate(tmpVec.x, tmpVec.y, tmpVec.z); //.inv();
//        planetModel.transform.setToTranslation(orbit);
//        planetModel.transform.getTranslation(position);

        planetModel.transform.rotate(axis, angle * delta * 120.f);
//        planetModel.transform.getTranslation(position);

//        tmpVec.rotate(axis, angle * delta * 120.f);
        planetModel.transform.translate(-tmpVec.x, -tmpVec.y, -tmpVec.z);
//        planetModel.transform.setToTranslation(position);

//        val[M03] = vector.x;
//        val[M13] = vector.y;
//        val[M23] = vector.z;
//
//        planetModel.transform.rotate(Vector3.Y, 1.5f);
////        planetModel.calculateTransforms();
        planetModel.transform.getTranslation(position);

//        planetModel.transform.set(quaternion);
/*        planetModel.transform.rotate(Vector3.Y, 1.5f);
        planetModel.calculateTransforms();

        planetModel.transform.rotate(axis, angle * delta * 120.f);
        planetModel.transform.getTranslation(position);
        tmpVec.set(position).sub(orbit);
        planetModel.transform.translate(-tmpVec.x, -tmpVec.y, -tmpVec.z);
        planetModel.transform.rotate(axis, -angle * delta * 120.f);
        planetModel.transform.translate(-tmpVec.x, -tmpVec.y, -tmpVec.z).inv();*/




    }

    public void dispose() {

    }

    public void setOrbit(Vector3 orbit) {
        this.orbit = orbit;
    }
}
