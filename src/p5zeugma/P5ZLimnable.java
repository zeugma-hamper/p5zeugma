
package p5zeugma;


import zeugma.Zeubject;
import zeugma.Limnable;
import zeugma.GrapplerPile;
import zeugma.IContainMultitudes;
import processing.opengl.PGraphicsOpenGL;


public interface P5ZLimnable  extends Limnable, IContainMultitudes
{
  default void BefoDraw (PGraphicsOpenGL g)
    { g . pushMatrix ();

      GrapplerPile gp = UnsecuredGrapplerPile ();
      if (gp != null)
        P5Z.ConcatModelView (g, gp . PntMat (), gp . InvPntMat ());
    }

  default void AftaDraw (PGraphicsOpenGL g)
    { g . popMatrix (); }

  default void DrawSelf (PGraphicsOpenGL g)
    { }

  default void IsolatedDraw (PGraphicsOpenGL g)
    { BefoDraw (g);
      DrawSelf (g);
      AftaDraw (g);
    }

  default void RecursivelyDraw (PGraphicsOpenGL g, long ratch, Limnable.CumuMats cm)
    { boolean dr = QueryShouldDraw ();
      boolean cdr = QueryShouldDrawChildrenEvenIfNotSelf ();

      if (!dr  &&  ! cdr)
        return;
      
      boolean drf = QueryShouldDrawBeforeChildren ();
      
      Limnable.CumuMats pushed_cm = cm;
      cm = DependCumuMatsFrom (cm);

      BefoDraw (g);
      
      if (dr  &&  drf)
        DrawSelf (g);
      
      if (dr  ||  cdr)
        if (NumChildren ()  >  0)
          for (Zeubject z  :  Children ())
            if (z instanceof P5ZLimnable)
              ((P5ZLimnable)z) . RecursivelyDraw (g, ratch, cm);
      
      if (dr  &&  ! drf)
        DrawSelf (g);
      
      AftaDraw (g);
    }
}
