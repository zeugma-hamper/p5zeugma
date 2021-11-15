
package p5zeugma;


import zeugma.Zeubject;
import zeugma.ZeColor;
import zeugma.ZoftThing;
import zeugma.CumuMats;
import zeugma.Limnable;
import zeugma.GrapplerPile;
import zeugma.IContainMultitudes;
import processing.opengl.PGraphicsOpenGL;


public interface PZLimnable extends Limnable, IContainMultitudes
{
  default void BefoDraw (PGraphicsOpenGL g)
    { g . pushMatrix ();

      GrapplerPile gp = UnsecuredGrapplerPile ();
      if (gp != null)
        PZ.ConcatModelView (g, gp . PntMat (), gp . InvPntMat ());
    }

  default void AftaDraw (PGraphicsOpenGL g)
    { g . popMatrix (); }


  default void PZfill (PGraphicsOpenGL g, ZoftThing <ZeColor> cz)
    { PZfill (g, (cz != null) ? cz.val : ZeColor.white); }

  default void PZfill (PGraphicsOpenGL g, ZeColor c)
    { ZeColor adj = CumuAdjColor ();
      g . fill (255.0f * adj.r * c.r,
                255.0f * adj.g * c.g,
                255.0f * adj.b * c.b,
                255.0f * adj.a * c.a);
    }

  default void PZstroke (PGraphicsOpenGL g, ZoftThing <ZeColor> cz)
    { PZstroke (g, (cz != null) ? cz.val : ZeColor.white); }

  default void PZstroke (PGraphicsOpenGL g, ZeColor c)
    { ZeColor adj = CumuAdjColor ();
      g . stroke (255.0f * adj.r * c.r,
                  255.0f * adj.g * c.g,
                  255.0f * adj.b * c.b,
                  255.0f * adj.a * c.a);
    }

  default void PZtint (PGraphicsOpenGL g, ZoftThing <ZeColor> cz)
    { PZtint (g, (cz != null) ? cz.val : ZeColor.white); }

  default void PZtint (PGraphicsOpenGL g, ZeColor c)
    { ZeColor adj = CumuAdjColor ();
      g . tint (255.0f * adj.r * c.r,
                255.0f * adj.g * c.g,
                255.0f * adj.b * c.b,
                255.0f * adj.a * c.a);
    }


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
            if (z instanceof PZLimnable)
              ((PZLimnable)z) . RecursivelyDraw (g, ratch, cur_cm, cumuc);
      // might be nice to be able to continue the recursion even for
      // non-PZLimnables...

      if (dr  &&  ! drf)
        DrawSelf (g);

      AftaDraw (g);
    }
}
