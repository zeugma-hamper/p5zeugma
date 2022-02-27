
//
// (c) treadle & loam, provisioners llc
//


import p5zeugma.PZApplet;
import p5zeugma.PZMaesBundle;
import p5zeugma.PZSpaceThing;
import p5zeugma.PZ;

import processing.core.PApplet;
import processing.core.PImage;
import processing.opengl.PGraphicsOpenGL;

import zeugma.*;

import java.util.HashMap;


class ShimmyCrate  extends PZSpaceThing
{ String mess;
  double sc;

  ShimmyCrate (String wrds, double sca, Vect offset, Vect shimax, double freq)
    { mess = wrds;
      sc = sca;
      SinuVect iago = new SinuVect (shimax, freq);
      TrGrappler trg = new TrGrappler (iago);
      AppendGrappler (trg);
      trg = new TrGrappler (offset);
      AppendGrappler (trg);
    }
  public void DrawSelf (PGraphicsOpenGL g)
    { g . box ((float)sc);
      g . textSize (100.0f);
      g . pushMatrix ();
      g . applyMatrix (1.0f,  0.0f,  0.0f,  0.0f,
                       0.0f, -1.0f,  0.0f,  0.0f,
                       0.0f,  0.0f,  1.0f,  0.0f,
                       0.0f,  0.0f,  0.0f,  1.0f);
      g . text (mess, 0.0f, 0.0f);
      g . popMatrix ();
    }
}


class Soikles  extends PZSpaceThing
{ int nx = 80;
  int ny = 45;
  double w, h;
  Vect o, u;
  double gap_frac = 0.2;
  double diamx, diamy;
  Vect jogx, jogy;
  Vect crgrtrn;

  public Soikles (PlatonicMaes ma)
    { w = ma . Width ();
      h = ma . Height ();
      o = ma . Over ();
      u = ma . Up ();
      diamx = w / ((1.0 + gap_frac) * nx);
      diamy = h / ((1.0 + gap_frac) * ny);
      jogx = o . Mul (diamx * (1.0 + gap_frac));
      jogy = u . Mul (diamy * (1.0 + gap_frac));
      crgrtrn = jogx . Mul ((double)nx);
    }

  public void DrawSelf (PGraphicsOpenGL g)
    { Vect p = Vect.zerov . Sub (jogx . Mul (0.5 * (nx - 1)))
                          . Sub (jogy . Mul (0.5 * (ny - 1)));
      g . pushStyle ();
      g . noFill ();
      g . stroke (255, 30);
      for (int ww = ny  ;  ww > 0  ;  --ww)
        { for (int qq = nx  ;  qq > 0  ;  --qq)
            { g . ellipse ((float)p.x, (float)p.y, (float)diamx, (float)diamy);
              p . AddAcc (jogx);
            }
          p . SubAcc (crgrtrn);
          p . AddAcc (jogy);
        }
      g . popStyle ();
    }
}


public class Flamv  extends PZApplet
                    implements ZESpatialPhagy
{ public Soikles soiks;
  public SinuVect diago;
  public ShimmyCrate topshim;
  public PZSpaceThing gaylord, omnibus;
  public PZSpaceThing wallifier;
  public ImageSplatter stein, forster;
  public HashMap <String, Vect> whackpt_by_maesname;

  public void setup ()
    { SetShouldPermitWindowResize (true);  // tragically, must go before PZViv...

      PZVivify ();

      PlatonicMaes ma = MaesByName ("front");

      soiks = new Soikles (ma);

      diago = new SinuVect (Vect.onesv . Sub (new Vect (0.0, 2.0, 0.0))
                            . Mul (133.31), 0.3113);
      topshim
        = new ShimmyCrate ("      Mesopotamian hints of imminent eversion",
                           200.0, ma.loc.val,
                           Vect.onesv . Sub (new Vect (0.0, 2.0, 0.0))
                             . Mul (133.31),
                           0.3113);

      ShimmyCrate shic
        = new ShimmyCrate ("    who but Blavatsky?", 100.0,
                           Vect.yaxis . Mul (-1250.0),
                           Vect.yaxis . Mul (80.0), 0.7);
      shic . AssuredGrapplerPile ()
        . PrependGrappler (new RoGrappler (Vect.zaxis,
                                           -30.0 * Math.PI / 180.0));
      topshim . AppendChild (shic);

      ShimmyCrate ycra = new ShimmyCrate ("     and then the loudest glance...",
                                          150.0, Vect.yaxis . Mul (-850.0),
                                          Vect.xaxis . Mul (90.0), 0.57);
      shic . AppendChild (ycra);

      gaylord = new PZSpaceThing();
      gaylord . AppendChild (topshim);
      gaylord . AppendGrappler (new TrGrappler (Vect.yaxis . Mul (ma.hei.val
                                                                  * 0.15)));

      wallifier = new PZSpaceThing();
      wallifier . AppendGrappler (new TrGrappler (ma.loc.val));
      wallifier . AppendChild (soiks);

      PImage st = loadImage ("snacks/stein-picabia-smaller.png");
      PImage fo = loadImage ("snacks/forster-fry-smaller.png");
      stein = new ImageSplatter (st);
      forster = new ImageSplatter (fo);
      stein . ScaleZoft () . Set (Vect.onesv . Mul (1500.0));
      stein . LocZoft () . Set (ma.loc.val . Add (Vect.xaxis . Mul (1000.0)));
      forster . ScaleZoft () . Set (Vect.onesv . Mul (1500.0));
      forster . LocZoft () . Set (ma.loc.val . Sub (Vect.xaxis . Mul (1000.0)));

      this.spaque . AppendPhage (stein);
      this.spaque . AppendPhage (forster);
      this.spaque . AppendPhage (this);

      this.yowque . AppendPhage (stein);
      this.yowque . AppendPhage (forster);

      omnibus = new PZSpaceThing();
      omnibus . AppendChild (stein);
      omnibus . AppendChild (forster);

      for (PlatonicMaes maes  :  maeses)
        { maes . AppendLayer (gaylord);
          if (maes == ma)
            maes . AppendLayer (wallifier);
          maes . AppendLayer (omnibus);
          maes . AppendLayer (cherd);
        }

      ma = MaesByName ("table");
      SinuColor sc = new SinuColor (new ZeColor (0.0f, 0.5f, 0.5f, 0.0f),
                                    0.666, new ZeColor (1.0f, 0.5f, 0.5f));
      if (ma != null)
        ma . InstallAdjColorZoft (sc);

      PZMaesBundle mbun = MaesBundleByMaesName ("left");
      if (mbun != null)
        { PZMaesBundle . AdaptBackplateForP5Coords (mbun);
          mbun . AppendMaesGeomChangeVoyeur (Flamv::MaesPalpingYelper);
          mbun . AppendMaesGeomChangeVoyeur
            (PZMaesBundle::ReviseBackplateAdaptationForP5Coords);
        }

//    IronLung pulmo = IronLung.GlobalByName ("omni-lung");
//    Eructathan e1 = new Eructathan ("Blarvles");
//    pulmo . AppendBreathee (e1);

      whackpt_by_maesname = new HashMap <> ();

      well_and_truly_ready = true;
    }

  static void MaesPalpingYelper (PlatonicMaes ma, PZMaesBundle mbun)
    { if (ma . Name () . equals ("left"))
        System.out.printf ("side wall maes wrenched around to [%d %d]...\n",
                           ma.cur_as_if_pwid, ma.cur_as_if_phei);
      else
        System.out.printf ("maes <%s> warped thus: [%f %f]...\n",
                           ma . Name (), ma.cur_wid, ma.cur_hei);
    }


  public long ZESpatialHarden (ZESpatialHardenEvent e)
    { PlatonicMaes.MaesAndHit mah
        = PZMaesBundle.ClosestAmongLiving (e.loc, e.aim);
      if (mah == null)
        return 0;

      PZMaesBundle mbun = PZMaesBundle.MaesBundleByMaes (mah.maes);
      if (mbun == null)
        return 0;

      Vect v = mah.hit;
      System.out.printf ("whackage at screen [%f %f %f]...\n",
                         mbun.screenX ((float)v.x, (float)v.y, (float)v.z),
                         mbun.screenY ((float)v.x, (float)v.y, (float)v.z),
                         mbun.screenZ ((float)v.x, (float)v.y, (float)v.z));
      whackpt_by_maesname . put (mah.maes . Name (), v);

      Vect vv = mbun . TransformWorldToScreen (v);
      System.out.printf ("(... while the new hotness says [%f %f %f])\n",
                         vv.x, vv.y, vv.z);

      return 0;
    }

  public long ZESpatialCaress (ZESpatialCaressEvent e)
    { PlatonicMaes.MaesAndHit mah
        = PZMaesBundle.ClosestAmongLiving (e.loc, e.aim);
      if (mah == null)
        return 0;

      Vect v = mah.hit;
      Vect c = e . CaressValue ();
      System.out.printf ("at hitpoint [%f %f %f], this caress {%f %f %f}.\n",
                         v.x, v.y, v.z, c.x, c.y, c.z);
      return 0;
    }


  public void PZDraw (PGraphicsOpenGL g, PZMaesBundle mbun,
                      float sp_width, float sp_height)
    { PlatonicMaes emm;
      if (mbun == null
          ||  (emm = mbun . ItsMaes ()) == null)
        return;

      String nm = emm . Name ();
      Vect v = whackpt_by_maesname . get (nm);
      if (v != null)
        { System.out.printf ("BUT: screen whackage (maes %s) at [%f %f %f]...\n",
                             nm,
                             mbun.screenX ((float)v.x, (float)v.y, (float)v.z),
                             mbun.screenY ((float)v.x, (float)v.y, (float)v.z),
                             mbun.screenZ ((float)v.x, (float)v.y, (float)v.z));
          Vect vv = mbun . TransformWorldToScreen (v);
          System.out.printf ("<< while novo dork has it thus: [%f %f %f] >>\n",
                             vv.x, vv.y, vv.z);
          whackpt_by_maesname . remove (nm);
        }

      if (! emm . Name () . equals ("left"))
        return;

      g . strokeWeight (0.5f);
      g . stroke (255, 20);

      // Vect cc[] = { new Vect (sp_width, sp_height, 0.0) . Mul (0.5),
      //               new Vect (sp_width, -sp_height, 0.0) . Mul (0.5),
      //               new Vect (-sp_width, -sp_height, 0.0) . Mul (0.5),
      //               new Vect (-sp_width, sp_height, 0.0) . Mul (0.5) };
      //
      // above: the version for reg'lar coords; below, processingized coords.
      //
      double rgt = emm.as_if_pixwid - 1.0;
      double bot = emm.as_if_pixhei - 1.0;
      Vect cc[] = { new Vect (0.0, 0.0, 0.0),
                    new Vect (rgt, 0.0, 0.0),
                    new Vect (0.0, bot, 0.0),
                    new Vect (rgt, bot, 0.0) };

      Vect cnt = new Vect (0.5 * rgt, 0.5 * bot, 0.0);

      for (Vect c  :  cc)
        { Vect b = cnt . Add (c . Sub (cnt) . Mul (0.15));
          PZ.line (g, b, c);
        }
    }

  public void settings()
    { FullscreenOnDisplay (1);
      PleaseDoNotFullscreen ();
      super . settings ();
    }

  public static void main (String av[])
    { PApplet.main ("Flamv"); }
}
