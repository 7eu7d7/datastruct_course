package coursedesign.gui;

import coursedesign.widget.Arrow;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class SortPanel extends JPanel {
    Vector<WorkView> works_sort;
    public SortPanel(){
        setLayout(null);
        setSize(1920,300);
        works_sort=topologySort(BasePanel.all_works);
        int count=10;
        for (WorkView wv:works_sort){
            add(wv);
            wv.setLocation(100,count);
            count+=(wv.getWidth()+20);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int i=0;i<works_sort.size();i++){
            Point ptemp=works_sort.get(i+1).getLNodePos(),thispos=works_sort.get(i).getRNodePos();
            Arrow.drawAL(thispos.x,thispos.y,ptemp.x-10,ptemp.y,(Graphics2D) g);
        }
    }

    public static Vector<WorkView> topologySort(Vector<WorkView> works){
        Vector<WorkView> works_sort=new Vector<WorkView>();
        HashMap<WorkView,Integer> templist=new HashMap<WorkView,Integer>();
        for(WorkView wv:works)templist.put(wv,wv.before_works.size());
        Queue<WorkView> wvque=new LinkedList<WorkView>();
        for(WorkView wv:works)if(templist.get(wv)==0)wvque.offer(wv);
        int nsize=0;
        while (wvque.size()>0){
            WorkView wv=wvque.poll();
            works_sort.add(new WorkView(wv));
            for(WorkView next:wv.next_works){
                templist.put(next,nsize=(templist.get(next)-1));
                if (nsize==0)wvque.offer(next);
            }
        }
        return works_sort;
    }
}
