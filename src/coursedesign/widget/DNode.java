package coursedesign.widget;

import coursedesign.gui.BasePanel;
import coursedesign.tool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class DNode extends JPanel {
    int x,y,offset_x,offset_y;
    boolean inthis;
    ImageIcon im_normal,im_over;
    OnClickListener ocl;

    public DNode(){
        super();
        setNormalImage(tool.loadImage("./ball.png"));
        setOverImage(tool.loadImage("./ball_over.png"));

        addMouseListener(new MouseListener() {

            public void mouseReleased(MouseEvent e) {
                if(ocl!=null)ocl.up();
            }
            public void mousePressed(MouseEvent e) {
                if(ocl!=null)ocl.down();
            }
            public void mouseExited(MouseEvent e) {
                inthis=false;
                if(ocl!=null)ocl.exit();
                repaint();
            }
            public void mouseEntered(MouseEvent e) {
                inthis=true;
                if(ocl!=null)ocl.enter();
                repaint();
            }
            public void mouseClicked(MouseEvent e) {
                //鼠标按下
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(inthis?im_over.getImage():im_normal.getImage(),offset_x,offset_y,getWidth()-offset_x*2,getHeight()-offset_y*2,im_normal.getImageObserver());
    }

    public interface OnClickListener{
        void down();
        void up();
        void enter();
        void exit();
    }
}
