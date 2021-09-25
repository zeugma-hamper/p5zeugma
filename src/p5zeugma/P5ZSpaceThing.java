
package p5zeugma;


import zeugma.SpaceThing;

import processing.opengl.PGraphicsOpenGL;


public class P5ZSpaceThing  extends SpaceThing  implements P5ZLimnable
{
  public void BefoDraw (PGraphicsOpenGL g)
    { g . pushMatrix ();
    }

  public void AftaDraw (PGraphicsOpenGL g)
    { g . popMatrix (); }

}
