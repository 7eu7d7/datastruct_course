package coursedesign;

import coursedesign.datastruct.Work;
import coursedesign.gui.BasePanel;
import coursedesign.gui.WorkView;
import coursedesign.widget.DPanel;
import coursedesign.widget.NumField;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        ma.showMainFrame();


    }

    public void showMainFrame(){
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame=new JFrame("From");
        content=frame.getContentPane();
        frame.setSize(1000,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
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
        //文件菜单
        {
            JMenu menu_file = new JMenu("文件");
            menubar1.add(menu_file);
            JMenuItem item1 = new JMenuItem("子菜单1");
            JMenuItem item2 = new JMenuItem("子菜单2");
            menu_file.add(item1);
            menu_file.addSeparator();
            menu_file.add(item2);
            menu_file.addSeparator();
        }
        //选项菜单
        {
            JMenu menu_todo = new JMenu("选项");
            menubar1.add(menu_todo);
            JMenuItem item1 = new JMenuItem("查询");
            item1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showSearchFrame();
                }
            });
            JMenuItem item2 = new JMenuItem("模糊查询");
            item2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showFuzzySearchFrame();
                }
            });
            JMenuItem item3 = new JMenuItem("筛选");
            item3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showFilterFrame();
                }
            });
            menu_todo.add(item1);
            menu_todo.addSeparator();
            menu_todo.add(item2);
            menu_todo.addSeparator();
            menu_todo.add(item3);
            menu_todo.addSeparator();
        }
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

    public void showFuzzySearchFrame(){
        JFrame sframe=new JFrame("模糊查询");
        sframe.setSize(230,100);
        sframe.getContentPane().setLayout(null);
        sframe.setLocationRelativeTo(null);
        JTextField txf=new JTextField();
        sframe.getContentPane().add(txf);
        sframe.getContentPane().add(Box.createHorizontalStrut(10));
        JButton bu=new JButton("查找");
        bu.setSize(60,30);
        bu.setLocation(150,(100-30-26)/2);
        sframe.getContentPane().add(bu);
        txf.setSize(130,30);
        txf.setLocation(10,(100-30-26)/2);

        bu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                base.clearChoose();
                String text=txf.getText();
                for(WorkView wv:base.all_works){
                    if(tool.fuzzyQuery(wv.getWork().name,text))wv.choosed=true;
                }
                sframe.dispose();
            }
        });
        sframe.setVisible(true);
    }

    public void showSearchFrame(){
        JFrame sframe=new JFrame("查询");
        sframe.setSize(230,130);
        sframe.getContentPane().setLayout(null);
        sframe.setLocationRelativeTo(null);
        JTextField txf=new JTextField();
        sframe.getContentPane().add(txf);
        JButton bu=new JButton("查找");
        bu.setSize(60,30);
        bu.setLocation(150,(100-30-26)/2+30);
        sframe.getContentPane().add(bu);
        txf.setSize(130,30);
        txf.setLocation(10,(100-30-26)/2+30);

        // 创建开关按钮
        JToggleButton toggleBtn = new JToggleButton("正则表达式查询");
        toggleBtn.setSize(150,20);
        toggleBtn.setLocation(40,20);
        sframe.getContentPane().add(toggleBtn);

        bu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                base.clearChoose();
                String text=txf.getText();
                for(WorkView wv:base.all_works){
                    if(toggleBtn.isSelected()?tool.regexMatch(wv.getWork().name,text):wv.getWork().name.equals(text))wv.choosed=true;
                }
                sframe.dispose();
            }
        });
        sframe.setVisible(true);
    }

    public void showFilterFrame(){
        JFrame fframe=new JFrame("筛选");
        fframe.setSize(260,130);
        fframe.getContentPane().setLayout(null);
        fframe.setLocationRelativeTo(null);

        JComboBox comboBox_name=new JComboBox();
        comboBox_name.addItem("最早开始时间");
        comboBox_name.addItem("持续时间");
        comboBox_name.addItem("最早结束时间");
        comboBox_name.addItem("最迟开始时间");
        comboBox_name.addItem("自由时差");
        comboBox_name.addItem("最迟结束时间");
        comboBox_name.setSize(110,30);
        comboBox_name.setLocation(10,(130-30-35)/2-20);
        fframe.getContentPane().add(comboBox_name);

        JComboBox comboBox_condition=new JComboBox();
        comboBox_condition.addItem("=");
        comboBox_condition.addItem(">");
        comboBox_condition.addItem("<");
        comboBox_condition.addItem("≥");
        comboBox_condition.addItem("≤");
        comboBox_condition.addItem("≠");
        comboBox_condition.setSize(50,30);
        comboBox_condition.setLocation(125,(130-30-35)/2-20);
        fframe.getContentPane().add(comboBox_condition);

        NumField txf=new NumField("");
        txf.setSize(60,30);
        txf.setLocation(180,(130-30-35)/2-20);
        fframe.getContentPane().add(txf);
        JButton bu=new JButton("查找");
        bu.setSize(60,30);
        bu.setLocation(50,(130-30-35)/2+20);
        bu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                base.clearChoose();
                int value=txf.getValue();
                for(WorkView wv:base.all_works){
                    Work wk=wv.getWork();
                    switch (comboBox_name.getSelectedIndex()){
                        case 0:if(dofilter(wk.early_start,comboBox_condition.getSelectedIndex(),txf.getValue()))wv.choosed=true;
                        case 1:if(dofilter(wk.duration,comboBox_condition.getSelectedIndex(),txf.getValue()))wv.choosed=true;
                        case 2:if(dofilter(wk.early_end,comboBox_condition.getSelectedIndex(),txf.getValue()))wv.choosed=true;
                        case 3:if(dofilter(wk.late_start,comboBox_condition.getSelectedIndex(),txf.getValue()))wv.choosed=true;
                        case 4:if(dofilter(wk.free,comboBox_condition.getSelectedIndex(),txf.getValue()))wv.choosed=true;
                        case 5:if(dofilter(wk.late_end,comboBox_condition.getSelectedIndex(),txf.getValue()))wv.choosed=true;
                    }
                }
                fframe.dispose();
            }
        });
        fframe.getContentPane().add(bu);
        JButton bu_qx=new JButton("取消");
        bu_qx.setSize(60,30);
        bu_qx.setLocation(140,(130-30-35)/2+20);
        bu_qx.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               fframe.dispose();
            }
        });
        fframe.getContentPane().add(bu_qx);

        fframe.setVisible(true);
    }

    public boolean dofilter(int var,int oprate,int value){
        switch (oprate){
            case 0: return var==value;
            case 1: return var>value;
            case 2: return var<value;
            case 3: return var>=value;
            case 4: return var<=value;
            case 5: return var!=value;
            default:return false;
        }
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
