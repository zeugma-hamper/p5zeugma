
package p5zeugma;


import zeugma.Zeubject;
import zeugma.ZeColor;
import zeugma.ZoftThing;
import zeugma.Vect;
import zeugma.Matrix44;
import zeugma.CumuMats;
import zeugma.Limnable;
import zeugma.GrapplerPile;
import zeugma.IContainMultitudes;

import processing.opengl.PGraphicsOpenGL;
import processing.core.PMatrix3D;


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


  default void PZvertex (PGraphicsOpenGL g, Vect p)
    { g . vertex ((float)p.x, (float)p.y, (float)p.z); }


  default void PZpoint (PGraphicsOpenGL g, Vect p)
    { g . point ((float)p.x, (float)p.y, (float)p.z); }

  default void PZline (PGraphicsOpenGL g, Vect p1, Vect p2)
    { g . line ((float)p1.x, (float)p1.y, (float)p1.z,
                (float)p2.x, (float)p2.y, (float)p2.z);
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

//
/// the following are not strictly necessary, but the idea here is that
/// we want these methods to be accessible from as many code contexts
/// as possible. (i.e. whatever class you're "in", this being java...)
//

  public static PMatrix3D ToP (Matrix44 m)
    { return PZ.ToP (m); }

  public static void PZConcatModelView (PGraphicsOpenGL g,
                                        Matrix44 fwd_mat, Matrix44 inv_mat)
    { PZ.ConcatModelView (g, fwd_mat, inv_mat); }

  public static void PZModelViewFlipYAxis (PGraphicsOpenGL g)
    { PZ.ModelViewFlipYAxis (g); }

//
///
//
}
