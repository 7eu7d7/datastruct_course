package coursedesign.gui;

import coursedesign.Main;
import coursedesign.datastruct.Work;
import coursedesign.widget.DNode;
import coursedesign.widget.DPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class WorkView extends DPanel {
    int item_size=20;
    int offset=3,offset_x=20,offset_y=3;


    float X,Y,last_x,last_y;
    Work work;
    Vector<WorkView> next_works=new Vector<WorkView>();
    Vector<WorkView> before_works=new Vector<WorkView>();

    JTextField la_es,la_ls,la_tf,la_name,la_ef,la_lf,la_ff;
    DNode node_left,node_right;

    public WorkView(){
        this.work=new Work();
        init();
    }
    public WorkView(Work work){
        this.work=work;
        init();
    }

    public void init(){
        setLayout(null);
        setOffset(10,0);
        initEdit();
        initMouse();
        initButton();
        setSize(getX(3)+offset_x,getY(3)+offset_y);
        setImage(new ImageIcon(getClass().getClassLoader().getSystemResource("./Panel.png")));
        setImage_choose(new ImageIcon(getClass().getClassLoader().getSystemResource("./Panel_choose.png")));
    }

    public void initEdit(){
        //添加可编辑文本框
        la_es=new JTextField(work.es+"");
        la_es.setBounds(getX(0),getY(0),item_size,item_size);
        la_es.setHorizontalAlignment(JTextField.CENTER);
        add(la_es);
        la_ls=new JTextField(work.ls+"");
        la_ls.setBounds(getX(1),getY(0),item_size,item_size);
        la_ls.setHorizontalAlignment(JTextField.CENTER);
        add(la_ls);
        la_tf=new JTextField(work.tf+"");
        la_tf.setBounds(getX(2),getY(0),item_size,item_size);
        la_tf.setHorizontalAlignment(JTextField.CENTER);
        la_tf.setEditable(false);

        add(la_tf);

        la_name=new JTextField(work.name);
        la_name.setBounds(getX(0),getY(1),item_size+getX(2)-offset-offset_x,item_size);
        la_name.setHorizontalAlignment(JTextField.CENTER);
        add(la_name);

        la_ef=new JTextField(work.ef+"");
        la_ef.setBounds(getX(0),getY(2),item_size,item_size);
        la_ef.setHorizontalAlignment(JTextField.CENTER);
        add(la_ef);
        la_lf=new JTextField(work.lf+"");
        la_lf.setBounds(getX(1),getY(2),item_size,item_size);
        la_lf.setHorizontalAlignment(JTextField.CENTER);
        add(la_lf);
        la_ff=new JTextField(work.ff+"");
        la_ff.setBounds(getX(2),getY(2),item_size,item_size);
        la_ff.setHorizontalAlignment(JTextField.CENTER);
        la_ff.setEditable(false);
        add(la_ff);
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
                if(BasePanel.work_choose==WorkView.this){
                    choosed=false;
                    BasePanel.work_choose=null;
                }else{
                    if(BasePanel.work_choose!=null)BasePanel.work_choose.choosed=false;
                    choosed=true;
                    BasePanel.work_choose=WorkView.this;
                }
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

    public Point getRNodePos(){
        return new Point(getX()+node_right.getX()+node_right.getWidth()/2,getY()+node_right.getY()+node_right.getHeight()/2);
    }

    public Point getLNodePos(){
        return new Point(getX()+node_left.getX()+node_left.getWidth()/2,getY()+node_left.getY()+node_left.getHeight()/2);
    }

    public void lineto(WorkView next){
        next_works.add(next);
        next.before_works.add(this);
    }
    public void remove(WorkView next){
        next.before_works.remove(this);
        next_works.remove(next);
    }

    public int getX(int col){
        return item_size*col+offset*(col+1)+offset_x;
    }
    public int getY(int row){
        return item_size*row+offset*(row+1)+offset_y;
    }
}
