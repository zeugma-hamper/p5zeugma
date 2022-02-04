
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

  public PGraphics most_recent_pgraphics = null;

  public double actual_to_ideal_pixel_ratio = 1.0;

  public int display_id;

  public long last_limned_ratchet = -1;

  public static boolean permit_window_resize = false;

  public boolean shunt_raw_key_events_to_der_leiter = false;
  public static boolean neutralize_esc_key_armageddon = false;

  public final static int default_win_wid = 960;
  public final static int default_win_hei = 540;

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
        Vect hit = ma.loc.val . Add (ma.ovr.val . Mul (ma.cur_wid * xnrm))
                              . Add (ma.upp.val . Mul (ma.cur_hei * ynrm));
        Vect n = ma.ovr.val . Cross (ma.upp.val) . Norm ();
        Vect aim = n . Neg ();
        n . MulAcc (0.8 * ma.wid.val);
        Vect eye = hit . Add (n);
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
      { if (shunt_raw_key_events_to_der_leiter)
          { if (PZMaesBundle.this != der_leiter  &&  der_leiter != null)
              der_leiter . handleKeyEvent (e);
            // actually, makes sense not to bypass the ZeEvent transformation
            // & handling below, even if shunting raw events, so we won't ...
            // return;
          }

        int tion = e . getAction ();
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

        if (e . getKey () == 27  &&  neutralize_esc_key_armageddon)
          /*e.*/key = 0;   // gott im himmel... schrecklich!
      }
  }
//
/// bittersweet terminus of nested class DialectCatcher
//


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


  public void InternalizeMaes (PlatonicMaes ma)
    { if (ma == null)
        return;

      its_maes = ma;

      its_cammy = PlatonicMaes.CameraFromMaes (ma);

      its_backplate . AlignToMaes (ma);
      its_backplate . LocGrapplerZoftVect ()
        . BecomeLike (ma . LocZoft ());
      its_maes . AppendLayer (its_backplate);
    }


  public PlatonicMaes ItsMaes ()
    { return its_maes; }

  public String ItsMaesName ()
    { return (its_maes == null)  ?  ""  :  its_maes . Name (); }

  public Bolex ItsCamera ()
    { return its_cammy; }

  public PZAlignifer ItsBackplate ()
    { return its_backplate; }

  // thanks, awt, for trampling on the classname 'List'. Genius!
  public static java.util.List <PZMaesBundle> AllMaesBundles ()
    { return all_maes_bundles; }

  public static PZMaesBundle NthMaesBundle (int ind)
    { return all_maes_bundles . get (ind); }

  public static PZMaesBundle MaesBundleByMaes (PlatonicMaes ma)
    { for (PZMaesBundle mb  :  all_maes_bundles)
        if (mb . ItsMaes ()  ==  ma)
          return mb;
      return null;
    }

  public static PZMaesBundle MaesBundleByMaesName (String mname)
    { PlatonicMaes ma;
      for (PZMaesBundle mb  :  all_maes_bundles)
        if ((ma = mb . ItsMaes ())  ==  ma)
          if (ma . Name () . equals (mname))
            return mb;
      return null;
    }

  public double ActualToIdealPixelRatio ()
    { return actual_to_ideal_pixel_ratio; }

  public void SetActualToIdealPixelRatio (double atipr)
    { actual_to_ideal_pixel_ratio = atipr; }


  public void FullscreenOnDisplay (int d)
    { display_id = d; }

  public void PleaseDoNotFullscreen ()
    { display_id *= (display_id > 0  ?  -1  :  1); }


  public static boolean ShouldPermitWindowResize ()
    { return permit_window_resize; }

  public static void SetShouldPermitWindowResize (boolean pwr)
    { permit_window_resize = pwr; }


  public static boolean ShouldNeutralizeESCKeyArmageddon ()
    { return neutralize_esc_key_armageddon; }

  public static void SetShouldNeutralizeESCKeyArmageddon (boolean neka)
    { neutralize_esc_key_armageddon = neka; }


  public boolean ShouldForwardRawKeyEventsToLeader ()
    { return shunt_raw_key_events_to_der_leiter; }

  public void SetShouldForwardRawKeyEventsToLeader (boolean frketl)
    { shunt_raw_key_events_to_der_leiter = frketl; }

  public static void SetShouldForwardRawKeyEventsFromAllWindowsToLeader
                                                                 (boolean frwd)
    { for (PZMaesBundle mbun  :  all_maes_bundles)
        if (mbun != null)
          mbun . SetShouldForwardRawKeyEventsToLeader (frwd);
    }


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

          int hold_w = der_leiter.width;
          int hold_h = der_leiter.height;
          der_leiter.width = this.width;
          der_leiter.height = this.height;

          der_leiter. PZDraw (ogl, this,
                              (float)(its_maes.wid . Val ()),
                              (float)(its_maes.hei . Val ()));

          der_leiter.width = hold_w;
          der_leiter.height = hold_h;

          ogl . pop ();
        }

      for (LimnyThing lay  :  lrs)
        if (lay instanceof PZLimnable)
          ((PZLimnable)lay) . RecursivelyDraw (ogl, ratch, cm, adjc);
    }


  public void settings ()
    { if (display_id  <  1)
        { int win_wid = default_win_wid;
          int win_hei = default_win_hei;
          if (its_maes != null
              &&  its_maes.requested_pixwid > 0
              &&  its_maes.requested_pixhei > 0)
            { win_wid = (int)(its_maes.requested_pixwid);
              win_hei = (int)(its_maes.requested_pixhei);
            }
          size (win_wid, win_hei, P3D);

          if (its_maes != null)
            { its_maes.pixwid = win_wid;
              its_maes.pixhei = win_hei;
            }
          //size (wid, hei, "info.fathom.hydra.HydraGraphics");
        }
      else
        { fullScreen (P3D, display_id);
          //
        }
    }

  public void setup ()
    { surface . setTitle ("little billy");
      //
      surface . setResizable (permit_window_resize);
    }

  protected void _ActuallyDraw (PGraphicsOpenGL ogl, long ratch)
    { if (ratch > 0  &&  ratch <= last_limned_ratchet)
        { // System.out.println ("ZOINKS! re-rendering frame at maes/ratchet "
          //                     +  its_maes. Name () + " @ "
          //                     +  its_maes. toString () + " / "
          //                    +  Long.toString (ratch));
          return;
        }

      most_recent_pgraphics = ogl;

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

      boolean changed_aspra = false;

      double orig_aspra = its_maes.wid.val / its_maes.hei.val;
      double prev_pix_aspra = its_maes.cur_wid / its_maes.cur_hei;
      double pix_aspra = (double)this.width / (double)this.height;

      if (its_maes.cur_wid < 0.0  ||  its_maes.cur_hei < 0.0)
        { its_maes.cur_wid = its_maes.wid.val;
          its_maes.cur_hei = its_maes.hei.val;
        }

      if (this.width != its_maes.pixwid  ||  this.height != its_maes.pixhei)
        { its_maes.pixwid = this.width;
          its_maes.pixhei = this.height;
          PlatonicMaes.RefreshCameraFromMaesAndPixelWH (its_cammy, its_maes);

          if (pix_aspra != prev_pix_aspra)
            { changed_aspra = true;

              if (pix_aspra > orig_aspra)
                { its_maes.cur_wid = its_maes.hei.val * pix_aspra;
                  its_maes.cur_hei = its_maes.hei.val;
                }
              else if (pix_aspra < orig_aspra)
                { its_maes.cur_wid = its_maes.wid.val;
                  its_maes.cur_hei = its_maes.wid.val / pix_aspra;
                }
              else
                { its_maes.cur_wid = its_maes.wid.val;
                  its_maes.cur_hei = its_maes.hei.val;
                }
            }
        }

      // the whole agglomeration of hoo-hah below -- i.e. everything
      // except for the "_ActuallyDraw()" line -- exists to allow
      // programs to specify an "as-if" pixel size that's different
      // from the window's (or, more generally, rendering region's)
      // actual pixular size.
      //
      // very specifically, by the time execution reaches this point,
      // the surrounding Processing (tm) machinery has already used
      // the 'width' and 'height' variables in a call to glViewport(),
      // so that mapping is set. now we temporarily/heretically set
      // those same variables to the as-if values; code within
      // "_ActuallyDraw()" that uses Processing's pixel-based rendering
      // API -- e.g. "text()" or "circle()" with non-3D arguments --
      // will consequently function as if the rendering space had the
      // "as-if" pixel extent. Y'know?


      boolean feign_pixel_size
        = (its_maes.as_if_pixwid > 0  &&  its_maes.as_if_pixhei > 0);

      if (feign_pixel_size)
        { double aiw = (double)its_maes.as_if_pixwid;
          double aih = (double)its_maes.as_if_pixhei;

          if (changed_aspra)
            { if (pix_aspra > orig_aspra)
                aiw = aih * pix_aspra;
              else if (pix_aspra < orig_aspra)
                aih = aiw / pix_aspra;
            }

          this.width = (int)aiw;
          this.height = (int)aih;
        }

      _ActuallyDraw (ogl, ratch);

      if (feign_pixel_size)
        { this.width = (int)its_maes.pixwid;
          this.height = (int)its_maes.pixhei;
        }
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
