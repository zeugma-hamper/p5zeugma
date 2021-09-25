
package p5zeugma;


import zeugma.Alignifer;

import processing.opengl.PGraphicsOpenGL;


public class P5ZAlignifer  extends Alignifer  implements P5ZLimnable
{
  public void BefoDraw (PGraphicsOpenGL g)
    { g . pushMatrix ();
    }

  public void AftaDraw (PGraphicsOpenGL g)
    { g . popMatrix (); }

}
