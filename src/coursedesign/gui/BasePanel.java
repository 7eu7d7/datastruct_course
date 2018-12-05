package coursedesign.gui;

import coursedesign.Main;
import coursedesign.datastruct.Work;
import coursedesign.tool;
import coursedesign.widget.Arrow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class BasePanel extends JPanel {
    public static Vector<WorkView> all_works=new Vector<WorkView>();
    public static WorkView click_work,passby_wrok;
    public static Point now_pos;

    int startX,startY,endX,endY;
    boolean moving;

    public BasePanel(){
        setLayout(null);
        Work work=new Work();
        work.early_start=1;
        work.name="fuck";

        WorkView wv=new WorkView(work);
        WorkView wv2=new WorkView(work);
        wv2.setLocation(300,300);
        //wv.lineto(wv2);
        addWork(wv);
        addWork(wv2);
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        //然后得到当前键盘事件的管理器
        manager.addKeyEventPostProcessor(getMyKeyEventHandler());
        //然后为管理器添加一个新的键盘事件监听者。

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(Main.mode==Main.DELLINE) {
                    endX = startX = e.getXOnScreen() - 4 - Main.frame.getX();
                    endY = startY = e.getYOnScreen() - (27 + Main.menubar1.getHeight() + 31) - Main.frame.getY();
                    moving = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(Main.mode==Main.DELLINE){
                    moving=false;
                    removeArrows();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(Main.mode==Main.DELLINE) {
                    endX = e.getXOnScreen() - 4 - Main.frame.getX();
                    endY = e.getYOnScreen() - (27 + Main.menubar1.getHeight() + 31) - Main.frame.getY();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
    }
    public KeyEventPostProcessor getMyKeyEventHandler() {
        return new KeyEventPostProcessor()//返回一个实现KeyEventPostProcessor接口的匿名内部类。
        {
            public boolean postProcessKeyEvent(KeyEvent e)//实现postProcessKeyEvent方法
            {
                if(e.getKeyCode()==KeyEvent.VK_DELETE){
                        for (int i=0;i<all_works.size();i++){
                            WorkView wv=all_works.get(i);
                            if(wv.ischoosed()){
                                removeWork(wv);
                                i--;
                            }
                        }
                        return true;
                }
                return false;
            }
        };
    }
    public void addWork(WorkView wv){
        all_works.add(wv);
        add(wv);
    }
    public void removeWork(WorkView wv){
        for(WorkView i:wv.before_works){
            i.next_works.remove(wv);
        }
        for(WorkView i:wv.next_works){
            i.before_works.remove(wv);
        }
        all_works.remove(wv);
        remove(wv);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(moving){
            g.setColor(Color.RED);
            g.drawLine(startX,startY,endX,endY);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d=(Graphics2D)g;
        if(click_work!=null&&now_pos!=null){
            Point click_pos=click_work.getRNodePos();
            Arrow.drawAL(click_pos.x,click_pos.y,now_pos.x,now_pos.y,g2d);
        }
        drawArrows(g2d);//绘制图节点间的箭头
    }

    public void clearChoose(){
        for(WorkView wv:all_works)wv.choosed=false;
    }

    public void drawArrows(Graphics2D g2d){
        Queue<WorkView> que=new LinkedList<WorkView>();//使用队列进行广度优先遍历
        Vector<WorkView> visit_list=new Vector<WorkView>();//储存已加入过的节点
        for(WorkView wv:all_works)
            if(wv.before_works.isEmpty())que.offer(wv);
        //遍历图
        while (true){
            if(que.size()<=0)break;
            WorkView nowwv=que.poll();
            if(visit_list.contains(nowwv))continue;
            Point thispos=nowwv.getRNodePos();
            visit_list.add(nowwv);
            //使用迭代器进行遍历，提高效率
            for(WorkView wv:nowwv.next_works){
                Point ptemp=wv.getLNodePos();
                Arrow.drawAL(thispos.x,thispos.y,ptemp.x-10,ptemp.y,g2d);
                que.offer(wv);
            }
        }
    }
    public void removeArrows(){
        Queue<WorkView> que=new LinkedList<WorkView>();//使用队列进行广度优先遍历
        Vector<WorkView> visit_list=new Vector<WorkView>();//储存已加入过的节点
        for(WorkView wv:all_works)
            if(wv.before_works.isEmpty())que.offer(wv);
        //遍历图
        while (true){
            if(que.size()<=0)break;
            WorkView nowwv=que.poll();
            if(visit_list.contains(nowwv))continue;
            Point thispos=nowwv.getRNodePos();
            visit_list.add(nowwv);

            for(int i=0;i<nowwv.next_works.size();i++){
                WorkView wv=nowwv.next_works.get(i);
                Point ptemp=wv.getLNodePos();
                if(tool.isIntersect(thispos.x,thispos.y,ptemp.x-10,ptemp.y,startX,startY,endX,endY)){
                    nowwv.remove(wv);
                    i--;
                }
                que.offer(wv);
            }
        }
    }



}
