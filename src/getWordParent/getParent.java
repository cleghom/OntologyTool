package getWordParent;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import staticVariable.staticvalue;

public class getParent {

    public static void main(String[] args) {
        String temp = getparentAttr("nr");
        System.out.println(temp);
        temp = getparentAttr("n");
        System.out.println(temp);
        temp = getparentAttr("nnd");
        System.out.println(temp);
        temp = getparentAttr("ns");
        System.out.println(temp);
        temp = getparentAttr("nb");
        System.out.println(temp);
        temp = getparentAttr("g");
        System.out.println(temp);
        temp = getparentAttr("vvvv");
        System.out.println(temp);
    }

    public static String getparentAttr(String wordnature) {
        SAXReader reader = new SAXReader();
        Document document;
        String parent = "no";
        try {
            document = reader.read((new File(staticvalue.projecturl + "\\bin\\naturetag.xml")));
            org.dom4j.Node root = document.selectSingleNode("/tag");
            @SuppressWarnings("rawtypes")
            java.util.List list = root.selectNodes("node[@nature='" + wordnature + "']");
            if (list.size() > 0) {
                Element e = (Element) list.get(0);
                parent = e.attributeValue("parent");
            }
        } catch (DocumentException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return parent;
    }
}
