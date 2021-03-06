#ifdef GL_ES
    precision mediump float;
#endif

uniform float u_time;
uniform sampler2D u_texture;
varying vec4 v_color;

uniform mat4 u_projTrans;


#ifdef GL_ES
varying mediump vec2 v_texCoords;
#else
varying vec2 v_texCoords;
#endif


// http://www.fractalforums.com/new-theories-and-research/very-simple-formula-for-fractal-patterns/
float field(in vec3 p) {
    float strength = 7. + .03 * log(1.e-6 + fract(sin(u_time) * 4373.11));
    float accum = 0.;
    float prev = 0.;
    float tw = 0.;
    for (int i = 0; i < 48; ++i) {
        float mag = dot(p, p);
        p = abs(p) / mag + vec3(-.51, -.4, -1.3);
        float w = exp(-float(i) / 7.);
        accum += w * exp(-strength * pow(abs(mag - prev), 2.3));
        tw += w;
        prev = mag;
    }
    return max(.1, 5. * accum / tw - .7);
}

vec4 cloud(vec2 coord, vec2 res, float time) {
    vec2 uv = 1.0 * coord / res - 1.0;
    vec2 uvs = uv * res / max(res.x, res.y);

    vec3 p = vec3(uvs / 4., 0) + vec3(2., -1.3, -1.);
    p += 0.1 * vec3(sin(time / 16.), sin(time / 12.),  sin(time / 128.));


    float t = pow(field(p), .2);

    return vec4(t, t, 0, 1.0);
}

void main() {
    vec4 color = texture2D(u_texture, v_texCoords); // * v_color

    vec2 texPosition = (v_texCoords * 2.0f) - 1.0f;


    float len = length(texPosition);

    color.a = 0.5f;
    color.rgb *= 1.0 - step(0.9, len);



    // Randomness starter values
    vec3 p = vec3(texPosition / 4., 0) + vec3(2., -1.3, -1.);
    p += 0.1f * vec3((u_time / 16.f));
    //p += 0.1 * vec3(sin(u_time / 16.), sin(u_time / 12.),  sin(u_time / 128.));

    //float circleDiameter = field(p) / 40.f;
    float circleDiameter = 0.01f;
    float r = 0.55;
    float softness = 0.15f;


    //float t = pow(field(p), .2);
    //float t = 2.f;
    //color = vec4( t/4.f, t/3.f, t/2.f, t/2.f);
    color = vec4( 0.14, 0.18, 0.31, 0.0f);

    //color *= vec4( vec3( smoothstep(r, r+softness, len) ), 1.0f );
    //color *= vec4( vec3( smoothstep(r, r+0.02f, len) ), 1.0f );
    color *= vec4( vec3( 1.0f - smoothstep(r+circleDiameter, r+circleDiameter+softness, len) ), 1.0f );

    color *= smoothstep( r-0.09, r, len);
    //color *= vec4( vec3( 1.0f - smoothstep(r-softness, r+softness, len) ), 1.0f );

    gl_FragColor = color;
}