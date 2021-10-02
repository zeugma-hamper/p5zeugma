
package p5zeugma;


import zeugma.*;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.event.MouseEvent;
import processing.opengl.PGraphicsOpenGL;

import java.util.HashMap;


public class P5ZLivingMaes  extends PApplet
{ public PlatonicMaes vital_maes;
  public Bolex vital_cammy;
  public P5ZApplet vital_fuehrer;
  public DialectCatcher vital_interpreter;
  
  public int display_if_fullscreen;
  
  public static HashMap <PGraphicsOpenGL, PlatonicMaes> maes_by_gfx_handle
    = new HashMap <> ();

  public class DialectCatcher
  { public void mouseEvent (MouseEvent e)
      { int tion = e . getAction ();
        switch (tion)
          { case MouseEvent.CLICK: case MouseEvent.ENTER:
            case MouseEvent.EXIT: case MouseEvent.WHEEL:
              return;
          }
        double xnrm = (double)(e . getX ()) / (double)width  -  0.5;  // ouch, to say the least.
        double ynrm = 0.5  -  (double)(e . getY ()) / (double)height;
        PlatonicMaes ma = vital_maes;
        if (ma == null)
            return;
        Vect hit = ma.loc.val . Add (ma.ovr.val . Mul (ma.wid.val * xnrm))
                              . Add (ma.upp.val . Mul (ma.hei.val * ynrm));
        Vect n = ma.ovr.val . Cross (ma.upp.val) . Norm ();
        n . MulAcc (0.8 * ma.wid.val);
        Vect eye = hit . Add (n);
        Vect aim = ma.upp.val . Cross (ma.ovr.val) . Norm ();
        int b = e . getButton ();
        long butt = 0;
        if (b != 0)
          butt |= ((b == PConstants.LEFT)  ?  (0x01 << 0)
                   :  ((b == PConstants.CENTER)  ?  (0x01 << 1)
                       :  ((b == PConstants.RIGHT)  ?  (0x01 << 2)  :  0)));
        // next in the unending onslaught of oy:
        // (this is because upstream the 'RELEASE' event has the 'button' var
        // set to the button that's being released, but zeugma triggers on the
        // change of a bit in the overall button bitfield.
        if (tion == MouseEvent.RELEASE)
          butt = 0;
        // now, meanwhile: how bad is this really, making
        // the mouse event masquerade as wand input?
        vital_fuehrer.spaque . InterpretRawWandish ("mouse-0", butt, eye,
        											aim, ma.ovr.val);
      }
  }

  public P5ZLivingMaes (P5ZApplet boese_fuehrer, int dspl_no)
    { super ();
      vital_maes = null;
      vital_cammy = null;
      vital_fuehrer = boese_fuehrer;
      vital_interpreter = new DialectCatcher ();
      
      display_if_fullscreen = dspl_no;

      registerMethod ("mouseEvent", vital_interpreter);

      if (boese_fuehrer != null)
        PApplet.runSketch (new String[] { this . getClass () . getName () },
    	                   this);
    }
  
  public void FullscreenOnDisplay (int d)
    { display_if_fullscreen = d; }

  public void PleaseDoNotFullscreen ()
    { display_if_fullscreen *= (display_if_fullscreen > 0  ?  -1  :  1); }

  public void ActuallyDraw (PGraphicsOpenGL ogl)
    { ogl . background (160, 20, 20); }
  // the angry red foregoing is one that we should never see: sumpin'd be wrong.
  // that's because every direct instance of this class should be calling the
  // overridden ActuallyDraw() method belonging to a subclass instance; see
  // the last line of draw(), below.


  public void settings ()
    { if (display_if_fullscreen  <  1)
    	size (960, 540, P3D);
      else
    	fullScreen (P3D, display_if_fullscreen);
    }

  public void setup ()
    { surface . setTitle ("little billy");
      //
    }

  public void draw ()
    { PGraphics g = getGraphics ();
      if (! (g instanceof PGraphicsOpenGL)
    	  ||  vital_maes == null
    	  ||  vital_cammy == null)
    	return;

      PlatonicMaes ma = vital_maes;
      Bolex cam = vital_cammy;

      PGraphicsOpenGL ogl = (PGraphicsOpenGL)g;
      Vect e = cam . ViewLoc ();
      Vect n = cam . ViewAim ();
      Vect u = cam . ViewUp ();
      Vect o = n . Cross (u . Norm ()) . Norm () ;
      Vect c = e . Add (n . Mul (cam . ViewDist ()));
        
      ogl . camera ((float)e.x, (float)e.y, (float)e.z,
                    (float)c.x, (float)c.y, (float)c.z,
                    (float)u.x, (float)u.y, (float)u.z);
      if (cam . ProjectionType ()  ==  Bolex.ProjType.PERSPECTIVE)
        { double asp = Math.tan (0.5 * cam . ViewHorizAngle ())
    	                 /  Math.tan (0.5 * cam . ViewVertAngle ());
          double fvy = cam . ViewVertAngle ();
          ogl . perspective ((float)fvy, (float)asp,
        		  			 (float)cam . NearClipDist (),
        		  			 (float)cam . FarClipDist ());
        }
      else if (cam . ProjectionType ()  ==  Bolex.ProjType.ORTHOGRAPHIC)
        { double hlf_w = cam . ViewDist ()
    	  		               * Math.tan (0.5 * cam . ViewHorizAngle ());
          double hlf_h = cam . ViewDist ()
                           * Math.tan (0.5 * cam . ViewVertAngle ());
          ogl . ortho (-(float)hlf_w, (float)hlf_w,
        		  	       -(float)hlf_h, (float)hlf_h);
        }
      else
        { // well... what?
        }

      // perhaps the following should be switched via some global flag...
      ogl . applyProjection (1.0f,  0.0f,  0.0f,  0.0f,
                             0.0f, -1.0f,  0.0f,  0.0f,
                             0.0f,  0.0f,  1.0f,  0.0f,
                             0.0f,  0.0f,  0.0f,  1.0f);

      maes_by_gfx_handle . put (ogl, ma);
      if (vital_fuehrer == null)
        this . ActuallyDraw (ogl);
      else
        vital_fuehrer . ActuallyDraw (ogl);
    }
}
