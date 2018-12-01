package coursedesign;

import coursedesign.gui.BasePanel;

import javax.swing.*;
import java.awt.*;

public class tool {
    public static ImageIcon loadImage(String path){
        return new ImageIcon(Main.frame.getClass().getClassLoader().getSystemResource(path));
    }
    public static ImageIcon loadImage(String path,int w,int h){
        ImageIcon img=new ImageIcon(Main.frame.getClass().getClassLoader().getSystemResource(path));
        img.setImage(img.getImage().getScaledInstance(w,h, Image.SCALE_DEFAULT));
        return img;
    }
    public static boolean isIntersect(double px1,double py1,double px2,double py2,double px3,double py3,double px4,double py4)//p1-p2 is or not intersect with p3-p4
    {
        boolean flag = false;
        double d = (px2-px1)*(py4-py3) - (py2-py1)*(px4-px3);
        if(d!=0)
        {
            double r = ((py1-py3)*(px4-px3)-(px1-px3)*(py4-py3))/d;
            double s = ((py1-py3)*(px2-px1)-(px1-px3)*(py2-py1))/d;
            if((r>=0) && (r <= 1) && (s >=0) && (s<=1))
            {
                flag = true;
            }
        }
        return flag;
    }
}
