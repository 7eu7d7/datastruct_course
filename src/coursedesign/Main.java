package coursedesign;

import coursedesign.datastruct.Work;
import coursedesign.gui.BasePanel;
import coursedesign.gui.WorkView;
import coursedesign.widget.DPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

public class Main {
    public static Insets main_insets;
    public static JMenuBar menubar1;
    public static JFrame frame;
    public static BasePanel base;
    DPanel floatpanel=new DPanel();
    Container content;

    public static int mode=0;

    public static final int NORMAL=0;
    public static final int DELLINE=1;

    public static void main(String[] args){
        Main ma=new Main();
        ma.showFrame();

    }

    public void showFrame(){
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame=new JFrame("From");
        content=frame.getContentPane();
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        content.setLayout(null);
        main_insets=frame.getInsets();

        base=new BasePanel();
        base.setSize(1920,1080);
        content.add(base);

        initToolBar();

        base.setLocation(0,30);
        frame.setVisible(true);
        render.start();
    }

    public void initToolBar(){
        menubar1 = new JMenuBar();
        frame.setJMenuBar(menubar1);
        JMenu menu1=new JMenu("菜单1");
        menubar1.add(menu1);
        JMenuItem item1=new JMenuItem("子菜单1");
        JMenuItem item2=new JMenuItem("子菜单2");
        menu1.add(item1);
        menu1.addSeparator();
        menu1.add(item2);
        menu1.addSeparator();

        //工具条
        JToolBar bar = new JToolBar();
        JButton bu_addwork = new JButton();
        bu_addwork.setOpaque(false);
        bu_addwork.setContentAreaFilled(false);
        bu_addwork.setMargin(new Insets(0, 0, 0, 0));
        bu_addwork.setBorderPainted(false);
        bu_addwork.setRolloverEnabled(true);  //允许翻转图标
        bu_addwork.setIcon(tool.loadImage("./add.png",25,25));
        bu_addwork.setRolloverIcon(tool.loadImage("./add_over.png",25,25));
        bu_addwork.setPressedIcon(tool.loadImage("./add.png",25,25));

        bu_addwork.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                floatpanel.setImage(tool.loadImage("./add.png",25,25));
                floatpanel.setVisible(true);
                floatpanel.setLocation(e.getXOnScreen()-frame.getX()-3,e.getYOnScreen()-frame.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                floatpanel.setVisible(false);
                WorkView wv_temp=new WorkView();
                wv_temp.setLocation(e.getXOnScreen()-frame.getX()-3-wv_temp.getWidth()/2,e.getYOnScreen()-(27+Main.menubar1.getHeight()+31)-frame.getY()-wv_temp.getHeight()/2);
                base.addWork(wv_temp);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        bu_addwork.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                floatpanel.setLocation(e.getXOnScreen()-frame.getX()-15-3,e.getYOnScreen()-frame.getY()-(27+Main.menubar1.getHeight()+15));
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        JButton bu_delline = new JButton();
        bu_delline.setOpaque(false);
        bu_delline.setContentAreaFilled(false);
        bu_delline.setMargin(new Insets(0, 0, 0, 0));
        bu_delline.setBorderPainted(false);
        bu_delline.setRolloverEnabled(true);  //允许翻转图标
        bu_delline.setIcon(tool.loadImage("./scissors.png",25,25));
        bu_delline.setRolloverIcon(tool.loadImage("./scissors_over.png",25,25));
        bu_delline.setPressedIcon(tool.loadImage("./scissors.png",25,25));
        bu_delline.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(mode!=1){
                    mode=1;
                    Toolkit tk = Toolkit.getDefaultToolkit();
                    Image image = tool.loadImage("./scissors.png",25,25).getImage();
                    Cursor cursor = tk.createCustomCursor(image, new Point(10, 10), "norm");
                    base.setCursor(cursor); //panel 也可以是其他组件
                }
                else{
                    mode=0;
                    base.setCursor(Cursor.getDefaultCursor());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        bar.add(bu_addwork);
        bar.add(bu_delline);
        bar.setSize(1920,30);
        content.add(bar);
        floatpanel.setSize(30,30);
        floatpanel.setVisible(false);
        content.add(floatpanel,0);
    }

    //渲染线程
    Thread render=new Thread(){
        @Override
        public void run() {
            while (true){
                content.repaint();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
