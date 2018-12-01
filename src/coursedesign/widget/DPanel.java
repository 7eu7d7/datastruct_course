package coursedesign.widget;

import javax.swing.*;
import java.awt.*;

public class DPanel extends JPanel {
    ImageIcon img,img_choose;
    int offset_x,offset_y;
    public boolean choosed;

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(choosed?img_choose.getImage():img.getImage(),offset_x,offset_y,getWidth()-offset_x*2,getHeight()-offset_y*2,img.getImageObserver());
    }
}
