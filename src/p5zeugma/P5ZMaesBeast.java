
package p5zeugma;


import zeugma.*;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.event.MouseEvent;
import processing.opengl.PGraphicsOpenGL;


public class P5ZMaesBeast  extends PApplet
{ public PlatonicMaes beast_maes;
  public Bolex beast_cammy;
  public P5ZApplet beast_fuehrer;
  public DialectCatcher beast_interpreter;

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
        PlatonicMaes ma = beast_maes;
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
        beast_fuehrer.spaque . InterpretRawWandish ("mouse-0", butt, eye,
        											aim, ma.ovr.val);
      }
  }

  public P5ZMaesBeast (P5ZApplet boese_fuehrer)
    { super ();
      beast_maes = null;
      beast_cammy = null;
      beast_fuehrer = boese_fuehrer;
      beast_interpreter = new DialectCatcher ();

      registerMethod ("mouseEvent", beast_interpreter);

      if (boese_fuehrer != null)
        PApplet.runSketch (new String[] { this . getClass () . getName () },
    	                   this);
    }

  public void ActuallyDraw (PGraphicsOpenGL ogl)
    { ogl . background (40);}


  public void settings ()
    { size (960, 540, P3D);
      //fullScreen (P3D, 3);
    }

  public void setup ()
    { surface . setTitle ("little billy");
      //
    }

  public void draw ()
    { PGraphics g = getGraphics ();
      if (! (g instanceof PGraphicsOpenGL)
    	  ||  beast_maes == null
    	  ||  beast_cammy == null)
    	return;

      PlatonicMaes ma = beast_maes;
      Bolex cam = beast_cammy;

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

      ogl . applyProjection (1.0f,  0.0f,  0.0f,  0.0f,
                             0.0f, -1.0f,  0.0f,  0.0f,
                             0.0f,  0.0f,  1.0f,  0.0f,
                             0.0f,  0.0f,  0.0f,  1.0f);
      
      if (beast_fuehrer == null)
    	this . ActuallyDraw (ogl);
      else
    	beast_fuehrer . ActuallyDraw (ogl);
    }
}
