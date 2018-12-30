package coursedesign;

import coursedesign.gui.BasePanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tool {
    public static ImageIcon loadImage(String path){
        return new ImageIcon(Main.frame.getClass().getClassLoader().getSystemResource(path));
    }
    public static ImageIcon loadImage(String path,int w,int h){
        ImageIcon img=new ImageIcon(Main.frame.getClass().getClassLoader().getSystemResource(path));
        img.setImage(img.getImage().getScaledInstance(w,h, Image.SCALE_DEFAULT));
        return img;
    }
    public static boolean isIntersect(double px1,double py1,double px2,double py2,double px3,double py3,double px4,double py4)//判断线段是否相交
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

    public static boolean fuzzyQuery(String res,String aim){//模糊查询
        String aim_fu="";
        for(int i=0;i<aim.length();i++)aim_fu+=(aim.charAt(i)+"*");
        return regexMatch(res,aim_fu);
    }
    public static boolean regexMatch(String res,String aim){//正则表达式匹配
        Pattern p=Pattern.compile(cutlast(aim));
        Matcher m=p.matcher(res);
        return m.find();
    }
    public static String cutlast(String str){
        return str.substring(0,str.length()-1);
    }
    public static String copmleteString(String res,String comp,int count){
        for(int i=res.length();i<count;i++)res=comp+res;
        return res;
    }

    public static File chooseFile(){
        JFileChooser jfc=new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.showDialog(new JLabel(), "选择文件");
        return jfc.getSelectedFile();
    }
}
