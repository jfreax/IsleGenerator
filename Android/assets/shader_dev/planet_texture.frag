#ifdef GL_ES
    precision mediump float;
#endif

uniform float time;
uniform vec2 resolution;
uniform sampler2D u_texture;
varying vec4 v_color;

uniform mat4 u_projTrans;


#ifdef GL_ES
varying mediump vec2 v_texCoords;
#else
varying vec2 v_texCoords;
#endif

void main() {
    vec4 color = v_color;

    vec2 texPosition = (v_texCoords * 2.0f) - 1.0f;
    //texPosition.x += time;

    float x = gl_FragCoord.x/resolution.x;
    float y = gl_FragCoord.y/resolution.y;
    float z = gl_FragCoord.z; // Already in range [0,1]

    // Converting from range [0,1] to NDC [-1,1]
    float ndcx = x * 2.0 - 1.0;
    float ndcy = y * 2.0 - 1.0;
    float ndcz = z * 2.0 - 1.0;
    vec3 ndc = vec3(ndcx, ndcy, ndcz);

    float len = length(texPosition);
    //float len = length( ndc );
    //color *= smoothstep(0.5, 0.495, len);

    //vec2 texPosition = (v_texCoords * 2.0f) - 1.0f;
    vec2 texCoord = v_texCoords;
    //texCoord += vec2(time); // / 360.f);

    color *= texture2D(u_texture, texCoord);
    color *= 1.0 - step(0.5, len);

    gl_FragColor = v_color;

}