
package p5zeugma;


import zeugma.Zeubject;
import zeugma.ZeColor;
import zeugma.CumuMats;
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

  default void RecursivelyDraw (PGraphicsOpenGL g, long ratch,
                                CumuMats cm, ZeColor adjc_above)
    { boolean dr = QueryShouldDraw ();
      boolean drc = QueryShouldDrawChildrenEvenIfNotSelf ();
      boolean ccm = QueryShouldCalcCumuMatsEvenIfNotDrawing ();

      if (! dr  &&  ! drc  &&  ! ccm)
        return;

      boolean drf = QueryShouldDrawBeforeChildren ();

      CumuMats cur_cm = CurrentCumuMats ();
      if (cur_cm != null
          &&  (ratch <= cur_cm.rat_fresh  &&  ratch >= 0))
        { }   // i.e. only don't if it's already freshly done
      else
        { cur_cm = DependCumuMatsFrom (cm);
          if (ratch >= 0)
            cur_cm.rat_fresh = ratch;
        }

      ZeColor adjc = AdjColor ();
      ZeColor cumuc = (adjc != null)  ?  adjc_above . Mul (adjc)  :  adjc_above;
      SetCumuAdjColor (cumuc);
      BefoDraw (g);

      if (dr  &&  drf)
        DrawSelf (g);

      if (dr  ||  drc)  // note the lack of " || ccm "...
        if (NumChildren ()  >  0)
          for (Zeubject z  :  Children ())
            if (z instanceof P5ZLimnable)
              ((P5ZLimnable)z) . RecursivelyDraw (g, ratch, cur_cm, cumuc);
      // might be nice to be able to continue the recursion even for
      // non-PZLimnables...

      if (dr  &&  ! drf)
        DrawSelf (g);

      AftaDraw (g);
    }
}
