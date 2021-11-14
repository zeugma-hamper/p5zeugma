
package p5zeugma;


import zeugma.*;

import oscP5.*;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Random;

import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.opengl.PGraphicsOpenGL;


public class PZApplet  extends PZMaesBundle
{ public static PZApplet sole_instance = null;
  public static boolean well_and_truly_ready = false;

  public Loopervisor global_looper;

  OscP5 osc_slurper;

  UninvitedHost uniho;

  protected SpatialAqueduct spaque;
  protected YowlAqueduct yowque;

  protected Matrix44 raw_to_room_direc_mat;
  protected Matrix44 raw_to_room_point_mat;

  protected ArrayList <PlatonicMaes> maeses = new ArrayList <> ();
  protected ArrayList <PZMaesBundle> all_mbundles = new ArrayList <> ();

  protected CursorHerd cherd;


  public class UninvitedHost
    { public void pre ()
        { if (global_looper != null)
            global_looper. OnceMoreUntoTheBreath ();
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

          raw_to_room_point_mat . TransformVectInPlace (pos);
          raw_to_room_direc_mat . TransformVectInPlace (aim);
          raw_to_room_direc_mat . TransformVectInPlace (ovr);

          spaque . InterpretRawWandish (wname, butts, pos, aim, ovr);
        }

    }

  static Random randolph = new Random ();

  public class Cursoresque  extends Alignifer
                            implements PZLimnable, ZESpatialPhagy
  { //public Vect wall_pos;
    public ArrayList <SinuVect> vs_lrg, vs_sml;
    public ZoftThing <ZeColor> iro;
    public PlatonicMaes cur_maes;

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
              AlignToMaes (cur_maes = mah.maes);
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


  public class CursorHerd  extends PZLimnyThing implements ZESpatialPhagy
  { public HashMap <String, Cursoresque> cursor_by_wand = new HashMap <> ();

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
  }


  public static Matrix44 ConjureMatrix44 (JSONArray ja)
    { if (ja == null  ||  ja . size ()  !=  16)
        return null;
      return new Matrix44
          (ja . getFloat (0),  ja . getFloat (1),
                                      ja . getFloat (2),  ja . getFloat (3),
           ja . getFloat (4),  ja . getFloat (5),
                                      ja . getFloat (6),  ja . getFloat (7),
           ja . getFloat (8),  ja . getFloat (9),
                                      ja . getFloat (10), ja . getFloat (11),
           ja . getFloat (12), ja . getFloat (13),
                                      ja . getFloat (14), ja . getFloat (15));
  }

  public static Vect ConjureVect (JSONArray ja)
    { if (ja == null  ||  ja . size ()  !=  3)
        return null;
      return new Vect (ja . getFloat (0), ja . getFloat (1), ja . getFloat (2));
    }


  public void HooverCoordTransforms ()
    { JSONObject jace = loadJSONObject ("config/coord-xform-raw-to-room.json");
      if (jace == null)
        jace = loadJSONObject
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

      raw_to_room_direc_mat = ConjureMatrix44 (dm);
      raw_to_room_point_mat = ConjureMatrix44 (pm);
    }


  public void HooverMaeses ()
    { JSONArray jarr = loadJSONArray ("config/maes-config.json");
      if (jarr == null)
        jarr = loadJSONArray ("/opt/trelopro/config/json/maes-config.json");
      // if still no success, we should, you know, say a little something
      // and then at least have the good grace to set the hapless system
      // up with some default maes or other... no?
      int num = jarr . size ();
      JSONObject ob;
      for (int q = 0  ;  q < num  ;  ++q)
        { if ((ob = jarr . getJSONObject (q))  ==  null)
            continue;
          if (ob . isNull ("name")  ||  ob . isNull ("location")  ||
              ob . isNull ("over")  ||  ob . isNull ("up")  ||
              ob . isNull ("width")  ||  ob . isNull ("height")  ||
              ob . isNull ("ideal-pixwid")  ||  ob . isNull ("ideal-pixhei"))
            continue;

          PlatonicMaes ma = new PlatonicMaes ();
          ma . SetName (ob . getString ("name"));
          ma.loc = new ZoftVect (ConjureVect (ob . getJSONArray ("location")));
          ma.ovr = new ZoftVect (ConjureVect (ob . getJSONArray ("over")));
          ma.upp = new ZoftVect (ConjureVect (ob . getJSONArray ("up")));
          ma.wid = new ZoftFloat (ob . getFloat ("width"));
          ma.hei = new ZoftFloat (ob . getFloat ("height"));
          ma.ideal_pixwid = ob . getInt ("ideal-pixwid");
          ma.ideal_pixhei = ob . getInt ("ideal-pixhei");

          maeses . add (ma);
println(q + "th maes is thus: " + ma);
        }
    }


  public List <PlatonicMaes> AllMaeses ()
    { return maeses; }

  public PlatonicMaes MaesByName (String nm)
    { if (nm != null)
        for (PlatonicMaes ma  :  maeses)
          if (ma . Name () . equals (nm))
            return ma;
      return null;
    }

  public List <PZMaesBundle> AllMaesBundles ()
    { return all_mbundles; }


  public PZMaesBundle MaesBundleByMaes (PlatonicMaes ma)
    { for (PZMaesBundle mb  :  all_mbundles)
        if (mb . ItsMaes ()  ==  ma)
          return mb;
      return null;
    }

  public PZMaesBundle MaesBundleByMaesName (String mname)
    { PlatonicMaes ma;
      for (PZMaesBundle mb  :  all_mbundles)
        if ((ma = mb . ItsMaes ())  ==  ma)
          if (ma . Name () . equals (mname))
            return mb;
      return null;
    }


  public SpatialAqueduct SpatialEventAqueduct ()
    { return spaque; }

  public YowlAqueduct YowlEventAqueduct ()
    { return yowque; }

  public void PZDraw (PGraphicsOpenGL g, PZMaesBundle mbun,
                      float sp_width, float sp_height)
    { }


  public void P5ZVivify()
    { global_looper = new Loopervisor ();

      global_looper. AppendAqueduct (spaque = new SpatialAqueduct ());
      global_looper. AppendAqueduct (yowque = new YowlAqueduct ());

      uniho = new UninvitedHost ();
      registerMethod ("pre", uniho);

      osc_slurper = new OscP5 (uniho, 54345);

      HooverCoordTransforms ();
      HooverMaeses ();

      for (int q = 0  ;  q < maeses . size ()  ;  ++q)
        { int dspl = (display_id > 0)  ?  (q + 1)  :  -(q + 1);
          PZMaesBundle mb;
          if (q == 0)  // special handling, natŭrlich, for the alpha MBundle
            { mb = this;  der_leiter = this; }
          else
            mb = new PZMaesBundle (this, dspl);
          all_mbundles . add (mb);

          mb.its_maes = maeses . get (q);
          mb.its_cammy = PlatonicMaes.CameraFromMaes (mb.its_maes);
          mb.its_backplate . AlignToMaes (mb.its_maes);
          mb.its_backplate . LocGrapplerZoftVect ()
            . BecomeLike (mb.its_maes . LocZoft ());
          mb.its_maes . AppendLayer (mb.its_backplate);
        }

      cherd = new CursorHerd ();
      spaque . AppendPhage (cherd);

      // Cursoresque curry = new Cursoresque (450.0, 6);
      // cherd.cursor_by_wand . put ("stasis-weasel", curry);
      // PlatonicMaes ma = maeses . get (0);
      // if (ma != null)
      //   { curry . LocZoft () . Set (ma.loc.val);
      //     curry . AlignToMaes (ma);
      //   }
    }

  public PZApplet()
    { super (null, 1);
      if (sole_instance != null)
        throw new RuntimeException
                    ("Ye dassn't make more than one instance o' PZApplet...");
      display_id = 0;
      sole_instance = this;
    }
}
