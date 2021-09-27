
package p5zeugma;


import zeugma.GrapplerPile;
import zeugma.SpaceThing;
import processing.core.PMatrix3D;
import processing.opengl.PGraphicsOpenGL;


public class P5ZSpaceThing  extends SpaceThing  implements P5ZLimnable
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
