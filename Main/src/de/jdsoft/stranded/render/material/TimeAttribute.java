package de.jdsoft.stranded.render.material;


import com.badlogic.gdx.graphics.g3d.materials.Material;

public class TimeAttribute extends Material.Attribute {
    public final static String Alias = "Time";
    public final static long ID = register(Alias);

    public float value;

    public TimeAttribute(final float value) {
        super(ID);
        this.value = value;
    }

    @Override
    public Material.Attribute copy () {
        return new TimeAttribute(value);
    }

    @Override
    protected boolean equals(Material.Attribute other) {
        return false;
    }

}
