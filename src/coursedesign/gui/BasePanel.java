package coursedesign.gui;

import coursedesign.Main;
import coursedesign.datastruct.Work;
import coursedesign.datastruct.XMLHelper;
import coursedesign.tool;
import coursedesign.widget.Arrow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class BasePanel extends JPanel {
    public static Vector<WorkView> all_works=new Vector<WorkView>();
    public static Vector<WorkView> key_works=new Vector<WorkView>();
    public static WorkView click_work,passby_wrok;
    public static Point now_pos;

    int startX,startY,endX,endY;
    boolean moving;

    public BasePanel(){
        setLayout(null);
        /*Work work=new Work();
        work.early_start=1;
        work.name="666";

        WorkView wv=new WorkView(work);
        WorkView wv2=new WorkView(work);
        wv2.setLocation(300,300);

        addWork(wv);
        addWork(wv2);*/
        /*int count=0;

        for(int i=0;i<26;i++,count++){
            Work work=new Work();
            work.duration=(int)(Math.random()*100+20);
            work.name=""+(char)('A'+count);
            WorkView wv=new WorkView(work);
            wv.setLocation(30+(i%10)*200,200*(i/10));
            addWork(wv);
        }*/

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
                    endX = startX = e.getXOnScreen() - 4 - Main.frame.getX();
                    endY = startY = e.getYOnScreen() - (27 + Main.menubar1.getHeight() + 31) - Main.frame.getY();
                    moving = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(Main.mode==Main.DELLINE){
                    moving=false;
                    removeArrows();
                    cal_timeparam();
                }else{
                    moving=false;
                    Rectangle rect=new Rectangle(Math.min(startX,endX),Math.min(startY,endY),Math.abs(endX-startX), Math.abs(endY-startY));
                    for (WorkView wv:all_works){
                        if(rect.contains(wv.getBounds()))wv.choose(!wv.ischoosed());
                    }
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
                    endX = e.getXOnScreen() - 4 - Main.frame.getX();
                    endY = e.getYOnScreen() - (27 + Main.menubar1.getHeight() + 31) - Main.frame.getY();
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
                if(e.isControlDown()&&e.getKeyCode()==KeyEvent.VK_S){
                    saveWorks();
                }
                if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
                    if(!key_works.isEmpty())key_works.removeAllElements();
                }

                return false;
            }
        };
    }
    public void saveWorks(){
        XMLHelper.saveWorks(all_works,tool.chooseFile());
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
        cal_timeparam();
    }

    public void addAllWork(Vector<WorkView> works){
        for(WorkView wv:works)add(wv);
        all_works.addAll(  works);
    }

    public void removeAllWork(){
        for(WorkView wv:all_works)remove(wv);
        all_works.removeAllElements();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(moving){
            if(Main.mode==Main.DELLINE) {
                g.setColor(Color.RED);
                g.drawLine(startX, startY, endX, endY);
            }else{
                Graphics2D g2d=(Graphics2D)g;
                /*g2d.setColor(new Color(0xae,0xe4,0xff,0xff));
                g2d.setStroke(new BasicStroke());
                g2d.drawRect(startX, startY, endX-startX, endY-startY);*/
                g2d.setColor(new Color(0x66,0xcc,0xff,0x90));
                g2d.fillRect(Math.min(startX,endX),Math.min(startY,endY),Math.abs(endX-startX), Math.abs(endY-startY));
            }
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
        for(WorkView wv:all_works)wv.choose(false);
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
                if(key_works.size()>0&&(key_works.contains(nowwv)&&key_works.get(key_works.indexOf(nowwv)+1)==wv))g2d.setColor(Color.red);
                else g2d.setColor(Color.black);
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

    public void addHeadAndEnd(){
        int head=0;
        for(WorkView wv:all_works)
            if(wv.before_works.isEmpty())head++;
        if(head>1){
            int min_x = 100000000, total_y = 0, count = 0;
            WorkView wvtemp = new WorkView();
            for (WorkView wv : all_works)
                if (wv.before_works.isEmpty()) {
                    wvtemp.lineto(wv);
                    count++;
                    min_x = Math.min(min_x, wv.getX());
                    total_y += wv.getY();
                }
            wvtemp.setLocation(min_x - 150, total_y / count);
            addWork(wvtemp);
        }

        int end=0;
        for(WorkView wv:all_works)
            if(wv.next_works.isEmpty())end++;
        if(end>1){
            int max_x = -10000, total_y = 0, count = 0;
            WorkView wvtemp = new WorkView();
            for (WorkView wv : all_works)
                if (wv.next_works.isEmpty()) {
                    wv.lineto(wvtemp);
                    count++;
                    max_x = Math.max(max_x, wv.getX());
                    total_y += wv.getY();
                }
            wvtemp.setLocation(max_x+150 , total_y / count);
            addWork(wvtemp);
        }
        cal_timeparam();
    }

    public void autoSort(){
        key_works.removeAllElements();
        //补充头尾结点
        addHeadAndEnd();

        HashMap<Integer,Integer> layercount=new HashMap<Integer,Integer>();
        HashMap<Integer,Integer> layer_y=new HashMap<Integer,Integer>();

        //自动排列
        Queue<WorkView> que=new LinkedList<WorkView>();//使用队列进行广度优先遍历
        Vector<WorkView> visit_list=new Vector<WorkView>();//储存已加入过的节点
        for(WorkView wv:all_works) {
            layercount.put(wv.layer,layercount.getOrDefault(wv.layer,0)+1);
            if (wv.before_works.isEmpty()) que.offer(wv);
        }
        int wvh=all_works.get(0).getHeight();
        for(Iterator iter=layercount.entrySet().iterator();iter.hasNext();){
            Map.Entry<Integer,Integer> entry=(Map.Entry<Integer,Integer>)iter.next();
            layer_y.put(entry.getKey(),(600+entry.getValue()*(wvh+20)-20)/2);
        }
        //遍历图
        WorkView first=((LinkedList<WorkView>) que).get(0);
        //first.setLocation(30,(800-first.getHeight())/2);
        while (true){
            if(que.size()<=0)break;
            WorkView nowwv=que.poll();
            if(visit_list.contains(nowwv))continue;
            //System.out.println(nowwv.getWork().name+","+nowwv.layer);
            nowwv.moveto(30+nowwv.layer*(nowwv.getWidth()+30),layer_y.get(nowwv.layer),500);
            layer_y.put(nowwv.layer,layer_y.get(nowwv.layer)-(wvh+20));
            visit_list.add(nowwv);
            //使用迭代器进行遍历，提高效率
            for(WorkView wv:nowwv.next_works){
                que.offer(wv);
            }
        }
    }

    public void cal_timeparam(){
        Queue<WorkView> que=new LinkedList<WorkView>();//使用队列进行广度优先遍历
        Vector<WorkView> visit_list=new Vector<WorkView>();//储存已加入过的节点
        for(WorkView wv:all_works) {
            wv.userdata=0;
            wv.layer=0;
            if (wv.before_works.isEmpty()) que.offer(wv);
        }
        //遍历图
        while (true){
            if(que.size()<=0)break;
            WorkView nowwv=que.poll();
            //if(visit_list.contains(nowwv))continue;
            visit_list.add(nowwv);
            //使用迭代器进行遍历，提高效率
            for(WorkView wv:nowwv.next_works){
                if((int)nowwv.userdata+nowwv.getWork().duration>(int)wv.userdata){
                    wv.userdata=(int)nowwv.userdata+nowwv.getWork().duration;
                }
                wv.layer=Math.max(wv.layer,nowwv.layer+1);
                que.offer(wv);
            }
        }
        for(WorkView wv:all_works){
            wv.tx_early_start.setValue((int)wv.userdata);
            wv.tx_early_end.setValue((int)wv.userdata+wv.getWork().duration);
            if(wv.next_works.isEmpty()) {
                wv.tx_free.setValue(0);
            }else{
                int min=Integer.MAX_VALUE;
                for(WorkView temp:wv.next_works)min=Math.min((int)temp.userdata,min);
                wv.tx_free.setValue(min-wv.getWork().early_end);
            }
            wv.tx_late_start.setValue(wv.getWork().early_start+wv.getWork().free);
            wv.tx_late_end.setValue(wv.getWork().late_start+wv.getWork().duration);
        }

    }

    public Vector<WorkView> getKeyWay(){
        HashMap<WorkView,Integer> templist=new HashMap<WorkView,Integer>();
        for(WorkView wv:all_works)templist.put(wv,wv.before_works.size());
        Queue<WorkView> wvque=new LinkedList<WorkView>();
        for(WorkView wv:all_works){
            wv.userdata=new Vector<WorkView>();
            if(templist.get(wv)==0) {
                wvque.offer(wv);
                ((Vector<WorkView>)(wv.userdata)).add(wv);
            }
        }
        int nsize=0;
        while (wvque.size()>0){
            WorkView wv=wvque.poll();
            for(WorkView next:wv.next_works){
                if(next.getWork().free==0&&wv.getWork().free==0){
                    ((Vector<WorkView>)(next.userdata)).removeAllElements();
                    ((Vector<WorkView>)(next.userdata)).addAll((Vector<WorkView>)(wv.userdata));
                    ((Vector<WorkView>)(next.userdata)).add(next);
                    if(next.next_works.size()==0)return (Vector<WorkView>)(next.userdata);
                }
                templist.put(next,nsize=(templist.get(next)-1));
                if (nsize==0)wvque.offer(next);
            }
        }
        return null;
    }

    public void poi(){
        for(WorkView wv:all_works)wv.poi();
    }
}
