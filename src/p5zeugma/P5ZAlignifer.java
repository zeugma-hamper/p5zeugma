
package p5zeugma;


import zeugma.Alignifer;
import zeugma.GrapplerPile;

import p5zeugma.P5Z;

import processing.core.PApplet;
import processing.core.PMatrix3D;
import processing.opengl.PGraphicsOpenGL;


public class P5ZAlignifer  extends Alignifer  implements P5ZLimnable
{
  public void BefoDraw (PGraphicsOpenGL g)
    { g . pushMatrix ();

      GrapplerPile gp = UnsecuredGrapplerPile ();
      if (gp != null)
        { PMatrix3D mah = P5Z.ToP (gp . PntMat ());
          g . applyMatrix (mah);
        }
    }

  public void AftaDraw (PGraphicsOpenGL g)
    { g . popMatrix (); }
}
