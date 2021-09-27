
package p5zeugma;


import zeugma.Zeubject;
import zeugma.Limnable;
import zeugma.IContainMultitudes;
import processing.opengl.PGraphicsOpenGL;


public interface P5ZLimnable  extends Limnable, IContainMultitudes
{
  default void BefoDraw (PGraphicsOpenGL g)
    { }

  default void AftaDraw (PGraphicsOpenGL g)
    { }
  
  default void DrawSelf (PGraphicsOpenGL g)
    { }

  default void IsolatedDraw (PGraphicsOpenGL g)
    { BefoDraw (g);
      DrawSelf (g);
      AftaDraw (g);
    }

  default void RecursivelyDraw (PGraphicsOpenGL g)
    { boolean dr = QueryShouldDraw ();
      boolean cdr = QueryShouldDrawChildrenEvenIfNotSelf ();

      if (!dr  &&  ! cdr)
        return;
      
      boolean drf = QueryShouldDrawBeforeChildren ();
      
      BefoDraw (g);
      
      if (dr  &&  drf)
        DrawSelf (g);
      
      if (dr  ||  cdr)
        if (NumChildren ()  >  0)
          for (Zeubject z  :  Children ())
            if (z instanceof P5ZLimnable)
              ((P5ZLimnable)z) . RecursivelyDraw (g);
      
      if (dr  &&  ! drf)
        DrawSelf (g);
      
      AftaDraw (g);
    }
}
