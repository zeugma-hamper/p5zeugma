
//
// (c) treadle & loam, provisioners llc
//


import java.util.Iterator;

import p5zeugma.P5ZApplet;
import p5zeugma.P5ZMaesBundle;
import p5zeugma.P5ZSpaceThing;

import processing.core.PApplet;
import processing.core.PImage;
import processing.opengl.PGraphicsOpenGL;

import zeugma.*;


class ShimmyCrate  extends P5ZSpaceThing
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


class Soikles  extends P5ZSpaceThing
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


public class Flamv  extends P5ZApplet
{ public Soikles soiks;
  public SinuVect diago;
  public ShimmyCrate topshim;
  public P5ZSpaceThing gaylord, omnibus;
  public P5ZSpaceThing wallifier;
  public ImageSplatter stein, forster;

  public void setup ()
    { P5ZVivify ();

      PlatonicMaes ma = FindMaesByName ("front");

      soiks = new Soikles (ma);

      diago = new SinuVect (Vect.onesv . Sub (new Vect (0.0, 2.0, 0.0)) . Mul (133.31), 0.3113);
      topshim = new ShimmyCrate ("      Mesopotamian hints of imminent eversion",
                                 200.0, ma.loc.val,
                                 Vect.onesv . Sub (new Vect (0.0, 2.0, 0.0)) . Mul (133.31),
                                 0.3113);

      ShimmyCrate shic = new ShimmyCrate ("    who but Blavatsky?", 100.0, Vect.yaxis . Mul (-1250.0),
                                          Vect.yaxis . Mul (80.0), 0.7);
      shic . AssuredGrapplerPile ()
        . PrependGrappler (new RoGrappler (Vect.zaxis, -30.0 * Math.PI / 180.0));
      topshim . AppendChild (shic);

      ShimmyCrate ycra = new ShimmyCrate ("     and then the loudest glance...",
                                          150.0, Vect.yaxis . Mul (-850.0),
                                          Vect.xaxis . Mul (90.0), 0.57);
      shic . AppendChild (ycra);

      gaylord = new P5ZSpaceThing ();
      gaylord . AppendChild (topshim);
      gaylord . AppendGrappler (new TrGrappler (Vect.yaxis . Mul (ma.hei.val * 0.15)));

      wallifier = new P5ZSpaceThing ();
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

      this.yowque . AppendPhage (stein);
      this.yowque . AppendPhage (forster);

      omnibus = new P5ZSpaceThing ();
      omnibus . AppendChild (stein);
      omnibus . AppendChild (forster);

      for (PlatonicMaes maes  :  maeses)
        { maes . AppendLayer (gaylord);
          if (maes == ma)
            maes . AppendLayer (wallifier);
          maes . AppendLayer (omnibus);
          maes . AppendLayer (cherd);
        }

      ma = FindMaesByName ("table");
      SinuColor sc = new SinuColor (new ZeColor (0.0f, 0.5f, 0.5f, 0.0f),
                                    0.666, new ZeColor (1.0f, 0.5f, 0.5f));
      if (ma != null)
        ma . InstallAdjColorZoft (sc);

//    IronLung pulmo = IronLung.GlobalByName ("omni-lung");
//    Eructathan e1 = new Eructathan ("Blarvles");
//    pulmo . AppendBreathee (e1);
      well_and_truly_ready = true;
    }


  public void PZDraw (PGraphicsOpenGL g, P5ZMaesBundle mbun,
                      float sp_width, float sp_height)
    { PlatonicMaes emm;
      if (mbun == null
          ||  (emm = mbun . ItsMaes ()) == null
          ||  ! emm . Name () . equals ("left"))
        return;

      g . strokeWeight (0.5f);
      g . stroke (255, 20);

      Vect cc[] = { new Vect (sp_width, sp_height, 0.0) . Mul (0.5),
                    new Vect (sp_width, -sp_height, 0.0) . Mul (0.5),
                    new Vect (-sp_width, -sp_height, 0.0) . Mul (0.5),
                    new Vect (-sp_width, sp_height, 0.0) . Mul (0.5) };

      for (Vect c  :  cc)
        { Vect b = c . Mul (0.15);
          g . line ((float)b.x, (float)b.y, (float)b.z,
                    (float)c.x, (float)c.y, (float)c.z);
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
