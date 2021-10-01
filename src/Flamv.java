
import java.util.Collection;
import java.util.Iterator;

import p5zeugma.P5ZApplet;
import p5zeugma.P5ZSpaceThing;
import p5zeugma.P5ZApplet.Cursoresque;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PMatrix3D;
import processing.opengl.PGraphicsOpenGL;

import zeugma.Matrix44;
import zeugma.TrGrappler;
import zeugma.RoGrappler;
import zeugma.PlatonicMaes;
import zeugma.SinuVect;
import zeugma.Vect;





public class Flamv  extends P5ZApplet
{ public SinuVect diago;
  public ShimmyCrate topshim;
  public P5ZSpaceThing gaylord;
  public P5ZSpaceThing wallifier;

  public void setup ()
    { P5ZVivify ();

      PlatonicMaes ma = maeses . get (0);

      diago = new SinuVect (Vect.onesv . Sub (new Vect (0.0, 2.0, 0.0)) . Mul (133.31), 0.3113);
      topshim = new ShimmyCrate ("  just one more reason that truculence is fine",
                                 200.0, ma.loc.val,
                                 Vect.onesv . Sub (new Vect (0.0, 2.0, 0.0)) . Mul (133.31),
                                 0.3113);

      ShimmyCrate shic = new ShimmyCrate ("  who but Blavatsky?", 100.0, Vect.yaxis . Mul (-1250.0),
                                          Vect.yaxis . Mul (80.0), 0.7);
      shic . AssuredGrapplerPile ()
        . PrependGrappler (new RoGrappler (Vect.zaxis, -30.0 * Math.PI / 180.0));
      topshim . AppendChild (shic);

      ShimmyCrate ycra = new ShimmyCrate ("  and then the loudest glance...", 150.0, Vect.yaxis . Mul (-850.0),
          Vect.xaxis . Mul (90.0), 0.57);
      shic . AppendChild (ycra);
    
      gaylord = new P5ZSpaceThing ();
      gaylord . AppendChild (topshim);
      gaylord . AppendGrappler (new TrGrappler (Vect.yaxis . Mul (ma.hei.val * 0.15)));
      
      wallifier = new P5ZSpaceThing ();
      wallifier . AppendGrappler (new TrGrappler (ma.loc.val));
//    IronLung pulmo = IronLung.GlobalByName ("omni-lung");
//    Eructathan e1 = new Eructathan ("Blarvles");
//    pulmo . AppendBreathee (e1);
    }

  public void settings()
    { size (1920, 1080, P3D);
      //fullScreen (P3D, 3);
    }

  public void ActuallyDraw (PGraphicsOpenGL ogl)
    { ogl . background (40);

      gaylord . RecursivelyDraw (ogl);
      
      int nx = 80;
      int ny = 45;
      double w = beast_maes . Width ();
      double h = beast_maes . Height ();
      Vect o = beast_maes . Over ();
      Vect u = beast_maes . Up ();
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
      
      Iterator <Cursoresque> cuit = cherd.cursor_by_wand . values () . iterator ();
      while (cuit . hasNext ())
        { Cursoresque cur = cuit . next ();
          cur . RecursivelyDraw (ogl);
        }
    }

  public static void main (String av[])
    { PApplet.main ("Flamv"); }
}
