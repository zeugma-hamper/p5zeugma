
import java.util.Collection;
import java.util.Iterator;

import p5zeugma.P5ZApplet;
import p5zeugma.P5ZSpaceThing;
import p5zeugma.P5ZApplet.Cursoresque;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PMatrix3D;
import processing.opengl.PGraphicsOpenGL;

import zeugma.Matrix44;
import zeugma.TrGrappler;
import zeugma.RoGrappler;
import zeugma.PlatonicMaes;
import zeugma.SinuVect;
import zeugma.Vect;



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


public class Flamv  extends P5ZApplet
{ public SinuVect diago;
  public ShimmyCrate topshim;
  public P5ZSpaceThing gaylord;
  public P5ZSpaceThing wallifier;
  public ImageSplatter stein, forster;
  public boolean well_and_truly_ready = false;

  public void setup ()
    { P5ZVivify ();

      PlatonicMaes ma = maeses . get (0);

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
//    IronLung pulmo = IronLung.GlobalByName ("omni-lung");
//    Eructathan e1 = new Eructathan ("Blarvles");
//    pulmo . AppendBreathee (e1);
      well_and_truly_ready = true;
    }

  public void settings()
    { FullscreenOnDisplay (1);
      PleaseDoNotFullscreen ();
      super . settings ();
    }

  public void ActuallyDraw (PGraphicsOpenGL ogl)
    { ogl . background (40);
      if (! well_and_truly_ready)
        return;
      
      gaylord . RecursivelyDraw (ogl);
      
      int nx = 80;
      int ny = 45;
      double w = vital_maes . Width ();
      double h = vital_maes . Height ();
      Vect o = vital_maes . Over ();
      Vect u = vital_maes . Up ();
      double gap_frac = 0.2;
      double diamx = w / ((1.0 + gap_frac) * nx);
      double diamy = h / ((1.0 + gap_frac) * ny);
      Vect jogx = o . Mul (diamx * (1.0 + gap_frac));
      Vect jogy = u . Mul (diamy * (1.0 + gap_frac));
      Vect crgrtrn = jogx . Mul ((double)nx);
      Vect p = Vect.zerov . Sub (jogx . Mul (0.5 * (nx - 1)))
                          . Sub (jogy . Mul (0.5 * (ny - 1)));
      wallifier . BefoDraw (ogl);
      ogl . pushStyle ();
      ogl . noFill ();
      ogl . stroke (255, 30);
      for (int ww = ny  ;  ww > 0  ;  --ww)
        { for (int qq = nx  ;  qq > 0  ;  --qq)
            { ogl . ellipse ((float)p.x, (float)p.y, (float)diamx, (float)diamy);
              p . AddAcc (jogx);
            }
          p . SubAcc (crgrtrn);
          p . AddAcc (jogy);
        }
      ogl . popStyle ();
      wallifier . AftaDraw (ogl);
      
      stein . RecursivelyDraw (ogl);
      forster . RecursivelyDraw (ogl);
      
      Iterator <Cursoresque> cuit = cherd.cursor_by_wand . values () . iterator ();
      while (cuit . hasNext ())
        { Cursoresque cur = cuit . next ();
          cur . RecursivelyDraw (ogl);
        }
    }

  public static void main (String av[])
    { PApplet.main ("Flamv"); }
}
