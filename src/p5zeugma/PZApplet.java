
package p5zeugma;


import zeugma.*;

import oscP5.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

import java.util.Random;

import processing.opengl.PGraphicsOpenGL;


public class PZApplet  extends PZMaesBundle
{ public static PZApplet sole_instance = null;
  public static boolean well_and_truly_ready = false;

  public Loopervisor global_looper;

  OscP5 osc_slurper;

  UninvitedHost uniho;

  protected SpatialAqueduct spaque;
  protected YowlAqueduct yowque;

  protected Matrix44 raw_to_room_point_mat;
  protected Matrix44 raw_to_room_direc_mat;

  protected List <PlatonicMaes> maeses = new ArrayList <> ();

  protected CursorHerd cherd;

  protected static PlatonicMaes dummy_maes;

  static
    { dummy_maes
        = new PlatonicMaes (new Vect (-1000000.0, 2000000.0, -80000000.0),
                            Vect.negxaxis, Vect.yaxis,
                            0.01, 0.01);
      PlatonicMaes m = dummy_maes;
      m.pixwid = m.as_if_pixwid = m.requested_pixwid = 160;
      m.pixhei = m.as_if_pixhei = m.requested_pixhei = 90;
    }


  public class UninvitedHost
    { public void pre ()
        { if (global_looper != null)
            global_looper. OnceMoreUntoTheBreath ();

          for (PZMaesBundle mbun  :  PZMaesBundle.AllMaesBundles ())
            if (mbun != null)
              mbun . FreshenInnards ();
        }

      public void oscEvent (OscMessage mess)
        { //String addr = mess . addrPattern ();
          //String ttag = mess . typetag ();
          //println ("IN: " + cnt + "th message; addrpatt: {" + addr + "} with typetag [" + ttag + "]");

          String wname = mess . get (0) . stringValue ();
          long butts = mess . get (1) . longValue ();
          Vect pos = new Vect (mess . get (2) . doubleValue (),
                               mess . get (3) . doubleValue (),
                               mess . get (4) . doubleValue ());
          Vect aim = new Vect (mess . get (5) . doubleValue (),
                               mess . get (6) . doubleValue (),
                               mess . get (7) . doubleValue ());
          Vect ovr = new Vect (mess . get (8) . doubleValue (),
                               mess . get (9) . doubleValue (),
                               mess . get (10) . doubleValue ());

          if (raw_to_room_point_mat != null)
              raw_to_room_point_mat . TransformVectInPlace (pos);
          else
            System.out.println
              ("Wafna! Freakin' threads... raw_to_room_point_mat not "
               + "loaded yet...");

          if (raw_to_room_direc_mat != null)
            { raw_to_room_direc_mat . TransformVectInPlace (aim);
              raw_to_room_direc_mat . TransformVectInPlace (ovr);
            }
          else
            System.out.println
              ("Merde! raw_to_room_direc_mat not yet loaded: thread kuso.");

          PZMaesBundle mb;
          PlatonicMaes.MaesAndHit mah
            = PlatonicMaes.ClosestAmong (maeses, pos, aim);
          if (mah != null  &&  (mb = MaesBundleByMaes (mah.maes))  != null)
            spaque . InterpretRawWandish (wname, butts, null, pos, aim, ovr,
                                          mah.maes, mb . ItsCamera (), mah.hit);
          else
            spaque . InterpretRawWandish (wname, butts, null, pos, aim, ovr);
          // see, the null foregoing is the 'caress events' argument.
        }
    }
//
/// hereabove: farewell to the flesh of nested class UninvitedHost
//

  static Random randolph = new Random ();

  public class Cursoresque  extends Alignifer
                            implements PZLimnable, ZESpatialPhagy
  { //public Vect wall_pos;
    public List <SinuVect> vs_lrg, vs_sml;
    public ZoftThing <ZeColor> iro;
    public PlatonicMaes cur_maes;

    public static Map <String, Double> scale_factor_by_maes = null;

    public Cursoresque (double sz, int nv)
      { vs_lrg = new ArrayList <SinuVect> ();
        vs_sml = new ArrayList <SinuVect> ();
        iro = new ZoftColor (1.0f, 0.6f);
        cur_maes = null;
        for (int w = 0  ;  w < 2  ;  ++w)
          for (int q = 0  ;  q < nv  ;  ++q)
            { double theeta
                = 2.0 * Math.PI / (double)nv * (double)q + (double)w * Math.PI;
              Vect radial
                = new Vect ((Vect.xaxis . Mul (Math.cos (theeta))
                             . Add (Vect.yaxis . Mul (Math.sin (theeta))))
                                        . Mul (0.5 * ((double)w + 1.0)));
              SinuVect arm
                = new SinuVect (radial . Mul (sz * 0.065),
                                0.8  +  0.11 * randolph . nextDouble (),
                                radial . Mul (sz * 0.24
                                              * (1.0 + 3.0 * (double)(q%2))));
              ((w > 0) ? vs_lrg : vs_sml) . add (arm);
            }
      }

    public void DrawSelf (PGraphicsOpenGL g)
      { Vect v;
        if (cur_maes  !=  PZMaesBundle.maes_by_gfx_handle . get (g))
          return;
        g . noStroke ();
        ZeColor c = iro.val;
        g . fill (255.0f * c.r, 255.0f * c.g, 255.0f * c.b, 255.0f * c.a);
        g . beginShape ();
        for (SinuVect sv  :  vs_lrg)
          { v = sv.val;
            g . vertex ((float)v.x, (float)v.y, (float)v.z);
          }
        g . endShape (CLOSE);

        g . beginShape ();
        for (SinuVect sv  :  vs_sml)
          { v = sv.val;
            g . vertex ((float)v.x, (float)v.y, (float)v.z);
          }
        g . endShape (CLOSE);
      }

    public long ZESpatialMove (ZESpatialMoveEvent e)
      { PlatonicMaes.MaesAndHit mah
          = PlatonicMaes.ClosestAmong (maeses, e.loc, e.aim);
        if (mah != null)
          { LocZoft () . Set (mah.hit);
            if (cur_maes  !=  mah.maes)
              { AlignToMaes (cur_maes = mah.maes);
                if (scale_factor_by_maes != null)
                  { Double s = scale_factor_by_maes . get (mah.maes . Name ());
                    double ess = (s != null)  ?  s  :  1.0;
                    SetScale (ess);
                  }
              }
          }
        return 0;
      }
    public long  ZESpatialHarden (ZESpatialHardenEvent e)
      { // System.out.println ("HARDEN! er... from " + e.prov);
        return 0;
      }
    public long  ZESpatialSoften (ZESpatialSoftenEvent e)
      { // System.out.println ("your pal " + e.prov + " gives it the ol' SOFTEN");
        return 0;
      }
  }
//
/// just now: auf wiedersehen to nested class Cursoresque...
//


  public class CursorHerd  extends PZLimnyThing implements ZESpatialPhagy
  { public Map <String, Cursoresque> cursor_by_wand = new HashMap <> ();

    public boolean PassTheBuckUpPhageHierarchy ()
      { return true; }   // so that all ZESpatial* events end up in ZESpatial()

    public long ZESpatial (ZESpatialEvent e)
      { String prv = e . Provenance ();
        Cursoresque crs = cursor_by_wand . get (prv);
        if (crs == null)
          { crs = new Cursoresque (80.0, 6);
            cursor_by_wand . put (prv, crs);
            AppendChild (crs);
          }
        return e . ProfferAsQuaffTo (crs);
      }

    public Map <String, Cursoresque> CursorsByWand ()
      { return cursor_by_wand; }

    public Collection <Cursoresque> AllCursors ()
      { return cursor_by_wand . values (); }
  }
//
/// adios to nested class CursorHerd
//


  public static Matrix44 ConjureMatrix44 (JSONArray ja)
    { if (ja == null  ||  ja . length ()  !=  16)
        return null;
      return new Matrix44
          (ja . getDouble (0),  ja . getDouble (1),
                                      ja . getDouble (2),  ja . getDouble (3),
           ja . getDouble (4),  ja . getDouble (5),
                                      ja . getDouble (6),  ja . getDouble (7),
           ja . getDouble (8),  ja . getDouble (9),
                                      ja . getDouble (10), ja . getDouble (11),
           ja . getDouble (12), ja . getDouble (13),
                                      ja . getDouble (14), ja . getDouble (15));
  }

  public static Vect ConjureVect (JSONArray ja)
    { if (ja == null  ||  ja . length ()  !=  3)
        return null;
      return new Vect (ja . getDouble (0),
                       ja . getDouble (1),
                       ja . getDouble (2));
    }


  public static Path ResolvePathInADesperateJavaWorld (String fname)
    { return ResolvePathInADesperateJavaWorld (Path.of (fname)); }

  public static Path ResolvePathInADesperateJavaWorld (Path pth)
    { if (pth == null)
        return pth;
      if (! Files.exists (pth)  &&  ! pth . isAbsolute ())
        { try
            { String fname = pth . toString ();
              // look for things relative to the 'ole jar file
              // from which PZApplet has emerged
              URL classUrl = PZApplet.class . getProtectionDomain ()
                . getCodeSource () . getLocation ();
              String decodedPath = classUrl.toURI () . getSchemeSpecificPart ();
              File jarFolder = new File (decodedPath) . getParentFile ();
              File soughtFile = new File (jarFolder, fname);
              pth = soughtFile . toPath ();
            }
          catch (Exception e)
            { System.err . println ("oh for pity's sake");
              e . printStackTrace ();
              return null;
            }
        }
      return pth;
    }


  public static JSONObject LoadJSONObjectFromFile (String fname)
    { return LoadJSONObjectFromFile (Path.of (fname)); }

  public static JSONObject LoadJSONObjectFromFile (Path pth)
    { String raw_s;
      pth = ResolvePathInADesperateJavaWorld (pth);
      try
        { raw_s = Files.readString (pth); }
      catch (IOException ex)
        { System.out.println
            ("Churlish exception hit while reading file <" + pth
             + "> into string, with this message:\n" + ex + "\n");
          return null;
        }
      JSONObject jobj = new JSONObject (raw_s);
      return jobj;
    }


  public static JSONArray LoadJSONArrayFromFile (String fname)
    { return LoadJSONArrayFromFile (Path.of (fname)); }

  public static JSONArray LoadJSONArrayFromFile (Path pth)
    { String raw_s;
      pth = ResolvePathInADesperateJavaWorld (pth);
      try
        { raw_s = Files.readString (pth); }
      catch (IOException ex)
        { System.out.println
            ("Wondrous exception while reading file <" + pth
             + "> into string, bearing this message:\n" + ex + "\n");
          return null;
        }
      JSONArray jarr = new JSONArray (raw_s);
      return jarr;
    }


  public Matrix44 RawToRoomPointMatrix ()
    { return raw_to_room_point_mat; }

  public Matrix44 RawToRoomDirectionMatrix ()
    { return raw_to_room_direc_mat; }


  public void SetRawToRoomPointMatrix (Matrix44 rtrpm)
    { if (raw_to_room_point_mat == null)
        raw_to_room_point_mat = new Matrix44 ();
      if (rtrpm != null)
        raw_to_room_point_mat . Load (rtrpm);
    }

  public void SetRawToRoomDirectionMatrix (Matrix44 rtrdm)
    { if (raw_to_room_direc_mat == null)
        raw_to_room_direc_mat = new Matrix44 ();
      if (rtrdm != null)
        raw_to_room_direc_mat . Load (rtrdm);
    }

  public void SetRawToRoomMatrices (Matrix44 rtrpm, Matrix44 rtrdm)
    { SetRawToRoomPointMatrix (rtrpm);
      SetRawToRoomDirectionMatrix (rtrdm);
    }


  public void HooverCoordTransforms ()
    { JSONObject jace
        = LoadJSONObjectFromFile ("config/coord-xform-raw-to-room.json");
      if (jace == null)
        jace = LoadJSONObjectFromFile
          ("/opt/trelopro/config/json/coord-xform-raw-to-room.json");

      if (jace == null)
        { System.out.println
            ("Alas: no room coord transform file available at "
             + "</opt/trelopro/config/json/coord-xform-raw-to-room.json>");
          raw_to_room_direc_mat = new Matrix44 ();
          raw_to_room_point_mat = new Matrix44 ();
          return;
        }

      JSONArray dm = jace . getJSONArray ("direc_mat");
      JSONArray pm = jace . getJSONArray ("point_mat");

      SetRawToRoomPointMatrix (ConjureMatrix44 (pm));
      SetRawToRoomDirectionMatrix (ConjureMatrix44 (dm));
    }


  public void HooverMaeses ()
    { JSONArray jarr = LoadJSONArrayFromFile ("config/maes-config.json");
      if (jarr == null)
        jarr = LoadJSONArrayFromFile
          ("/opt/trelopro/config/json/maes-config.json");

      // if still no success, we should, you know, say a little something
      // and then at least have the good grace to set the hapless system
      // up with some default maes or other... no?
      int num = jarr . length ();
      JSONObject ob;
      for (int q = 0  ;  q < num  ;  ++q)
        { if ((ob = jarr . getJSONObject (q))  ==  null)
            continue;
          if (ob . isNull ("name")  ||  ob . isNull ("location")  ||
              ob . isNull ("over")  ||  ob . isNull ("up")  ||
              ob . isNull ("width")  ||  ob . isNull ("height"))
            continue;

          PlatonicMaes ma = new PlatonicMaes ();
          ma . SetName (ob . getString ("name"));
          ma.loc = new ZoftVect (ConjureVect (ob . getJSONArray ("location")));
          ma.ovr = new ZoftVect (ConjureVect (ob . getJSONArray ("over")));
          ma.upp = new ZoftVect (ConjureVect (ob . getJSONArray ("up")));
          ma.wid = new ZoftFloat (ma.cur_wid = ob . getDouble ("width"));
          ma.hei = new ZoftFloat (ma.cur_hei = ob . getDouble ("height"));

          if (! (ob . isNull ("as-if-pixwid")
                 ||  ob . isNull ("as-if-pixhei")))
            { ma.as_if_pixwid = ob . getLong ("as-if-pixwid");
              ma.as_if_pixhei = ob . getLong ("as-if-pixhei");
              ma.cur_as_if_pwid = ma.as_if_pixwid;
              ma.cur_as_if_phei = ma.as_if_pixhei;
            }
          else if (! (ob . isNull ("ideal-pixwid")  // a bit o' back-compat
                      ||  ob . isNull ("ideal-pixhei")))
            { ma.as_if_pixwid = ob . getLong ("ideal-pixwid");
              ma.as_if_pixhei = ob . getLong ("ideal-pixhei");
              ma.cur_as_if_pwid = ma.as_if_pixwid;
              ma.cur_as_if_phei = ma.as_if_pixhei;
            }

          if (! (ob . isNull ("pixwid")
                 ||  ob . isNull ("pixhei")))
            { ma.requested_pixwid = ob . getLong ("pixwid");
              ma.requested_pixhei = ob . getLong ("pixhei");
            }
          else if (! (ob . isNull ("requested-pixwid")
                 ||  ob . isNull ("requested-pixhei")))
            { ma.requested_pixwid = ob . getLong ("requested-pixwid");
              ma.requested_pixhei = ob . getLong ("requested-pixhei");
            }

          if (! ob . isNull ("window-title"))
            { PZMaesBundle.init_wintitle_by_maesname
                . put (ma . Name (), ob . getString ("window-title"));
            }

          maeses . add (ma);
println(q + "th maes is thus: " + ma);
        }
    }


  public void AssertWellAndTrulyReady ()
    { well_and_truly_ready = true; }

  public List <PlatonicMaes> AllMaeses ()
    { return maeses; }

  public PlatonicMaes MaesByName (String nm)
    { if (nm != null)
        for (PlatonicMaes ma  :  maeses)
          if (ma . Name () . equals (nm))
            return ma;
      return null;
    }


  public CursorHerd ItsCursorHerd ()
    { return cherd; }


  public SpatialAqueduct SpatialEventAqueduct ()
    { return spaque; }

  public YowlAqueduct YowlEventAqueduct ()
    { return yowque; }


  public boolean EnrollSpatialPhage (ZESpatialPhagy phage)
    { if (spaque != null)
        if (spaque . AppendPhage (phage)  ==  0)
          return true;
      return false;
    }

  public boolean EnrollYowlPhage (ZEYowlPhagy phage)
    { if (yowque != null)
        if (yowque . AppendPhage (phage)  ==  0)
          return true;
      return false;
    }


  public void PZDraw (PGraphicsOpenGL g, PZMaesBundle mbun,
                      float sp_width, float sp_height)
    { }


  // the following is (or'd durn well better be) called from within setup()
  public void PZVivify ()
    { //
      uniho = new UninvitedHost ();
      registerMethod ("pre", uniho);

      osc_slurper = new OscP5 (uniho, 54345);

      int cnt = maeses . size ()
        +  (use_dummy_window_for_pzapplet ? 1 : 0);

      for (int q = 0  ;  q < cnt  ;  ++q)
        { int maes_ind = q  -  (use_dummy_window_for_pzapplet ? 1 : 0);
          PZMaesBundle mb;
          if (q == 0)  // special handling, natŭrlich, for our alpha MBundle
            { //mb = this;
              //der_leiter = this;
              // we need this to be in place earlier in life's trajectory,
              // so moving it to our constructor (below)...
            }
          else
            { int dspl = q + (use_dummy_window_for_pzapplet ? 0 : 1);
              dspl *= (display_id > 0)  ?  1  :  -1;
System.out.println("********** **** *** ** *  DISPLAY_ID --> " + dspl);
              mb = new PZMaesBundle (this, dspl);
              mb . InternalizeMaes (maeses . get (maes_ind));
            }
        }

      surface . setResizable (! use_dummy_window_for_pzapplet
                              &&  permit_window_resize);
      String wttl = init_wintitle_by_maesname . get (its_maes . Name ());
      if (wttl != null)
        surface . setTitle (wttl);
      // the lines foregoing also appear in children windows' setup();
      // but if we don't do it here it won't be enacted for this
      // most primary of all windows.

      cherd = new CursorHerd ();
      spaque . AppendPhage (cherd);
    }


  public PZApplet ()
    { super (null, 1);

      if (sole_instance != null)
        throw new RuntimeException
                    ("Ye dassn't make more than one instance o' PZApplet...");
      display_id = 0;
      sole_instance = this;
      der_leiter = this;

      global_looper = new Loopervisor ();

      global_looper. AppendAqueduct (spaque = new SpatialAqueduct ());
      global_looper. AppendAqueduct (yowque = new YowlAqueduct ());

      // just in case, there'll be something instead of nothing:
      SetRawToRoomMatrices (Matrix44.idmat, Matrix44.idmat);

      HooverCoordTransforms ();
      HooverMaeses ();

      InternalizeMaes (use_dummy_window_for_pzapplet
                       ?  dummy_maes
                       :  maeses . get (0));
    }
}
