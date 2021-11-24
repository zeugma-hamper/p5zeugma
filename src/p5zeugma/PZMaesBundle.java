
package p5zeugma;


import zeugma.*;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PMatrix3D;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import processing.opengl.PGraphicsOpenGL;

import java.awt.*;

import java.util.ArrayList;
import java.util.HashMap;


public class PZMaesBundle  extends PApplet
{ public PlatonicMaes its_maes;
  public Bolex its_cammy;
  public PZAlignifer its_backplate;
  public PZApplet der_leiter;
  public DialectCatcher vital_interpreter;

  public int display_id;

  public long last_limned_ratchet = -1;

  public static ArrayList <PZMaesBundle> all_maes_bundles
    = new ArrayList <> ();

  public static HashMap <PGraphicsOpenGL, PlatonicMaes> maes_by_gfx_handle
    = new HashMap <> ();

  public class DialectCatcher
  { public void mouseEvent (MouseEvent e)
      { int tion = e . getAction ();
        switch (tion)
          { case MouseEvent.CLICK:
            case MouseEvent.ENTER:
            case MouseEvent.EXIT:
            case MouseEvent.WHEEL:
              return;
          }
        double xnrm = (double)(e . getX ()) / (double)width  -  0.5;  // ouch, to say the least.
        double ynrm = 0.5  -  (double)(e . getY ()) / (double)height;
        PlatonicMaes ma = its_maes;
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
        der_leiter.spaque . InterpretRawWandish ("mouse-0", butt, eye,
                                                    aim, ma.ovr.val);
      }

    public void keyEvent (KeyEvent e)
      { int tion = e . getAction ();
        if (tion  ==  KeyEvent.TYPE)
          return;
        long mods = 0;
        int ms = e . getModifiers ();
        if ((ms & KeyEvent.SHIFT)  >  0)  mods |= ZEYowlEvent.MODK_SHIFT;
        if ((ms & KeyEvent.CTRL)   >  0)  mods |= ZEYowlEvent.MODK_CTRL;
        if ((ms & KeyEvent.ALT)    >  0)  mods |= ZEYowlEvent.MODK_ALT;
        if ((ms & KeyEvent.META)   >  0)  mods |= ZEYowlEvent.MODK_META;
        der_leiter.yowque
          . InterpretRawKeyfulness ("keyboard-0", tion != KeyEvent.RELEASE,
                                    e . getKey (), e . getKeyCode (), mods);
      }
  }

  public PZMaesBundle (PZApplet boese_leiter, int dspl_no)
    { super ();
      its_maes = null;
      its_cammy = null;
      its_backplate = new PZAlignifer();
      der_leiter = boese_leiter;
      vital_interpreter = new DialectCatcher ();

      display_id = dspl_no;

      registerMethod ("mouseEvent", vital_interpreter);
      registerMethod ("keyEvent", vital_interpreter);

      if (boese_leiter != null)
        new Thread () {
          public void run ()
            { PApplet.runSketch
                (new String[] { this . getClass () . getName () },
                 PZMaesBundle.this);
            }
        } . start ();

      all_maes_bundles . add (this);
    }


  public PlatonicMaes ItsMaes ()
    { return its_maes; }

  public Bolex ItsCamera ()
    { return its_cammy; }

  public PZAlignifer ItsBackplate ()
    { return its_backplate; }


  public void FullscreenOnDisplay (int d)
    { display_id = d; }

  public void PleaseDoNotFullscreen ()
    { display_id *= (display_id > 0  ?  -1  :  1); }


  public void DrawAllLayers (PGraphicsOpenGL ogl, ArrayList <LimnyThing> lrs)
    { if (der_leiter != null  &&  ! der_leiter.well_and_truly_ready)
        return;

      ogl . hint (DISABLE_DEPTH_TEST);
      ZeColor co = its_maes . BackgroundColor ();
      ogl . background ((float)(255.0 * co.r), (float)(255.0 * co.g),
                        (float)(255.0 * co.b), (float)(255.0 * co.a));
      long ratch = -1;
      if (der_leiter != null  &&  der_leiter.global_looper != null)
        ratch = der_leiter.global_looper . RecentestRatchet ();

      CumuMats cm = new CumuMats ();
      cm.rat_fresh = ratch;

      ZeColor adjc = its_maes. AdjColor ();

      if (der_leiter != null)
        { ogl . push ();
          GrapplerPile gp = its_backplate . AssuredGrapplerPile ();
          PZ.ConcatModelView (ogl, gp . PntMat (), gp . InvPntMat ());
          der_leiter. PZDraw (ogl, this,
                                (float)(its_maes.wid . Val ()),
                                (float)(its_maes.hei . Val ()));
          ogl . pop ();
        }

      for (LimnyThing lay  :  lrs)
        if (lay instanceof PZLimnable)
          ((PZLimnable)lay) . RecursivelyDraw (ogl, ratch, cm, adjc);
    }


  public void settings ()
    { if (display_id  <  1) {
        size(960, 540, P3D);
        //size (960, 540, "info.fathom.hydra.HydraGraphics");
      } else {
        fullScreen(P3D, display_id);
      }
    }

  public void setup ()
    { surface . setTitle ("little billy");
      //
    }

  protected void _ActuallyDraw (PGraphicsOpenGL ogl, long ratch)
    { if (ratch > 0  &&  ratch <= last_limned_ratchet)
        { // System.out.println ("ZOINKS! re-rendering frame at maes/ratchet "
          //                     +  its_maes. Name () + " @ "
          //                     +  its_maes. toString () + " / "
          //                    +  Long.toString (ratch));
          return;
        }
      // else
      //   System.out.println ("FINE: rendering frame at maes/ratchet "
      //                       +  its_maes. Name () + " @ "
      //                       +  its_maes. toString () + " / "
      //                       +  Long.toString (ratch));

      PlatonicMaes ma = its_maes;
      Bolex cam = its_cammy;

      Vect e = cam . ViewLoc ();
      Vect n = cam . ViewAim ();
      Vect u = cam . ViewUp ();
      // Vect o = n . Cross (u . Norm ()) . Norm () ;
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
      DrawAllLayers (ogl, its_maes.layers);

      last_limned_ratchet = ratch;
    }


  public void draw ()
    { PGraphics g = getGraphics ();
      if (! (g instanceof PGraphicsOpenGL)
          ||  its_maes == null
          ||  its_cammy == null)
        return;

      long ratch = -1;
      if (der_leiter != null  &&  der_leiter.global_looper != null)
        ratch = der_leiter.global_looper . RecentestRatchet ();

      PGraphicsOpenGL ogl = (PGraphicsOpenGL)g;

      _ActuallyDraw (ogl, ratch);
    }


  public static PlatonicMaes.MaesAndHit ClosestAmongLiving (Vect frm, Vect aim)
    { Vect cls_hit = null;
      PlatonicMaes ma, cls_maes = null;
      double cls_dst = -1.0;
      for (PZMaesBundle mb  :  all_maes_bundles)
        if ((ma = mb.its_maes) != null)
          { Vect hit = Geom.RayRectIntersection (frm, aim, ma.loc.val,
                                                 ma.ovr.val, ma.upp.val,
                                                 ma.wid.val, ma.hei.val);
            if (hit != null)
              { double d = hit . Sub (frm) . AutoDot ();
                if (cls_dst < 0.0  ||  d < cls_dst)
                  { cls_dst = d;
                    cls_hit = hit;
                    cls_maes = ma;
                  }
              }
          }
      return (cls_maes == null)  ?  null
          :  new PlatonicMaes.MaesAndHit (cls_maes, cls_hit);
    }

//
/// following: some conveni-enttes.
//
  public static PMatrix3D ToP (Matrix44 m)
    { return PZ.ToP (m); }

  public static void PZConcatModelView (PGraphicsOpenGL g,
                                        Matrix44 fwd_mat, Matrix44 inv_mat)
    { PZ.ConcatModelView (g, fwd_mat, inv_mat); }

  public static void PZModelViewFlipYAxis (PGraphicsOpenGL g)
    { PZ.ModelViewFlipYAxis (g); }
//
///
//

}
