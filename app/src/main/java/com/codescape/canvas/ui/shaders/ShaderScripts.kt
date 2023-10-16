package com.codescape.canvas.ui.shaders

import org.intellij.lang.annotations.Language

// From Skia Shaders Playground: https://shaders.skia.org/?id=de2a4d7d893a7251eb33129ddf9d76ea517901cec960db116a1bbd7832757c1f
@Language("AGSL")
const val SHADER_1 =
    """
    uniform float2 iResolution;
    uniform float iTime;
    uniform float2 iMouse;
    
    // Source: @notargs https://twitter.com/notargs/status/1250468645030858753
    float f(vec3 p) {
        p.z -= iTime * 10.;
        float a = p.z * .1;
        p.xy *= mat2(cos(a), sin(a), -sin(a), cos(a));
        return .1 - length(cos(p.xy) + sin(p.yz));
    }
    
    half4 main(vec2 fragcoord) { 
        vec3 d = .5 - fragcoord.xy1 / iResolution.y;
        vec3 p=vec3(0);
        for (int i = 0; i < 32; i++) {
          p += f(p) * d;
        }
        return ((sin(p) + vec3(2, 5, 9)) / length(p)).xyz1;
    }
    """

// From Skia Shaders Playground: https://shaders.skia.org/?id=e0ec9ef204763445036d8a157b1b5c8929829c3e1ee0a265ed984aeddc8929e2
@Language("AGSL")
const val SHADER_2 =
    """
    uniform float2 iResolution;
    uniform float iTime;
    uniform float2 iMouse;
    
    // Star Nest by Pablo Roman Andrioli
    
    // This content is under the MIT License.
    
    const int iterations = 17;
    const float formuparam = 0.53;
    
    const int volsteps = 20;
    const float stepsize = 0.1;
    
    const float zoom  = 0.800;
    const float tile  = 0.850;
    const float speed =0.010 ;
    
    const float brightness =0.0015;
    const float darkmatter =0.300;
    const float distfading =0.730;
    const float saturation =0.850;
    
    
    half4 main( in vec2 fragCoord )
    {
        //get coords and direction
        vec2 uv=fragCoord.xy/iResolution.xy-.5;
        uv.y*=iResolution.y/iResolution.x;
        vec3 dir=vec3(uv*zoom,1.);
        float time=iTime*speed+.25;
    
        //mouse rotation
        float a1=.5+iMouse.x/iResolution.x*2.;
        float a2=.8+iMouse.y/iResolution.y*2.;
        mat2 rot1=mat2(cos(a1),sin(a1),-sin(a1),cos(a1));
        mat2 rot2=mat2(cos(a2),sin(a2),-sin(a2),cos(a2));
        dir.xz*=rot1;
        dir.xy*=rot2;
        vec3 from=vec3(1.,.5,0.5);
        from+=vec3(time*2.,time,-2.);
        from.xz*=rot1;
        from.xy*=rot2;
        
        //volumetric rendering
        float s=0.1,fade=1.;
        vec3 v=vec3(0.);
        for (int r=0; r<volsteps; r++) {
            vec3 p=from+s*dir*.5;
            p = abs(vec3(tile)-mod(p,vec3(tile*2.))); // tiling fold
            float pa,a=pa=0.;
            for (int i=0; i<iterations; i++) { 
                p=abs(p)/dot(p,p)-formuparam; // the magic formula
                a+=abs(length(p)-pa); // absolute sum of average change
                pa=length(p);
            }
            float dm=max(0.,darkmatter-a*a*.001); //dark matter
            a*=a*a; // add contrast
            if (r>6) fade*=1.-dm; // dark matter, don't render near
            //v+=vec3(dm,dm*.5,0.);
            v+=fade;
            v+=vec3(s,s*s,s*s*s*s)*a*brightness*fade; // coloring based on distance
            fade*=distfading; // distance fading
            s+=stepsize;
        }
        v=mix(vec3(length(v)),v,saturation); //color adjust
        return vec4(v*.01,1.);	
        
    }
    """

// From Skia Shaders Playground: https://shaders.skia.org/?id=2bee4488820c3253cd8861e85ce3cab86f482cfddd79cfd240591bf64f7bcc38
@Language("AGSL")
const val SHADER_3 =
    """
    uniform float2 iResolution;
    uniform float iTime;
    uniform float2 iMouse;    

    // Source: @XorDev https://twitter.com/XorDev/status/1475524322785640455
    vec4 main(vec2 FC) {
      vec4 o = vec4(0);
      vec2 p = vec2(0), c=p, u=FC.xy*2.-iResolution.xy;
      float a;
      for (float i=0; i<4e2; i++) {
        a = i/2e2-1.;
        p = cos(i*2.4+iTime+vec2(0,11))*sqrt(1.-a*a);
        c = u/iResolution.y+vec2(p.x,a)/(p.y+2.);
        o += (cos(i+vec4(0,2,4,0))+1.)/dot(c,c)*(1.-p.y)/3e4;
      }
      return o;
    }
    """
