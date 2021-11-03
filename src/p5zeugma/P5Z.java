
package p5zeugma;


import zeugma.Vect;
import zeugma.Matrix44;

import processing.core.PMatrix;
import processing.core.PMatrix3D;
import processing.opengl.PGraphicsOpenGL;


public class P5Z
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
}
