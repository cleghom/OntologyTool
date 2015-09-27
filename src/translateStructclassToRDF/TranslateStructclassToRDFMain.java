package translateStructclassToRDF;

import hanLP.HanLPMain;
import hanLP.StructClass;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author Zhao Hang
 * @date:2015-4-23下午5:27:11
 * @email:1610227688@qq.com
 */
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
	 * 主函数以每行句子为单位执�?
	 */
	public static OntModel mainfunction(OntModel ontmodel, String string, String fileuri) {
		ArrayList<StructClass> sc = new ArrayList<StructClass>();
		sc = HanLPMain.mainfunction(string);// 处理后的句子生成的结构体类赋值给sc
		int x = sc.size();
		while (x-- > 0) {
			System.out.println("类关系");
			String classname = sc.get(x).ontObjectName;
			String parentclassname = sc.get(x).ontObjectParentName;
			int classpropertynumber = sc.get(x).ontObjectProperty.size();
			System.out.println("子类：" + classname + "---->父类：" + parentclassname + "---->类的属性个数:" + classpropertynumber);

			Resource newResource = ontmodel.createProperty(myontologymodelclass + classname);

			int y = sc.get(x).ontObjectProperty.size();
			while (y-- > 0) {
				System.out.println("属性关系");
				String propertyname = sc.get(x).ontObjectProperty.get(y).ontObjectPropertyName.toString();
				String propertyvalue = sc.get(x).ontObjectProperty.get(y).ontObjectPropertyValue.toString();
				System.out.println(propertyname + "---->" + propertyvalue);
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
