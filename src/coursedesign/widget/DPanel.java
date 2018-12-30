package coursedesign.widget;

import javax.swing.*;
import java.awt.*;

public class DPanel extends JPanel {
    ImageIcon img,img_choose;
    int offset_x,offset_y;
    private boolean choosed;

    double mdx,mdy,nowx,nowy;
    int movetime=0;
    public int aimx,aimy;

    public DPanel(){
        setBackground(null);
        setOpaque(false);
    }

    public void setImage(ImageIcon img){
        this.img=img;
    }
    public void setImage_choose(ImageIcon img){
        this.img_choose=img;
    }
    public void setOffset(int x,int y){
        offset_x=x;offset_y=y;
    }
    public void choose(boolean cho){
        choosed=cho;
    }
    public boolean ischoosed(){
        return choosed;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(img!=null)g.drawImage(choosed?img_choose.getImage():img.getImage(),offset_x,offset_y,getWidth()-offset_x*2,getHeight()-offset_y*2,img.getImageObserver());
    }

    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        aimx=x;
        aimy=y;
    }

    public void moveto(double aimx, double aimy, int time){
        movetime=time;
        this.aimx=(int)aimx;
        this.aimy=(int)aimy;
        mdx=20*(aimx-getX())/time;
        mdy=20*(aimy-getY())/time;
        nowx=getX();
        nowy=getY();
    }
    public void poi(){
        if(movetime>0) {
            nowx+=mdx;
            nowy+=mdy;
            setLocation((int)nowx, (int)nowy);
            movetime -= 20;
        }
    }
}
