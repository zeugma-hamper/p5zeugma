
package p5zeugma;

import processing.opengl.PGraphicsOpenGL;

public interface P5ZLimnable
{
  default void BefoDraw (PGraphicsOpenGL g)
    { }

  default void AftaDraw (PGraphicsOpenGL g)
    { }
  
  default void DrawSelf (PGraphicsOpenGL g)
    { }

  default void Draw (PGraphicsOpenGL g)
    { BefoDraw (g);
      DrawSelf (g);
      AftaDraw (g);
    }

  default void RecursivelyDraw (PGraphicsOpenGL g)
    { }
}
