package coursedesign.widget;

import coursedesign.tool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DButton extends JPanel {
    int x,y,offset_x,offset_y;
    boolean inthis,isclick;
    ImageIcon im_normal,im_over,im_click;
    OnClickListener ocl;

    public DButton(){
        super();
        addMouseListener(new MouseListener() {

            public void mouseReleased(MouseEvent e) {
                isclick=true;
                repaint();
            }
            public void mousePressed(MouseEvent e) {
                isclick=false;
                repaint();
            }
            public void mouseExited(MouseEvent e) {
                inthis=false;
                repaint();
            }
            public void mouseEntered(MouseEvent e) {
                inthis=true;
                repaint();
            }
            public void mouseClicked(MouseEvent e) {
                //鼠标按下
                if(ocl!=null)ocl.click();
            }
        });
        setBackground(null);
        setOpaque(false);
    }

    public void setOnClickListener(OnClickListener ocl){
        this.ocl=ocl;
    }

    public void setNormalImage(ImageIcon img){
        this.im_normal=img;
    }
    public void setOverImage(ImageIcon img){
        this.im_over=img;
    }
    public void setClickImage(ImageIcon img){
        this.im_click=img;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(isclick?im_click.getImage():(inthis?im_over.getImage():im_normal.getImage()),offset_x,offset_y,getWidth()-offset_x*2,getHeight()-offset_y*2,im_normal.getImageObserver());
    }

    public interface OnClickListener{
        void click();
    }
}
