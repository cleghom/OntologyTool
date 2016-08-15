package translateStructclassToRDF;

import hanLP.HanLPMain;
import hanLP._object;
import hanLP._objectproperty;

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

		ArrayList<_object> _ol = new ArrayList<_object>();
		HanLPMain.mainfunction(string);// 处理后的句子生成的结构体类赋值给sc
		_ol = HanLPMain.getStructclass();

		for (_object _o : _ol) {
			@SuppressWarnings("unused")
			int _object_type = _o.objecttype;
			@SuppressWarnings("unused")
			String _object_name = _o.objectname;
			ArrayList<_object> _object_parent = _o.parent_object;
			ArrayList<_object> _object_sub = _o.sub_object;
			ArrayList<_objectproperty> _object_property = _o.objectproperty;

			Resource newResource = ontmodel.createProperty(myontologymodelclass + _o.objectname);

			if (_object_parent != null)
				for (_object _o_parent : _object_parent) {
					System.out.println(_o_parent.objectname);
				}

			if (_object_sub != null)
				for (_object _o_sub : _object_sub) {
					System.out.println(_o_sub.objectname);
				}

			if (_object_property != null)
				for (_objectproperty _o_property : _object_property) {
					String propertyname = _o_property.propertyname;
					String propertyvalue = _o_property.propertyvalue;
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
