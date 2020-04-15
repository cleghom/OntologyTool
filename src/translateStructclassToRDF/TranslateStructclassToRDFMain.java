package translateStructclassToRDF;

import ontMainUtils.HanLPMain;
import ontMainUtils.object;
import ontMainUtils.objectProperty;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class TranslateStructclassToRDFMain {

    /**
     * @param
     */
    static String myontologymodelclass = "http://myontologymodelclass/";
    static String myontologymodelproperty = "http://myontologymodelproperty/";

    public static void main(String[] args) {
        OntModel ontModel = ModelFactory.createOntologyModel();// 新建
        String s = "中华人民共和国是国家。胡锦涛是中华人民共和国的主席.艾滋病的感染性是强的。艾滋病的疾病传播方式是性传播和母婴传播。鸟的翅膀数量是2。麻雀是鸟，啄木鸟是鸟，乌鸦是鸟。";
        OntModel oo = mainfunction(ontModel, s, "D:/213542354345654435545.txt");
        write(oo, "D:/213542354345654435545.txt");
    }

    /*
     * 主函数以每行句子为单位执行
     */
    public static OntModel mainfunction(OntModel ontmodel, String string, String fileuri) {

        ArrayList<object> _ol = new ArrayList<object>();
        HanLPMain.mainfunction(string);// 处理后的句子生成的结构体类赋值给sc
        _ol = HanLPMain.getStructclass();

        for (object _o : _ol) {
            @SuppressWarnings("unused")
            int _object_type = _o.objectType;
            @SuppressWarnings("unused")
            String _object_name = _o.objectName;
            ArrayList<object> _object_parent = _o.parentObject;
            ArrayList<object> _object_sub = _o.subObject;
            ArrayList<objectProperty> _object_property = _o.objectproperty;

            Resource newResource = ontmodel.createProperty(myontologymodelclass + _o.objectName);

            if (_object_parent != null)
                for (object _o_parent : _object_parent) {
                    System.out.println(_o_parent.objectName);
                }

            if (_object_sub != null)
                for (object _o_sub : _object_sub) {
                    System.out.println(_o_sub.objectName);
                }

            if (_object_property != null)
                for (objectProperty _o_property : _object_property) {
                    String propertyname = _o_property.propertyName;
                    String propertyvalue = _o_property.propertyValue;
                    Property newproperty = ontmodel.createProperty(myontologymodelclass, propertyname);
                    newResource.addProperty(newproperty, propertyvalue);
                }
        }
        return ontmodel;
    }

    public static void write(OntModel o, String fileuri) {
        FileOutputStream outFile;
        try {
            outFile = new FileOutputStream(fileuri);
            Writer out = new OutputStreamWriter(outFile);
            o.write(out);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
