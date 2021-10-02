
package p5zeugma;


import zeugma.*;

import oscP5.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;

import java.util.Random;

import processing.core.*;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.*;
import processing.opengl.PGraphicsOpenGL;


public class P5ZApplet  extends P5ZLivingMaes
{ MotherTime momma_tee;
  long ratchet = 0;

  OscP5 osc_slurper;
  
  UninvitedHost uniho;

  SpatialAqueduct spaque;

  Matrix44 raw_to_room_direc_mat;
  Matrix44 raw_to_room_point_mat;

  protected ArrayList <PlatonicMaes> maeses = new ArrayList <> ();
  
  protected CursorHerd cherd;

  
  public class UninvitedHost
    { public void pre ()
        { spaque . DrainReservoir ();

          ratchet += 8;
          double t = momma_tee . CurTime ();
          
          VelvetLung vl = ProtoZoftThingGuts.MassBreather ();
          if (vl != null)
              vl . Inhale (ratchet, t);

          //IronLung pulmo = IronLung.GlobalByName ("omni-lung");
          for (IronLung pulmo  :  IronLung.GlobalLungs ())
            if (pulmo != null  &&  pulmo != vl)
              pulmo . Inhale (ratchet, t);
        }

//      public void mouseEvent (MouseEvent e)
//        { int tion = e . getAction ();
//          switch (tion)
//            { case MouseEvent.CLICK: case MouseEvent.ENTER:
//              case MouseEvent.EXIT: case MouseEvent.WHEEL:
//                return;
//            }
//          double xnrm = (double)(e . getX ()) / (double)width  -  0.5;  // ouch, to say the least.
//          double ynrm = 0.5  -  (double)(e . getY ()) / (double)height;
//          PlatonicMaes ma = maeses . get (0);  // plenty more oy.
//          if (ma == null)
//              return;
//          Vect hit = ma.loc.val . Add (ma.ovr.val . Mul (ma.wid.val * xnrm))
//                                . Add (ma.upp.val . Mul (ma.hei.val * ynrm));
//          Vect n = ma.ovr.val . Cross (ma.upp.val) . Norm ();
//          n . MulAcc (0.8 * ma.wid.val);
//          Vect eye = hit . Add (n);
//          Vect aim = ma.upp.val . Cross (ma.ovr.val) . Norm ();
//          int b = e . getButton ();
//          long butt = 0;
//          if (b != 0)
//            butt |= ((b == PConstants.LEFT)  ?  (0x01 << 0)
//                     :  ((b == PConstants.CENTER)  ?  (0x01 << 1)
//                         :  ((b == PConstants.RIGHT)  ?  (0x01 << 2)  :  0)));
//          // next in the unending onslaught of oy:
//          // (this is because upstream the 'RELEASE' event has the 'button' var
//          // set to the button that's being released, but zeugma triggers on the
//          // change of a bit in the overall button bitfield.
//          if (tion == MouseEvent.RELEASE)
//            butt = 0;
//          // now, meanwhile: how bad is this really, making
//          // the mouse event masquerade as wand input?
//          spaque . InterpretRawWandish ("mouse-0", butt, eye, aim, ma.ovr.val);
//        }
      
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

  public class Cursoresque  extends P5ZAlignifer  implements ZESpatialPhagy
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
            { double theeta = 2.0 * Math.PI / (double)nv * (double)q + (double)w * Math.PI;
              Vect radial = new Vect ((Vect.xaxis . Mul (Math.cos (theeta))
                                      . Add (Vect.yaxis . Mul (Math.sin (theeta))))
                                        . Mul (0.5 * ((double)w + 1.0)));
              SinuVect arm = new SinuVect (radial . Mul (sz * 0.065),
                                           0.8  +  0.11 * randolph . nextDouble (),
                                           radial . Mul (sz * 0.24 * (1.0 + 3.0 * (double)(q%2))));
              ((w > 0) ? vs_lrg : vs_sml) . add (arm);
            }
      }
    public void DrawSelf (PGraphicsOpenGL g)
      { Vect v;
        if (cur_maes  !=  P5ZLivingMaes.maes_by_gfx_handle . get (g))
          return;
        g . noStroke ();
        ZeColor c = iro.val;
        g . fill ((int)(255 * c.r), (int)(255 * c.g), (int)(255 * c.b), (int)(255 * c.a));
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
      { PlatonicMaes.MaesAndHit mah = PlatonicMaes.ClosestAmong (maeses, e.loc, e.aim);
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


  public class CursorHerd  implements ZESpatialPhagy
  { public HashMap <String, Cursoresque> cursor_by_wand = new HashMap <> ();

    public boolean PassTheBuckUpPhageHierarchy ()
      { return true; }   // so that all the various ZESpatial* events end up in ZESpatial()

    public long ZESpatial (ZESpatialEvent e)
      { String prv = e . Provenance ();
        Cursoresque crs = cursor_by_wand . get (prv);
        if (crs == null)
          cursor_by_wand . put (prv, crs = new Cursoresque (150.0, 6));
        return e . ProfferAsQuaffTo (crs);
      }
  }


  public static Matrix44 ConjureMatrix44 (JSONArray ja)
    { if (ja == null  ||  ja . size ()  !=  16)
        return null;
      return new Matrix44
          (ja . getFloat (0),  ja . getFloat (1),  ja . getFloat (2),  ja . getFloat (3),
           ja . getFloat (4),  ja . getFloat (5),  ja . getFloat (6),  ja . getFloat (7),
           ja . getFloat (8),  ja . getFloat (9),  ja . getFloat (10), ja . getFloat (11),
           ja . getFloat (12), ja . getFloat (13), ja . getFloat (14), ja . getFloat (15));
  }

  public static Vect ConjureVect (JSONArray jarr)
    { if (jarr == null  ||  jarr . size ()  !=  3)
        return null;
      return new Vect (jarr . getFloat (0), jarr . getFloat (1), jarr . getFloat (2));
    }


  public void HooverCoordTransforms ()
    { JSONObject jace = loadJSONObject
        ("/opt/trelopro/config/json/coord-xform-raw-to-room.json");
      println ("JACE is, well, " + jace);
      JSONArray dm = jace . getJSONArray ("direc_mat");
      JSONArray pm = jace . getJSONArray ("point_mat");

      raw_to_room_direc_mat = ConjureMatrix44 (dm);
      raw_to_room_point_mat = ConjureMatrix44 (pm);
    }


  public void HooverMaeses ()
    { JSONArray jarr = loadJSONArray
        ("/opt/trelopro/config/json/maes-config.json");
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

  
  public void P5ZVivify()
    { momma_tee = new MotherTime ();
      momma_tee . ZeroTime ();

      spaque = new SpatialAqueduct ();
      
      uniho = new UninvitedHost ();
      registerMethod ("pre", uniho);
    
      osc_slurper = new OscP5 (uniho, 54345);

      HooverCoordTransforms ();
      HooverMaeses ();
      vital_maes = maeses . get (0);
      vital_cammy = PlatonicMaes.CameraFromMaes (vital_maes);
      vital_fuehrer = this;
      
      int q = 1;
      for (  ;  q < maeses . size ()  ;  ++q)
        { int dspl = (display_if_fullscreen > 0)  ?  (q + 1)  :  -(q + 1);
          P5ZLivingMaes lma = new P5ZLivingMaes (this, dspl);
          lma.vital_maes = maeses . get (q);
          lma.vital_cammy = PlatonicMaes.CameraFromMaes (lma.vital_maes);
        }
 
      cherd = new CursorHerd ();
      spaque . AppendPhage (cherd);

      Cursoresque curry = new Cursoresque (450.0, 6);
      cherd.cursor_by_wand . put ("stasis-weasel", curry);
      PlatonicMaes ma = maeses . get (0);
      if (ma != null)
    	  { curry . LocZoft () . Set (ma.loc.val);
    	    curry . AlignToMaes (ma);
    	  }
    }

  public P5ZApplet ()
    { super (null, 1);
      display_if_fullscreen = 0;
    }
}
