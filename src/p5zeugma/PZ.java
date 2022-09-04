
package p5zeugma;


import zeugma.Vect;
import zeugma.ZeColor;
import zeugma.Matrix44;
import zeugma.ZoftThing;
import zeugma.PlatonicMaes;

import processing.core.PMatrix;
import processing.core.PMatrix3D;
import processing.core.PGraphics;
import processing.opengl.PGraphicsOpenGL;


public class PZ
{ public static Matrix44 yflip_mat;
  public static PMatrix3D p5_yflip_mat;

  static
    { yflip_mat = new Matrix44 (1.0,  0.0,  0.0,  0.0,
                                0.0, -1.0,  0.0,  0.0,
                                0.0,  0.0,  1.0,  0.0,
                                0.0,  0.0,  0.0,  1.0);
      p5_yflip_mat = ToP (yflip_mat);
    }


  public static PMatrix3D ToP (Matrix44 m)
    { // note -- please, won't you? -- the implicit transpose following:
        return new PMatrix3D
            ((float)m.m00, (float)m.m10, (float)m.m20, (float)m.m30,
             (float)m.m01, (float)m.m11, (float)m.m21, (float)m.m31,
             (float)m.m02, (float)m.m12, (float)m.m22, (float)m.m32,
             (float)m.m03, (float)m.m13, (float)m.m23, (float)m.m33);
    }

  public static void ConcatModelView (PGraphicsOpenGL g,
                                      Matrix44 fwd_mat, Matrix44 inv_mat)
    { PMatrix3D p_fwd = ToP (fwd_mat);
      PMatrix3D p_inv = ToP (inv_mat);

      g.modelview . apply (p_fwd);
      g.modelviewInv . preApply (p_inv);
      g.projmodelview . apply (p_fwd);
    }

  public static void ModelViewFlipYAxis (PGraphicsOpenGL g)
    { g.modelview . apply (p5_yflip_mat);
      g.modelviewInv . preApply (p5_yflip_mat);
      g.projmodelview . apply (p5_yflip_mat);
    }

  public static PZMaesBundle MaesBundleByPGraphics (PGraphics g)
    { for (PZMaesBundle mbun  :  PZMaesBundle.AllMaesBundles ())
        if (g  ==  mbun.most_recent_pgraphics)
          return mbun;
      return null;
    }

  public static PlatonicMaes MaesByPGraphics (PGraphics g)
    { for (PZMaesBundle mbun  :  PZMaesBundle.AllMaesBundles ())
        if (g  ==  mbun.most_recent_pgraphics)
          return mbun.its_maes;
      return null;
    }

  public static double ActualToAsIfPixelRatioFor (PGraphics g)
    { PZMaesBundle mbun = MaesBundleByPGraphics (g);
      if (mbun != null)
        return mbun . ActualToAsIfPixelRatio ();
      return 1.0;
    }

//
///
//

  public static void fill (PGraphicsOpenGL g, ZoftThing <ZeColor> cz)
    { PZ.fill (g, (cz != null) ? cz.val : ZeColor.white); }

  public static void fill (PGraphicsOpenGL g, ZeColor c)
    { // ZeColor adj = CumuAdjColor ();
      g . fill (255.0f * c.r,
                255.0f * c.g,
                255.0f * c.b,
                255.0f * c.a);
    }

  public static void stroke (PGraphicsOpenGL g, ZoftThing <ZeColor> cz)
    { PZ.stroke (g, (cz != null) ? cz.val : ZeColor.white); }

  public static void stroke (PGraphicsOpenGL g, ZeColor c)
    { //ZeColor adj = CumuAdjColor ();
      g . stroke (255.0f * c.r,
                  255.0f * c.g,
                  255.0f * c.b,
                  255.0f * c.a);
    }

  public static void tint (PGraphicsOpenGL g, ZoftThing <ZeColor> cz)
    { PZ.tint (g, (cz != null) ? cz.val : ZeColor.white); }

  public static void tint (PGraphicsOpenGL g, ZeColor c)
    { //ZeColor adj = CumuAdjColor ();
      g . tint (255.0f * c.r,
                255.0f * c.g,
                255.0f * c.b,
                255.0f * c.a);
    }


  public void vertex (PGraphicsOpenGL g, Vect p)
    { g . vertex ((float)p.x, (float)p.y, (float)p.z); }


  public static void point (PGraphicsOpenGL g, Vect p)
    { g . point ((float)p.x, (float)p.y, (float)p.z); }

  public static void line (PGraphicsOpenGL g, Vect p1, Vect p2)
    { g . line ((float)p1.x, (float)p1.y, (float)p1.z,
                (float)p2.x, (float)p2.y, (float)p2.z);
    }
}
