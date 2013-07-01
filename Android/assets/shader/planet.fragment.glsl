#ifdef GL_ES
    precision mediump float;
#endif

uniform mat4 u_projTrans;

uniform sampler2D u_texture;
uniform sampler2D u_diffuseTexture;
uniform float u_shininess;

varying vec3 v_normal;
varying vec4 v_color;

varying vec3 v_lightDiffuse;
//uniform float u_time;


#ifdef GL_ES
varying mediump vec2 v_texCoords0;
#else
varying vec2 v_texCoords0;
#endif

// Globals
float u_time = u_shininess;

// Forward declarations
vec3 permute(vec3 x);
float cnoise(vec2 P);
float pnoise(vec2 P, vec2 rep);
float field(in vec3 p);
float field(in vec2 p);

float snoise(vec2 v);

vec2 calcCoord1( vec2 coord ) {
    vec2 ret = vec2(0,0);
    ret.x += cos(coord.y / 16.f) + (permute(vec3(coord, 1.0f)).y) * 0.01f;
    ret.y += u_time * 0.02f;
    ret.x += u_time * ((permute(vec3(coord, 1.0f)).x) / 3890.f); 

    return ret;
}


void main() {

    vec4 fragColor;
    vec3 normal = v_normal;
    vec4 diffuse = texture2D(u_diffuseTexture, v_texCoords0);

    fragColor.rgb = (diffuse.rgb * v_lightDiffuse);


    vec4 color = texture2D(u_texture, v_texCoords0); // * v_color


    vec2 coord = calcCoord1( v_texCoords0 );

    vec2 p = vec2(coord / 4.) + vec2(2., -1.3);
    p += 0.1 * vec2(sin(u_time / 16.), sin(u_time / 12.));

    vec3 p2 = vec3(v_texCoords0 / 2., 0) + vec3(2., -1.3, -1.);
    p2 += 0.1 * vec3(sin(u_time / 16.), sin(u_time / 12.),  sin(u_time / 128.));

    vec2 coord2 = v_texCoords0;
    coord2.x += tan(coord.y / 6.f) * (coord.x/32.f);
    coord2.x += u_time * 0.01f;
    coord2.y -= u_time * 0.03f;


    float n = field(p2)*0.4f; // + field(vec3(coord.x, p2.z, p2.x))*0.1f; // + cnoise(coord2 * 4.f) / 2.f;
    //cnoise(coord * 3.f) / 6.f + 
    //float n = pnoise(coord * 3.f, vec2(1.f, 10.f));
    //float n = pnoise( calcCoord1( v_texCoords0 ), calcCoord1(vec2(1.f, 1.f)) );
    //float n = pnoise(coord * 3.f, calcCoord1(vec2(1.f, 1.f)));
    //        + pnoise(coord2 * 4.f, vec2(1.f, 11.f));
    //        + field(p2)*0.5f;

    //n = step(0.9f, 1.0f - field(p2)*0.5f);

    vec4 cloudColor = vec4( 0.6 + 0.5 * vec3(n, n, n), 1.0f );
    //cloudColor = 1.0f - cloudColor;
    //cloudColor = smoothstep(0.5f, 0.7f, cloudColor);
    cloudColor -= 0.5;
    cloudColor *= 2.f;  
    //cloudColor = 1.0 - cloudColor;
    cloudColor = smoothstep(0.22f, 0.7f, cloudColor);

    // To remove hard edge after 360°
    float removeHardEdge = 1.0f - ( smoothstep(0.90f, 1.0f, v_texCoords0.x) );
    removeHardEdge *= ( smoothstep(0.0f, 0.08f, v_texCoords0.x) );

    diffuse = mix(cloudColor, vec4(0.f), 1.0f - removeHardEdge) + diffuse;
    fragColor.rgb = (diffuse.rgb * v_lightDiffuse);

    gl_FragColor = fragColor;
    //gl_FragColor = cloudColor * fragColor;
}


vec4 mod289(vec4 x)
{
  return x - floor(x * (1.0 / 289.0)) * 289.0;
}

vec4 permute(vec4 x)
{
  return mod289(((x*34.0)+1.0)*x);
}

vec4 taylorInvSqrt(vec4 r)
{
  return 1.79284291400159 - 0.85373472095314 * r;
}

vec2 fade(vec2 t) {
  return t*t*t*(t*(t*6.0-15.0)+10.0);
}

// Classic Perlin noise
float cnoise(vec2 P)
{
  vec4 Pi = floor(P.xyxy) + vec4(0.0, 0.0, 1.0, 1.0);
  vec4 Pf = fract(P.xyxy) - vec4(0.0, 0.0, 1.0, 1.0);
  Pi = mod289(Pi); // To avoid truncation effects in permutation
  vec4 ix = Pi.xzxz;
  vec4 iy = Pi.yyww;
  vec4 fx = Pf.xzxz;
  vec4 fy = Pf.yyww;

  vec4 i = permute(permute(ix) + iy);

  vec4 gx = fract(i * (1.0 / 41.0)) * 2.0 - 1.0 ;
  vec4 gy = abs(gx) - 0.5 ;
  vec4 tx = floor(gx + 0.5);
  gx = gx - tx;

  vec2 g00 = vec2(gx.x,gy.x);
  vec2 g10 = vec2(gx.y,gy.y);
  vec2 g01 = vec2(gx.z,gy.z);
  vec2 g11 = vec2(gx.w,gy.w);

  vec4 norm = taylorInvSqrt(vec4(dot(g00, g00), dot(g01, g01), dot(g10, g10), dot(g11, g11)));
  g00 *= norm.x;
  g01 *= norm.y;
  g10 *= norm.z;
  g11 *= norm.w;

  float n00 = dot(g00, vec2(fx.x, fy.x));
  float n10 = dot(g10, vec2(fx.y, fy.y));
  float n01 = dot(g01, vec2(fx.z, fy.z));
  float n11 = dot(g11, vec2(fx.w, fy.w));

  vec2 fade_xy = fade(Pf.xy);
  vec2 n_x = mix(vec2(n00, n01), vec2(n10, n11), fade_xy.x);
  float n_xy = mix(n_x.x, n_x.y, fade_xy.y);
  return 2.3 * n_xy;
}

vec3 permute(vec3 x) { return mod(((x*34.0)+1.0)*x, 289.0); }


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
        //accum = mod(accum, 1.f);
        tw += w;
        prev = mag;
    }
    return max(.1, 5. * accum / tw - .7);
}

float field(in vec2 p) {
  return field(vec3(p, 1.0f));
}

// Classic Perlin noise, periodic variant
float pnoise(vec2 P, vec2 rep)
{
  vec4 Pi = floor(P.xyxy) + vec4(0.0, 0.0, 1.0, 1.0);
  vec4 Pf = fract(P.xyxy) - vec4(0.0, 0.0, 1.0, 1.0);
  Pi = mod(Pi, rep.xyxy); // To create noise with explicit period
  Pi = mod289(Pi);        // To avoid truncation effects in permutation
  vec4 ix = Pi.xzxz;
  vec4 iy = Pi.yyww;
  vec4 fx = Pf.xzxz;
  vec4 fy = Pf.yyww;

  vec4 i = permute(permute(ix) + iy);

  vec4 gx = fract(i * (1.0 / 41.0)) * 2.0 - 1.0 ;
  vec4 gy = abs(gx) - 0.5 ;
  vec4 tx = floor(gx + 0.5);
  gx = gx - tx;

  vec2 g00 = vec2(gx.x,gy.x);
  vec2 g10 = vec2(gx.y,gy.y);
  vec2 g01 = vec2(gx.z,gy.z);
  vec2 g11 = vec2(gx.w,gy.w);

  vec4 norm = taylorInvSqrt(vec4(dot(g00, g00), dot(g01, g01), dot(g10, g10), dot(g11, g11)));
  g00 *= norm.x;  
  g01 *= norm.y;  
  g10 *= norm.z;  
  g11 *= norm.w;  

  float n00 = dot(g00, vec2(fx.x, fy.x));
  float n10 = dot(g10, vec2(fx.y, fy.y));
  float n01 = dot(g01, vec2(fx.z, fy.z));
  float n11 = dot(g11, vec2(fx.w, fy.w));

  vec2 fade_xy = fade(Pf.xy);
  vec2 n_x = mix(vec2(n00, n01), vec2(n10, n11), fade_xy.x);
  float n_xy = mix(n_x.x, n_x.y, fade_xy.y);
  return 2.3 * n_xy;
}