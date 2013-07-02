package de.jdsoft.stranded.model;


import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.materials.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.materials.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ShortArray;

import java.util.Random;

public class SphereBuilder {

    /** The vertex attributes of the resulting mesh */
    private VertexAttributes attributes;
    /** The vertices to construct, no size checking is done */
    private FloatArray vertices = new FloatArray();
    /** The indices to construct, no size checking is done */
    private ShortArray indices = new ShortArray();
    /** The size (in number of floats) of each vertex */
    /** The current vertex index, used for indexing */
    private short vindex;
    /** The offset in the indices array when begin() was called, used to define a meshpart. */
    private int istart;
    /** The offset within an vertex to position */
    private int posOffset;
    /** The size (in number of floats) of the position attribute */
    private int posSize;
    /** The offset within an vertex to normal, or -1 if not available */
    private int norOffset;
    /** The offset within an vertex to color, or -1 if not available */
    private int colOffset;
    /** The size (in number of floats) of the color attribute */
    private int colSize;
    /** The offset within an vertex to packed color, or -1 if not available */
    private int cpOffset;
    /** The offset within an vertex to texture coordinates, or -1 if not available */
    private int uvOffset;
    /** The meshpart currently being created */
    private MeshPart part;
    /** The parts created between begin and end */
    private Array<MeshPart> parts = new Array<MeshPart>();
    /** The color used if no vertex color is specified. */
    private final Color color = new Color();
    /** Whether to apply the default color. */
    private boolean colorSet;

    private float[] vertex;

    private final MeshPartBuilder.VertexInfo vertTmp3 = new MeshPartBuilder.VertexInfo();
    private final Vector3 tempV1 = new Vector3();
    private final Vector3 tempV2 = new Vector3();
    private int primitiveType;



    static public Model createNew(Texture texture, Pixmap heightmap, String id, long attribute, float width, float height, float depth, int divisionsU, int divisionsV) {
        Mesh mesh = createNewMesh(id, attribute, width, height, depth, divisionsU, divisionsV, heightmap);

        ModelBuilder modelBuilder = new ModelBuilder();

        modelBuilder.begin();
        modelBuilder.part(id, mesh, GL10.GL_TRIANGLES,
                new Material(
                          new TextureAttribute(TextureAttribute.Diffuse, texture)
                        , new FloatAttribute(FloatAttribute.Shininess, 0.f)
                      //, new BlendingAttribute(GL10.GL_ONE, GL10.GL_ONE)
                ));
        return modelBuilder.end();
    }


    static public Mesh createNewMesh(String id, long attribute, float width, float height, float depth, int divisionsU, int divisionsV, Pixmap heightmap) {
        SphereBuilder sb = new SphereBuilder();
        sb.begin(attribute);
        sb.part(id, GL10.GL_TRIANGLES);
        sb.sphere(width, height, depth, divisionsU, divisionsV, heightmap);
        return sb.end();
    }


    /** Begin building a mesh.
     * @param attributes bitwise mask of the {@link com.badlogic.gdx.graphics.VertexAttributes.Usage},
     * only Position, Color, Normal and TextureCoordinates is supported. */
    public void begin(final long attributes) {
        begin(createAttributes(attributes), 0);
    }

    /** Begin building a mesh */
    public void begin(final VertexAttributes attributes, int primitiveType) {
        if (this.attributes != null)
            throw new RuntimeException("Call end() first");
        this.attributes = attributes;
        this.vertices.clear();
        this.indices.clear();
        this.parts.clear();
        this.vindex = 0;
        this.istart = 0;
        this.part = null;
        /* The size (in number of floats) of each vertex */
        int stride = attributes.vertexSize / 4;
        this.vertex = new float[stride];
        VertexAttribute a = attributes.findByUsage(VertexAttributes.Usage.Position);
        if (a == null)
            throw new GdxRuntimeException("Cannot build mesh without position attribute");
        posOffset = a.offset / 4;
        posSize = a.numComponents;
        a = attributes.findByUsage(VertexAttributes.Usage.Normal);
        norOffset = a == null ? -1 : a.offset / 4;
        a = attributes.findByUsage(VertexAttributes.Usage.Color);
        colOffset = a == null ? -1 : a.offset / 4;
        colSize = a == null ? 0 : a.numComponents;
        a = attributes.findByUsage(VertexAttributes.Usage.ColorPacked);
        cpOffset = a == null ? -1 : a.offset / 4;
        a = attributes.findByUsage(VertexAttributes.Usage.TextureCoordinates);
        uvOffset = a == null ? -1 : a.offset / 4;
        setColor(null);
        this.primitiveType = primitiveType;
    }

    /** Starts a new MeshPart. The mesh part is not usable until end() is called */
    public MeshPart part(final String id, int primitiveType) {
        if (this.attributes == null)
            throw new RuntimeException("Call begin() first");
        endpart();

        part = new MeshPart();
        part.id = id;
        this.primitiveType = part.primitiveType = primitiveType;
        parts.add(part);

        setColor(null);

        return part;
    }

    private void endpart() {
        if (part != null) {
            part.indexOffset = istart;
            part.numVertices = indices.size - istart;
            istart = indices.size;
            part = null;
        }
    }

    public Mesh end() {
        if (this.attributes == null)
            throw new RuntimeException("Call begin() first");
        endpart();

        final Mesh mesh = new Mesh(true, vertices.size, indices.size, attributes);
        mesh.setVertices(vertices.items, 0, vertices.size);
        mesh.setIndices(indices.items, 0, indices.size);

        for (MeshPart p : parts)
            p.mesh = mesh;
        parts.clear();

        attributes = null;
        vertices.clear();
        indices.clear();

        return mesh;
    }

    public void setColor(final Color color) {
        if ((colorSet = color != null))
            this.color.set(color);
    }


    /** @param usage bitwise mask of the {@link com.badlogic.gdx.graphics.VertexAttributes.Usage},
     * only Position, Color, Normal and TextureCoordinates is supported. */
    public static VertexAttributes createAttributes(long usage) {
        final Array<VertexAttribute> attrs = new Array<VertexAttribute>();
        if ((usage & VertexAttributes.Usage.Position) == VertexAttributes.Usage.Position)
            attrs.add(new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE));
        if ((usage & VertexAttributes.Usage.Color) == VertexAttributes.Usage.Color)
            attrs.add(new VertexAttribute(VertexAttributes.Usage.Color, 4, ShaderProgram.COLOR_ATTRIBUTE));
        if ((usage & VertexAttributes.Usage.Normal) == VertexAttributes.Usage.Normal)
            attrs.add(new VertexAttribute(VertexAttributes.Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE));
        if ((usage & VertexAttributes.Usage.TextureCoordinates) == VertexAttributes.Usage.TextureCoordinates)
            attrs.add(new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+"0"));
        final VertexAttribute attributes[] = new VertexAttribute[attrs.size];
        for (int i = 0; i < attributes.length; i++)
            attributes[i] = attrs.get(i);
        return new VertexAttributes(attributes);
    }

    public void sphere(float width, float height, float depth, int divisionsU, int divisionsV) {
        sphere(width, height, depth, divisionsU, divisionsV, null);
    }


    public void sphere(float width, float height, float depth, int divisionsU, int divisionsV, Pixmap heightmap) {
        final float hw = width * 0.5f;
        final float hh = height * 0.5f;
        final float hd = depth * 0.5f;
        final float stepU = MathUtils.PI2 / divisionsU;
        final float stepV = MathUtils.PI / divisionsV;
        final float us = 1f / divisionsU;
        final float vs = 1f / divisionsV;
        float u, v, angleU, angleV;
        MeshPartBuilder.VertexInfo curr1 = vertTmp3.set(null, null, null, null);
        curr1.hasUV = curr1.hasPosition = curr1.hasNormal = true;
        for (int i = 0; i <= divisionsU; i++) {
            angleU = stepU * i;
            u = 1f - us * i;
            tempV1.set(MathUtils.cos(angleU) * hw, 0f, MathUtils.sin(angleU) * hd);
            for (int j = 0; j <= divisionsV; j++) {
                angleV = stepV * j;
                v = vs * j;

                Vector3 middle = new Vector3();
                Vector3 bottom = new Vector3();
                Vector3 left = new Vector3();
                Vector3 right = new Vector3();
                Vector3 top = new Vector3();
                float hMiddle = 0.f;
                if( heightmap != null ) {
                    final float stepw = us;
                    final float steph = vs;

                    getHeightFromHeightMap(heightmap, angleU, angleV);
                    hMiddle = getHeightFromHeightMap(heightmap, angleU, angleV);
                    float hBottom = getHeightFromHeightMap(heightmap, angleU, angleV - steph);
                    float hLeft = getHeightFromHeightMap(heightmap, angleU - stepw, angleV);
                    float hRight = getHeightFromHeightMap(heightmap, angleU + stepw, angleV);
                    float hTop = getHeightFromHeightMap(heightmap, angleU, angleV + steph);

                    hMiddle = normalizeHeight(hMiddle);
                    hBottom = normalizeHeight(hBottom);
                    hLeft = normalizeHeight(hLeft);
                    hRight = normalizeHeight(hRight);
                    hTop = normalizeHeight(hTop);

                    polarToCarth(angleU, angleV, hw, hd, hh, hMiddle, middle ); // middle point
                    polarToCarth(angleU-stepw, angleV, hw, hd, hh, hLeft, left );
                    polarToCarth(angleU+stepw, angleV, hw, hd, hh, hRight, right );
                    polarToCarth(angleU, angleV-steph, hw, hd, hh, hBottom, bottom );
                    polarToCarth(angleU, angleV+steph, hw, hd, hh, hTop, top );

                    //
                    right.sub(middle);
                    left.sub(middle);
                    top.sub(middle);
                    bottom.sub(middle);

                    tempV1.set(right).crs(top).nor();
                    tempV1.add(tempV2.set(top).crs(left).nor());
                    tempV1.add(tempV2.set(left).crs(bottom).nor());
                    tempV1.add(tempV2.set(bottom).crs(right).nor());

                    tempV1.nor();

                    // Set normal vector
                    curr1.normal.set(tempV1);

                } else { // Without height map
                    curr1.normal.set(curr1.position).nor();
                }

                polarToCarth(angleU, angleV, hw, hd, hh, hMiddle, curr1.position );

                curr1.uv.set(u, v);
                vertex(curr1);
                if (i == 0 || j == 0)
                    continue;
                // FIXME don't duplicate lines and points
                index((short)(vindex-2), (short)(vindex-1), (short)(vindex-(divisionsV+2)),
                        (short)(vindex-1), (short)(vindex-(divisionsV+1)), (short)(vindex-(divisionsV+2)));
            }
        }
    }

    private float getHeightFromHeightMap(Pixmap heightmap, float angleU, float angleV) {
        return (heightmap.getPixel((int) (((MathUtils.PI2-angleU)  * heightmap.getWidth()) / MathUtils.PI2),
                (int) ((angleV * heightmap.getHeight()) / MathUtils.PI)) & 0xff) / 255.f;
    }

    private float normalizeHeight(float height) {

        return (float)(Math.exp(2.77258872 * height) - 1) / 100.f;
    }

    private void polarToCarth(float angleU, float angleV, float hw, float hd, float hh, float heightOffset, Vector3 out) {
        float t = MathUtils.sin(angleV);
        tempV1.set(MathUtils.cos(angleU) * (hw+heightOffset), 0f, MathUtils.sin(angleU) * (hd+heightOffset));
        out.set(tempV1.x * t, MathUtils.cos(angleV) * (hh+heightOffset), tempV1.z * t);
    }


    public short vertex(Vector3 pos, Vector3 nor, Color col, Vector2 uv) {
        if (col == null && colorSet) {
            col = color;
        }
        if (pos != null) {
            vertex[posOffset  ] = pos.x;
            if (posSize > 1) vertex[posOffset+1] = pos.y;
            if (posSize > 2) vertex[posOffset+2] = pos.z;
        }
        if (nor != null && norOffset >= 0) {
            vertex[norOffset  ] = nor.x;
            vertex[norOffset+1] = nor.y;
            vertex[norOffset+2] = nor.z;
        }
        if (col != null) {
            if (colOffset >= 0) {
                vertex[colOffset  ] = col.r;
                vertex[colOffset+1] = col.g;
                vertex[colOffset+2] = col.b;
                if (colSize > 3) vertex[colOffset+3] = col.a;
            } else if (cpOffset > 0)
                vertex[cpOffset] = col.toFloatBits(); // FIXME cache packed color?
        }
        if (uv != null && uvOffset >= 0) {
            vertex[uvOffset  ] = uv.x;
            vertex[uvOffset+1] = uv.y;
        }
        vertices.addAll(vertex);
        return (vindex++);
    }


    public short vertex(final MeshPartBuilder.VertexInfo info) {
        return vertex(info.hasPosition ? info.position : null, info.hasNormal ? info.normal : null,
                info.hasColor ? info.color : null, info.hasUV ? info.uv : null);
    }


    public void index(short value1, short value2, short value3, short value4, short value5, short value6) {
        indices.ensureCapacity(6);
        indices.add(value1);
        indices.add(value2);
        indices.add(value3);
        indices.add(value4);
        indices.add(value5);
        indices.add(value6);
    }
}
