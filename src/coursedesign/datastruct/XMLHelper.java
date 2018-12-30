package coursedesign.datastruct;

import coursedesign.gui.WorkView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XMLHelper {
    public static Vector<WorkView> readWorks(File file){
        Vector<WorkView> works=new Vector<WorkView>();

        // 创建SAXReader的对象reader
        SAXReader reader = new SAXReader();
        try {
            // 通过reader对象的read方法加载books.xml文件,获取docuemnt对象。
            Document document = reader.read(file);
            // 通过document对象获取根节点
            Element root = document.getRootElement();
            List<Element> childElements = root.elements();
            for (Element child : childElements) {
                Work work=new Work();

                work.early_start=Integer.parseInt(child.elementText("early_start"));
                work.duration=Integer.parseInt(child.elementText("duration"));
                work.early_end=Integer.parseInt(child.elementText("early_end"));
                work.late_start=Integer.parseInt(child.elementText("late_start"));
                work.free=Integer.parseInt(child.elementText("free"));
                work.late_end=Integer.parseInt(child.elementText("late_end"));
                work.name=child.elementText("name");

                WorkView wv=new WorkView(work);
                wv.setLocation(Integer.parseInt(child.attributeValue("x")),Integer.parseInt(child.attributeValue("y")));
                works.add(wv);
            }

            int i=0;
            for (Element child : childElements){
                for(Element before:(List<Element>)child.element("befores").elements()){
                    works.get(i).before_works.add(works.get(Integer.parseInt(before.getText())));
                }
                for(Element next:(List<Element>)child.element("nexts").elements()){
                    works.get(i).next_works.add(works.get(Integer.parseInt(next.getText())));
                }
                i++;
            }
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return works;
    }

    public static void saveWorks(Vector<WorkView> works,File file){

        try {
            // 创建xml
            Document doc = DocumentHelper.createDocument();
            // 创建document对象获取根节点
            Element root = doc.addElement("root");
            List<Element> childElements = root.elements();
            int i=0;
            for (WorkView wv : works) {
                Work work=wv.getWork();
                Element item=root.addElement("work");

                item.addElement("early_start").setText(work.early_start+"");
                item.addElement("duration").setText(work.duration+"");
                item.addElement("early_end").setText(work.early_end+"");
                item.addElement("late_start").setText(work.late_start+"");
                item.addElement("free").setText(work.free+"");
                item.addElement("late_end").setText(work.late_end+"");
                item.addElement("name").setText(work.name);

                item.addAttribute("x",wv.getX()+"");
                item.addAttribute("y",wv.getY()+"");
                item.addAttribute("id",i+"");

                Element befores=item.addElement("befores");
                Element nexts=item.addElement("nexts");

                for(WorkView before:wv.before_works)befores.addElement("item").setText(works.indexOf(before)+"");
                for(WorkView next:wv.next_works)nexts.addElement("item").setText(works.indexOf(next)+"");

                //实例化输出格式对象
                OutputFormat format = OutputFormat.createPrettyPrint();
                //设置输出编码
                format.setEncoding("UTF-8");
                //生成XMLWriter对象，构造函数中的参数为需要输出的文件流和格式
                XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
                //开始写入，write方法中包含上面创建的Document对象
                writer.write(doc);

                i++;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
