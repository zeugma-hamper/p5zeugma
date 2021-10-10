

import p5zeugma.P5ZAlignifer;
import p5zeugma.P5ZLivingMaes;
import p5zeugma.P5ZLimnable;

import zeugma.*;

import processing.core.PConstants;
import processing.core.PImage;
import processing.opengl.PGraphicsOpenGL;


public class ImageSplatter  extends Alignifer
							implements P5ZLimnable, ZESpatialPhagy, ZEYowlPhagy
{ PImage immy;
  double im_wid, im_hei;
  PlatonicMaes cur_maes;
  String grab_prov;
  double grab_off_h, grab_off_v;

  public ImageSplatter (PImage im)
    { super ();
      grab_prov = "";
      AssuredGrapplerPile () . PrependGrappler (new ScGrappler (1.0, -1.0, 1.0));
      immy = im;
      if (im != null)
        { im_wid = (double)(im.width);
          im_hei = (double)(im.height);
          double scaledown = 1.0 / ((im_wid > im_hei)  ?  im_wid  :  im_hei);
          im_wid *= scaledown;
          im_hei *= scaledown;
        }
    }

  public long ZESpatialHarden (ZESpatialHardenEvent e)
    { if (! grab_prov . isEmpty ())
        return 0;
      GrapplerPile gp = AssuredGrapplerPile ();
      Vect cnt = gp.pnt_mat . TransformVect (Vect.zerov);
      Vect o = gp.nrm_mat . TransformVect (Vect.xaxis);
      Vect u = gp.nrm_mat . TransformVect (Vect.yaxis);
      double wid = gp.pnt_mat . TransformVect (Vect.xaxis . Mul (im_wid))
                        . Sub (cnt) . Mag ();
      double hei = gp.pnt_mat . TransformVect (Vect.yaxis . Mul (im_hei))
                        . Sub (cnt) . Mag ();
      Vect hit = Geom.RayRectIntersection (e.loc, e.aim, cnt, o, u, wid, hei);
      if (hit == null)
        return 0;
      grab_prov = e . Provenance ();
      Vect offset = hit . Sub (CurLoc ());
      grab_off_h = offset . Dot (o);
      grab_off_v = offset . Dot (u);
      return 0;
    }

  public long ZESpatialSoften (ZESpatialSoftenEvent e)
    { if (e . Provenance () . equals (grab_prov))
        grab_prov = "";
      return 0;
    }

  public long ZESpatialMove (ZESpatialMoveEvent e)
    { if (! e . Provenance () . equals (grab_prov))
        return 0;
      PlatonicMaes.MaesAndHit mah = P5ZLivingMaes.ClosestAmongLiving (e.loc, e.aim);
      if (mah == null)
        return 0;
      if (mah.maes != cur_maes)
        AlignToMaes (cur_maes = mah.maes);
      GrapplerPile gp = AssuredGrapplerPile ();
      Vect o = gp.nrm_mat . TransformVect (Vect.xaxis);
      Vect u = gp.nrm_mat . TransformVect (Vect.yaxis);
      mah.hit = mah.hit . Sub (o . Mul (grab_off_h))
                        . Sub (u . Mul (grab_off_v));
      LocZoft () . Set (mah.hit);
      
      return 0;
    }


  public long ZEYowlAppear (ZEYowlAppearEvent e)
    { System.out.print ("APPEAR: " + e . Utterance () + "  ");  return 0; }
  public long ZEYowlRepeat (ZEYowlRepeatEvent e)
    { System.out.print ("(rept!) : " + e . Utterance () + "  ");  return 0; }
  public long ZEYowlVanish (ZEYowlVanishEvent e)
    { System.out.println ("VANISH (g'bye): " + e . Utterance ());  return 0; }

  public void DrawSelf (PGraphicsOpenGL g)
    { g . imageMode (PConstants.CENTER);
      g . image (immy, 0.0f, 0.0f, (float)im_wid, (float)im_hei);
    }

}
