package coursedesign;

import coursedesign.datastruct.Work;
import coursedesign.datastruct.XMLHelper;
import coursedesign.gui.BasePanel;
import coursedesign.gui.SortPanel;
import coursedesign.gui.WorkView;
import coursedesign.widget.DPanel;
import coursedesign.widget.NumField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
    public static Insets main_insets;
    public static JMenuBar menubar1;
    public static JFrame frame;//主窗体
    public static BasePanel base;//背景平面
    DPanel floatpanel=new DPanel();
    Container content;//主窗体内含平面

    public static int mode=0;//鼠标模式

    public static final int NORMAL=0;
    public static final int DELLINE=1;
    public static Main ma;

    public static void main(String[] args){
        ma=new Main();
        frame=new JFrame("From");
        ma.showStartFrame();
        ma.showMainFrame();//显示主窗体


    }

    public void showStartFrame(){
        JFrame start_frame  =   new  JFrame( " Transparent Window " );
        start_frame.setLayout(null);
        start_frame.setSize(960,540);
        start_frame.setLocationRelativeTo(null);
        start_frame.setUndecorated(true);
        DPanel dp=new DPanel();
        dp.setImage_choose(new ImageIcon());
        dp.setSize(960,540);
        dp.setLocation(0,0);
        start_frame.add(dp);
        start_frame.setBackground(new Color(0,0,0,0));
        start_frame.setVisible(true);
        new Thread(){
            @Override
            public void run() {
                for(int i=0;i<120;i++) {
                    long start=System.currentTimeMillis();
                    dp.setImage(tool.loadImage("start/start"+tool.copmleteString(i+"","0",3)+".png"));
                    dp.repaint();
                    try {
                        Thread.sleep(Math.max(0,33-(System.currentTimeMillis()-start)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                start_frame.dispose();
                frame.setVisible(true);//显示主窗体
                render.start();//开启渲染线程
            }
        }.start();
    }

    public void showMainFrame(){//显示主窗体
        JFrame.setDefaultLookAndFeelDecorated(true);//让界面更好看
        frame=new JFrame("From");
        content=frame.getContentPane();//获取内含平面
        frame.setSize(1000,800);//设置大小
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭主窗体时程序结束
        frame.setLocationRelativeTo(null);//窗体放在屏幕中间
        content.setLayout(null);//不用布局管理器
        main_insets=frame.getInsets();

        base=new BasePanel();//new一个背景平面
        base.setSize(1920,1080);//设置大小
        content.add(base);//加入主窗体

        initToolBar();//设置工具栏

        base.setLocation(0,30);

    }

    public void initToolBar(){//设置工具栏
        menubar1 = new JMenuBar();
        frame.setJMenuBar(menubar1);//给主窗体添加工具栏
        //文件菜单
        {
            JMenu menu_file = new JMenu("文件");
            menubar1.add(menu_file);
            JMenuItem item1 = new JMenuItem("保存");
            item1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    base.saveWorks();
                }
            });
            JMenuItem item2 = new JMenuItem("读取");
            item2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    base.removeAllWork();
                    base.addAllWork(XMLHelper.readWorks(tool.chooseFile()));
                }
            });
            menu_file.add(item1);
            menu_file.addSeparator();
            menu_file.add(item2);
            menu_file.addSeparator();
        }
        //查看菜单
        {
            JMenu mene_view = new JMenu("查看");
            menubar1.add(mene_view);
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
            mene_view.add(item1);
            mene_view.addSeparator();
            mene_view.add(item2);
            mene_view.addSeparator();
            mene_view.add(item3);
            mene_view.addSeparator();
        }
        //选项菜单
        {
            JMenu menu_todo = new JMenu("选项");
            menubar1.add(menu_todo);
            JMenuItem item1 = new JMenuItem("拓扑排序");
            item1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showTopologyFrame();
                }
            });
            JMenuItem item2 = new JMenuItem("关键路径");
            item2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showKeyPath();
                }
            });
            JMenuItem item3 = new JMenuItem("自动排序");
            item3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    base.autoSort();
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
        JButton bu_addwork = new JButton();//新建添加工作按钮
        bu_addwork.setOpaque(false);//设置透明
        bu_addwork.setContentAreaFilled(false);
        bu_addwork.setMargin(new Insets(0, 0, 0, 0));//设置边距为0
        bu_addwork.setBorderPainted(false);//不画边框
        bu_addwork.setRolloverEnabled(true);  //允许翻转图标
        bu_addwork.setIcon(tool.loadImage("add.png",25,25));//设置平常图片
        bu_addwork.setRolloverIcon(tool.loadImage("add_over.png",25,25));//设置鼠标滑过时图片
        bu_addwork.setPressedIcon(tool.loadImage("add.png",25,25));//设置点击时图片

        bu_addwork.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //鼠标单击
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //鼠标按下
                floatpanel.setImage(tool.loadImage("add.png",25,25));//给浮动平面设置图片
                floatpanel.setVisible(true);//设置为可见
                floatpanel.setLocation(e.getXOnScreen()-frame.getX()-3,e.getYOnScreen()-frame.getY());//跟随鼠标移动
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                floatpanel.setVisible(false);//设置为不可见
                WorkView wv_temp=new WorkView();//新建一个工作
                wv_temp.setLocation(e.getXOnScreen()-frame.getX()-3-wv_temp.getWidth()/2,e.getYOnScreen()-(27+Main.menubar1.getHeight()+31)-frame.getY()-wv_temp.getHeight()/2);
                base.addWork(wv_temp);//加入平面
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
                //鼠标拖动
                floatpanel.setLocation(e.getXOnScreen()-frame.getX()-15-3,e.getYOnScreen()-frame.getY()-(27+Main.menubar1.getHeight()+15));//跟随鼠标
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
        bu_delline.setIcon(tool.loadImage("scissors.png",25,25));
        bu_delline.setRolloverIcon(tool.loadImage("scissors_over.png",25,25));
        bu_delline.setPressedIcon(tool.loadImage("scissors.png",25,25));
        bu_delline.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(mode!=1){
                    mode=1;
                    //更改鼠标图片
                    Toolkit tk = Toolkit.getDefaultToolkit();
                    Image image = tool.loadImage("scissors.png",25,25).getImage();
                    Cursor cursor = tk.createCustomCursor(image, new Point(10, 10), "norm");
                    base.setCursor(cursor); //panel 也可以是其他组件
                }
                else{
                    mode=0;
                    base.setCursor(Cursor.getDefaultCursor());//还原鼠标图片
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
        
        JButton bu_addhead = new JButton();
        bu_addhead.setOpaque(false);
        bu_addhead.setContentAreaFilled(false);
        bu_addhead.setMargin(new Insets(0, 0, 0, 0));
        bu_addhead.setBorderPainted(false);
        bu_addhead.setRolloverEnabled(true);  //允许翻转图标
        bu_addhead.setIcon(tool.loadImage("addhead.png",25,25));
        bu_addhead.setRolloverIcon(tool.loadImage("addhead_over.png",25,25));
        bu_addhead.setPressedIcon(tool.loadImage("addhead.png",25,25));
        bu_addhead.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                base.addHeadAndEnd();
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
        bar.add(bu_addhead);
        bar.setSize(1920,30);
        content.add(bar);
        floatpanel.setSize(30,30);
        floatpanel.setVisible(false);
        content.add(floatpanel,0);
    }

    public void showFuzzySearchFrame(){//显示模糊查询界面
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
                    if(tool.fuzzyQuery(wv.getWork().name,text))wv.choose(true);
                }
                sframe.dispose();
            }
        });
        sframe.setVisible(true);
    }

    public void showSearchFrame(){//显示查询界面
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
                    if(toggleBtn.isSelected()?tool.regexMatch(wv.getWork().name,text):wv.getWork().name.equals(text))wv.choose(true);
                }
                sframe.dispose();
            }
        });
        sframe.setVisible(true);
    }

    public void showFilterFrame(){//显示筛选界面
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
                        case 0:if(dofilter(wk.early_start,comboBox_condition.getSelectedIndex(),txf.getValue()))wv.choose(true);break;
                        case 1:if(dofilter(wk.duration,comboBox_condition.getSelectedIndex(),txf.getValue()))wv.choose(true);break;
                        case 2:if(dofilter(wk.early_end,comboBox_condition.getSelectedIndex(),txf.getValue()))wv.choose(true);break;
                        case 3:if(dofilter(wk.late_start,comboBox_condition.getSelectedIndex(),txf.getValue()))wv.choose(true);break;
                        case 4:if(dofilter(wk.free,comboBox_condition.getSelectedIndex(),txf.getValue()))wv.choose(true);break;
                        case 5:if(dofilter(wk.late_end,comboBox_condition.getSelectedIndex(),txf.getValue()))wv.choose(true);break;
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
    public void showTopologyFrame(){//显示拓扑排序
        JFrame tframe=new JFrame("拓扑排序");


        SortPanel sp=new SortPanel();
        tframe.setSize(sp.getWidth(),sp.getHeight());
        tframe.getContentPane().setLayout(null);
        tframe.setLocationRelativeTo(null);
        tframe.add(sp);
        tframe.setVisible(true);
    }
    public void showKeyPath(){//显示关键路径
        BasePanel.key_works=base.getKeyWay();
        for(WorkView wv:BasePanel.key_works)wv.choose(true);
    }

    public boolean dofilter(int var,int oprate,int value){//执行筛选条件
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
                base.poi();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
