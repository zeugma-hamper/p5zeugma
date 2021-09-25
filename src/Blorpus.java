
import java.util.Collection;
import java.util.Iterator;

import p5zeugma.P5ZApplet;
import p5zeugma.P5ZApplet.Cursoresque;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PMatrix3D;
import processing.opengl.PGraphicsOpenGL;
import zeugma.Matrix44;
import zeugma.PlatonicMaes;
import zeugma.SinuVect;
import zeugma.Vect;

public class Blorpus  extends P5ZApplet
{ public SinuVect diago;

  public void setup ()
    { diago = new SinuVect (Vect.onesv . Sub (new Vect (0.0, 2.0, 0.0)) . Mul (133.31), 0.3113);
    
      P5ZVivify ();
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

          pushMatrix ();
          Matrix44 tranny = new Matrix44 () . LoadTranslation (c . Sub (Vect.yaxis . Mul (500.0))
                                                                . Add (diago.val));
          Matrix44 rotty = new Matrix44 () . LoadRotation (Vect.zaxis, 30.0 * Math.PI / 180.0);
          Matrix44 muh = rotty . Mul (tranny);
          PMatrix3D matty = Z2P (muh);
          // translate ((float)c.x, (float)c.y - 500.0f, (float)c.z);
          applyMatrix (matty);
          box (300.0f);
          textSize (100.0f);
          applyMatrix (1.0f,  0.0f,  0.0f,  0.0f,
                       0.0f, -1.0f,  0.0f,  0.0f,
                       0.0f,  0.0f,  1.0f,  0.0f,
                       0.0f,  0.0f,  0.0f,  1.0f);
          text ("peristaltic indications of incipient eversion", 0.0f, 0.0f);
          popMatrix ();

          Collection <Cursoresque> crsrs = cherd.wand_to_wallpos . values ();
          Iterator <Cursoresque> cuit = crsrs . iterator ();
          while (cuit . hasNext ())
            { Cursoresque cur = cuit . next ();
              Vect p = cur.wall_pos;
              pushMatrix ();
              translate ((float)p.x, (float)p.y, (float)p.z);
              //box (100.0);
              cur . Draw (ogl);
              popMatrix ();
            }
        }
      //println ("Momma keeps on tickin': " + momma_tee . DeltaTime ());
//      ++cnt;
    }

  public static void main (String av[])
    { PApplet.main ("Blorpus"); }
}
