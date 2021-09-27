
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


public class Blorpus  extends P5ZApplet
{ public SinuVect diago;
  public ShimmyCrate topshim;
  public P5ZSpaceThing gaylord;

  public void setup ()
    { P5ZVivify ();

      PlatonicMaes ma = maeses . get (0);

      diago = new SinuVect (Vect.onesv . Sub (new Vect (0.0, 2.0, 0.0)) . Mul (133.31), 0.3113);
      topshim = new ShimmyCrate ("Mesopotamian indications of undue eversion",
                                 200.0, ma.loc.val,
                                 Vect.onesv . Sub (new Vect (0.0, 2.0, 0.0)) . Mul (133.31),
                                 0.3113);
//      topshim . AssuredGrapplerPile ()
//        . PrependGrappler (new RoGrappler (Vect.zaxis, -30.0 * Math.PI / 180.0));

      ShimmyCrate shic = new ShimmyCrate ("who but Blavatsky?", 100.0, Vect.yaxis . Mul (-850.0),
                                          Vect.yaxis . Mul (50.0), 0.7);
      shic . AssuredGrapplerPile ()
        . PrependGrappler (new RoGrappler (Vect.zaxis, -30.0 * Math.PI / 180.0));
      topshim . AppendChild (shic);

      ShimmyCrate ycra = new ShimmyCrate ("and then the loudest glance...", 150.0, Vect.yaxis . Mul (-850.0),
          Vect.yaxis . Mul (50.0), 0.57);
      shic . AppendChild (ycra);
    
      gaylord = new P5ZSpaceThing ();
      gaylord . AppendChild (topshim);
      gaylord . AppendGrappler (new TrGrappler (Vect.yaxis . Mul (-ma.hei.val * 0.15)));
//    IronLung pulmo = IronLung.GlobalByName ("omni-lung");
//    Eructathan e1 = new Eructathan ("Blarvles");
//    pulmo . AppendBreathee (e1);
    }

  public void settings()
    { size (1920, 1080, P3D);
      //fullScreen (P3D, 3);
    }

  public void draw ()
    { background (40);

      PlatonicMaes ma = maeses . get (0);
      if (ma != null)
        { Vect c = ma.loc.val;
          Vect u = ma.upp.val;
          Vect n = ma.ovr.val . Cross (u);
          double w = ma.wid.val;
          double h = ma.hei.val;
          Vect e = c . Add (n . Mul (0.8 * w));
          
          camera ((float)e.x, (float)e.y, (float)e.z,
                  (float)c.x, (float)c.y, (float)c.z,
                  (float)u.x, (float)u.y, (float)u.z);
          
          float asp = 16.0f / 9.0f;
          float fvy = 2.0f * atan2 ((float)(0.5 * h), (float)(0.8 * w));
          perspective (fvy, asp, (float)(0.1 * w), (float)(5.0 * w));

          PGraphics ics = getGraphics ();
          PGraphicsOpenGL ogl = (PGraphicsOpenGL)ics;
          ogl . applyProjection (1.0f,  0.0f,  0.0f,  0.0f,
                                 0.0f, -1.0f,  0.0f,  0.0f,
                                 0.0f,  0.0f,  1.0f,  0.0f,
                                 0.0f,  0.0f,  0.0f,  1.0f);

          gaylord . RecursivelyDraw (ogl);
          
          Iterator <Cursoresque> cuit = cherd.wand_to_wallpos . values () . iterator ();
          while (cuit . hasNext ())
            { Cursoresque cur = cuit . next ();
              cur . RecursivelyDraw (ogl);
            }
        }
    }

  public static void main (String av[])
    { PApplet.main ("Blorpus"); }
}
