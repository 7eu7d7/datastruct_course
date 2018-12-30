package coursedesign.gui;

import coursedesign.Main;
import coursedesign.datastruct.Work;
import coursedesign.widget.DNode;
import coursedesign.widget.DPanel;
import coursedesign.widget.NumField;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class WorkView extends DPanel {
    int item_size=20;
    int offset=3,offset_x=20,offset_y=3;


    float X,Y,last_x,last_y;
    Work work;
    public Vector<WorkView> next_works=new Vector<WorkView>();
    public Vector<WorkView> before_works=new Vector<WorkView>();
    int layer=0;

    NumField tx_early_start,tx_duration,tx_early_end,tx_late_start,tx_free,tx_late_end;
    JTextField la_name;
    DNode node_left,node_right;
    Object userdata;

    int base_w,base_h,scale_count;
    double scale_rate=1,base_x,base_y;

    public WorkView(){
        this.work=new Work();
        init();
    }
    public WorkView(Work work){
        this.work=work;
        init();
    }
    public WorkView(WorkView wv){
        this.work=new Work(wv.getWork());
        init();
    }

    public void init(){
        setLayout(null);
        setOffset(10,0);
        initEdit();
        initMouse();
        initButton();
        setSize(getX(3)+offset_x,getY(3)+offset_y);
        setImage(new ImageIcon(getClass().getClassLoader().getSystemResource("Panel.png")));
        setImage_choose(new ImageIcon(getClass().getClassLoader().getSystemResource("Panel_choose.png")));
        base_w=getWidth();
        base_h=getHeight();
        base_x=getX();
        base_y=getY();
    }

    public void initEdit(){
        //添加可编辑文本框
        tx_early_start=new NumField(work.early_start+"");
        tx_early_start.setEditable(false);
        tx_early_start.setBounds(getX(0),getY(0),item_size,item_size);
        tx_early_start.setHorizontalAlignment(JTextField.CENTER);

        add(tx_early_start);
        tx_duration=new NumField(work.duration+"");
        tx_duration.setBounds(getX(1),getY(0),item_size,item_size);
        tx_duration.setHorizontalAlignment(JTextField.CENTER);
        tx_duration.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                refreshValue();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                refreshValue();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                refreshValue();
            }
        });
        add(tx_duration);
        tx_early_end=new NumField(work.early_end+"");
        tx_early_end.setBounds(getX(2),getY(0),item_size,item_size);
        tx_early_end.setHorizontalAlignment(JTextField.CENTER);
        tx_early_end.setEditable(false);

        add(tx_early_end);

        la_name=new JTextField(work.name);
        la_name.setBounds(getX(0),getY(1),item_size+getX(2)-offset-offset_x,item_size);
        la_name.setHorizontalAlignment(JTextField.CENTER);
        add(la_name);

        tx_late_start=new NumField(work.late_start+"");
        tx_late_start.setBounds(getX(0),getY(2),item_size,item_size);
        tx_late_start.setHorizontalAlignment(JTextField.CENTER);
        tx_late_start.setEditable(false);

        add(tx_late_start);
        tx_free=new NumField(work.free+"");
        tx_free.setBounds(getX(1),getY(2),item_size,item_size);
        tx_free.setHorizontalAlignment(JTextField.CENTER);
        tx_free.setEditable(false);
        add(tx_free);
        tx_late_end=new NumField(work.late_end+"");
        tx_late_end.setBounds(getX(2),getY(2),item_size,item_size);
        tx_late_end.setHorizontalAlignment(JTextField.CENTER);
        tx_late_end.setEditable(false);
        add(tx_late_end);
    }

    public void initMouse(){
        //添加鼠标事件
        addMouseListener(new MouseListener() {

            public void mouseReleased(MouseEvent e) {
                //鼠标松开

            }
            public void mousePressed(MouseEvent e) {
                //鼠标按下
                X=e.getXOnScreen();
                Y=e.getYOnScreen();
                last_x=getX();
                last_y=getY();
            }
            public void mouseExited(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseClicked(MouseEvent e) {
                /*if(BasePanel.work_choose==WorkView.this){
                    choosed=false;
                    BasePanel.work_choose=null;
                }else{
                    if(BasePanel.work_choose!=null)BasePanel.work_choose.choosed=false;
                    choosed=true;
                    BasePanel.work_choose=WorkView.this;
                }*/
                choose(!ischoosed());
            }
        });
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                setLocation((int)(last_x+e.getXOnScreen()-X),(int)(last_y+e.getYOnScreen()-Y));
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {


            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    public void initButton(){
        node_left=new DNode();
        node_left.setBounds(0,(getY(3)+offset_y)/2-10,20,20);
        node_left.setOnClickListener(new DNode.OnClickListener() {
            @Override
            public void down() {

            }

            @Override
            public void up() {

            }

            @Override
            public void enter() {
                BasePanel.passby_wrok=WorkView.this;
            }

            @Override
            public void exit() {
                BasePanel.passby_wrok=null;
            }
        });
        add(node_left);

        node_right=new DNode();
        node_right.setBounds(getX(3)+offset_x-20,(getY(3)+offset_y)/2-10,20,20);
        node_right.setOnClickListener(new DNode.OnClickListener() {
            @Override
            public void down() {
                //记录选择的Work
                BasePanel.click_work=WorkView.this;
                //JOptionPane.showConfirmDialog(null, "down", "是否继续", JOptionPane.YES_NO_OPTION);
            }

            @Override
            public void up() {
                //下一个Work
                if(BasePanel.passby_wrok!=null){
                    BasePanel.click_work.lineto(BasePanel.passby_wrok);
                    Main.ma.base.cal_timeparam();
                }
                BasePanel.passby_wrok=null;
                BasePanel.click_work=null;
                BasePanel.now_pos=null;
            }

            @Override
            public void enter() {

            }

            @Override
            public void exit() {

            }
        });
        node_right.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                BasePanel.now_pos=new Point(e.getXOnScreen()-4-Main.frame.getX(),e.getYOnScreen()- (27+Main.menubar1.getHeight()+31)-Main.frame.getY());
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
        add(node_right);
    }

    @Override
    public void paint(Graphics g) {
        if(scale_count>0)inScaleAnim((Graphics2D)g);
        super.paint(g);
    }

    @Override
    public void poi() {
        super.poi();
        if(scale_count>0) {
            scale_rate = 1 + 0.5 * ((scale_count / 3) * Math.sin(scale_count)) / 30;
            scale_count--;
            double sw=base_w*scale_rate,sh=base_h*scale_rate;
            //System.out.println(sw+","+sh);
            //setLocation((int)(base_x-(sw-base_w)/2),(int)(base_y-(sh-base_h)/2));
            setSize((int)sw,(int)sh);
        }

    }

    public void inScaleAnim(Graphics2D g2d){
        double sw=base_w*scale_rate,sh=base_h*scale_rate;
        g2d.scale(scale_rate,scale_rate);
        g2d.translate((base_x-(sw-base_w)/2),(base_y-(sh-base_h)/2));
    }
    public void showScaleAnim(){
        scale_count=20;
    }

    @Override
    public void choose(boolean cho) {
        if(cho!=ischoosed())showScaleAnim();
        super.choose(cho);

    }

    public Point getRNodePos(){
        return new Point(getX()+node_right.getX()+node_right.getWidth()/2,getY()+node_right.getY()+node_right.getHeight()/2);
    }

    public Point getLNodePos(){
        return new Point(getX()+node_left.getX()+node_left.getWidth()/2,getY()+node_left.getY()+node_left.getHeight()/2);
    }

    public void refreshValue(){
        getWork();
        Main.ma.base.cal_timeparam();
    }
    public void lineto(WorkView next){
        next_works.add(next);
        next.before_works.add(this);
    }
    public void remove(WorkView next){
        next.before_works.remove(this);
        next_works.remove(next);
    }
    public Work getWork(){
        work.name=la_name.getText();
        work.early_start=Integer.parseInt(tx_early_start.getText());
        try {
            work.duration = Integer.parseInt(tx_duration.getText());
        }catch (Exception e){
            work.duration=0;
        }
        work.early_end=Integer.parseInt(tx_early_end.getText());
        work.late_start=Integer.parseInt(tx_late_start.getText());
        work.free=Integer.parseInt(tx_free.getText());
        work.late_end=Integer.parseInt(tx_late_end.getText());

        return work;
    }

    public int getX(int col){
        return item_size*col+offset*(col+1)+offset_x;
    }
    public int getY(int row){
        return item_size*row+offset*(row+1)+offset_y;
    }
}
