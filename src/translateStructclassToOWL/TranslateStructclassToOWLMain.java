package translateStructclassToOWL;

import hanLP.HanLPMain;
import hanLP.StructClass;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter.OWLModelWriter;

/*
 * 本方法主要使用了smi的protege3.5的相关jar包
 */
/**
 * @author Zhao Hang
 * @date:2015-5-2下午2:55:23
 * @email:1610227688@qq.com
 */
public class TranslateStructclassToOWLMain {
	/**
	 * @param args
	 * @throws OntologyLoadException
	 * @throws IOException
	 */

	/*
	 * 
	 * 测试主方法
	 * 
	 * 
	 * public static void main(String[] args) throws OntologyLoadException,
	 * IOException { String s =
	 * "植物是生物。人是智慧生物。人是劳动工具。智慧生物是生物。劳动工具是工具。人是动物，动物是生物。刘晓涛是一个中国人。德国是一个政权组织。Eclipse是一个代码工具，代码工具是工具。胡锦涛是中华人民共和国的主席,中国人是人。美国人是人。胡锦涛是一个中国人。奥巴马是一个美国人。德国是一个国家。奥巴马是一个美国人。中华人民共和国是一个国家。"
	 * ; String ss =
	 * "赵航是云南大学的学生。小刘的昵称是刘二，小刘是一个厨师，刘大伟的昵称是伟伟。刘大伟是一个厨师。小王的性别是男，小王的年龄是27.小王的父亲是老王，小王的母亲是张丽。张丽是一个教师。老王是一个工人。小王是一个厨师，小王是一个裁缝。植物是生物。人是智慧生物。人是劳动工具。智慧生物是生物。德国是一个政权组织,德国是国家。张强是我校的学生会主席。刘涛的电话号码是13866655333。赵航的电话号码是14787806414。赵航的学生证封面颜色是红的。赵航是一个中国人。"
	 * ; String sss =
	 * "鹅是鸟。癌症的致死率是高的。癌症的潜伏期是5年的。癌症是一个疾病。艾滋病的潜伏期是12年。艾滋病的感染性是强的。艾滋病的疾病传播方式是性传播。艾滋病是一个疾病。毛毛的羽毛颜色是黑色的。毛毛是一个麻雀。鸟的翅膀个数是2，大雁是鸟。牛牛的年龄是10.大雁的羽毛颜色是白色的。鹅的羽毛颜色是白色的。牛牛是一个大雁，麻雀的翅膀个数是2，麻雀是鸟。"
	 * ; String ssss = "Eclipse的性能是ok的。感冒的潜伏期是2天。感冒是一个疾病。"; String sssss = "";
	 * String fileuri = "D:/owlmodel/aaaaaaaa.owl"; mainfunction(s + ss + sss +
	 * ssss + sssss, fileuri, "base", "kkkkkkk"); }
	 */
	
	/*
	 * 携入 操作
	 */
	public static void writetoFile(String s, OWLModel o) throws IOException {
		
		String filePathOut01 = "D:/owlmodel/" + s + ".owl";
		// 写入：
		FileOutputStream outFile = new FileOutputStream(filePathOut01);
		Writer out = new OutputStreamWriter(outFile, "UTF-8");
		OWLModelWriter omw = new OWLModelWriter(o, o.getTripleStoreModel().getActiveTripleStore(), out);
		omw.write();
		out.close();
	}

	public static OWLModel mainfunction(String string, String fileuri, String Resource, String filename) throws OntologyLoadException, IOException {
		OWLModel o = ProtegeOWL.createJenaOWLModel();

		o.getNamespaceManager().setDefaultNamespace("http://" + Resource + "#");
		ArrayList<StructClass> sc = new ArrayList<StructClass>();// 新建ArrayList准备保存从string中获取到并已处理的表。
		sc = HanLPMain.mainfunction(string);// 处理后的句子生成的结构体类赋值给sc
		int scClassSize = sc.size();

		while (scClassSize-- > 0) {
			System.out.println(scClassSize + ":");
			String classname = sc.get(scClassSize).ontObjectName;
			String parentclassname = sc.get(scClassSize).ontObjectParentName;
			int classpropertynumber = sc.get(scClassSize).ontObjectProperty.size();
			int classindividualnumber = sc.get(scClassSize).ontObjectIndividual.size();
			if (!parentclassname.equals("root") & parentclassname != null) {// 以下试将此类添加进本体
				tryToAddClassIntoOWLModel(o, classname);
				// 以下试将父类添加进本体
				tryToAddClassIntoOWLModel(o, parentclassname);
				// 以下试将此类和父类构造一个父子类的关系
				tryToAddSubclassIntoOWLModel(o, classname, parentclassname);
			}
			int scPropertySize = classpropertynumber;
			int scIndividualSize = classindividualnumber;
			while (scIndividualSize-- > 0) {
				String individualname = sc.get(scClassSize).ontObjectIndividual.get(scIndividualSize).ontObjectIndividualName;
				System.out.println("实例名称为->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + individualname);
				tryToAddObjectIndividualIntoOWLModel(o, classname, individualname);
			}
			while (scPropertySize-- > 0) {
				// 尝试将类的属性添加进模型
				String propertyname = sc.get(scClassSize).ontObjectProperty.get(scPropertySize).ontObjectPropertyName;
				String propertyvalue = sc.get(scClassSize).ontObjectProperty.get(scPropertySize).ontObjectPropertyValue;
				tryToAddObjectPropertyIntoOWLModel(o, propertyname);
				attachPropertyTOClassOrIndividual(o, classname, propertyname, propertyvalue);
			}
		}
		writetoFile(filename, o);
		return o;
	}

	public static OWLModel mainfunction(OWLModel owlModel, String info, String OwlOntBaseURI, String writetoFileName) throws OntologyLoadException,
			IOException {
		// 要更改的模型
		// 输入的信息
		// 根目录
		// 输出到文件名
		OWLModel o = ProtegeOWL.createJenaOWLModel();
		o = owlModel;

		o.getNamespaceManager().setDefaultNamespace("http://" + OwlOntBaseURI + "#");
		ArrayList<StructClass> sc = new ArrayList<StructClass>();// 新建ArrayList准备保存从string中获取到并已处理的表。
		sc = HanLPMain.mainfunction(info);// 处理后的句子生成的结构体类赋值给sc
		int scClassSize = sc.size();

		while (scClassSize-- > 0) {
			System.out.println(scClassSize + ":");
			String classname = sc.get(scClassSize).ontObjectName;
			String parentclassname = sc.get(scClassSize).ontObjectParentName;
			int classpropertynumber = sc.get(scClassSize).ontObjectProperty.size();
			int classindividualnumber = sc.get(scClassSize).ontObjectIndividual.size();
			tryToAddClassIntoOWLModel(o, classname);
			if (!parentclassname.equals("root") & parentclassname != null) {
				tryToAddClassIntoOWLModel(o, parentclassname);
				tryToAddSubclassIntoOWLModel(o, classname, parentclassname);
			}
			int scPropertySize = classpropertynumber;
			int scIndividualSize = classindividualnumber;
			while (scIndividualSize-- > 0) {
				String individualname = sc.get(scClassSize).ontObjectIndividual.get(scIndividualSize).ontObjectIndividualName;
				System.out.println("实例名称为->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + individualname);
				tryToAddObjectIndividualIntoOWLModel(o, classname, individualname);
			}
			while (scPropertySize-- > 0) {
				// 尝试将类的属性添加进模型
				String propertyname = sc.get(scClassSize).ontObjectProperty.get(scPropertySize).ontObjectPropertyName;
				String propertyvalue = sc.get(scClassSize).ontObjectProperty.get(scPropertySize).ontObjectPropertyValue;
				tryToAddObjectPropertyIntoOWLModel(o, propertyname);
				attachPropertyTOClassOrIndividual(o, classname, propertyname, propertyvalue);
			}
		}
		owlModel = o;
		writetoFile(writetoFileName, owlModel);
		return owlModel;
	}

	/*
	 * 关于添加类的操作
	 */
	public static void tryToAddClassIntoOWLModel(OWLModel o, String s) {
		try {
			if (!checkClass(o, s)) {
				System.out.println("无此类" + s);
				operatorforClass(o, s);
			} else {
				System.out.println("有此类" + s);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@SuppressWarnings("rawtypes")
	public static boolean checkClass(OWLModel o, String s) {
		boolean flag = false;
		Collection classes = o.getUserDefinedOWLNamedClasses();
		for (Iterator it = classes.iterator(); it.hasNext();) {
			OWLNamedClass cls = (OWLNamedClass) it.next();
			System.out.println("^^^^^^^^^^^^^" + cls.getBrowserText());
			if (cls.getBrowserText().equals(s)) {
				flag = true;
				break;
			}
		}
		Collection classes2 = o.getUserDefinedRDFSNamedClasses();
		for (Iterator it = classes2.iterator(); it.hasNext();) {
			OWLNamedClass cls = (OWLNamedClass) it.next();
			if (cls.getBrowserText().equals(s)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	@SuppressWarnings("unused")
	public static void operatorforClass(OWLModel o, String s) {
		OWLNamedClass tempclass;
		if (!checkIfClassIsAnIndividual(o, s)) {
			if (!checkClass(o, s)) {
				if (!checkIndividual(o, s)) {
					System.out.println("start to add class" + s);
					tempclass = o.createOWLNamedClass(s);
				}
			}
		}
	}

	/*
	 * 添加子类
	 */
	private static void tryToAddSubclassIntoOWLModel(OWLModel o, String subclassName, String parentClassName) {
		if (checkClass(o, parentClassName) & !checkIndividual(o, subclassName)) {
			System.out.println("有此 父类");
			operatorforSubclass(o, subclassName, parentClassName);
		} else {
			System.out.println("无此父类");
		}
	}

	private static void operatorforSubclass(OWLModel o, String subclassName, String parentClassName) {
		o.getOWLNamedClass(subclassName).addSuperclass(o.getOWLNamedClass(parentClassName));
		o.getOWLNamedClass(subclassName).removeSuperclass(o.getOWLThingClass());
	}

	/*
	 * 判断一个类是否为个体
	 */
	private static boolean checkIfClassIsAnIndividual(OWLModel o, String classname) {
		boolean flag = checkIndividual(o, classname);
		return flag;
	}

	@SuppressWarnings("rawtypes")
	private static boolean checkIndividual(OWLModel o, String classname) {
		boolean flag = false;
		Collection classes = o.getOWLIndividuals();
		for (Iterator it = classes.iterator(); it.hasNext();) {
			OWLIndividual oi = (OWLIndividual) it.next();
			if (oi.getBrowserText().equals(classname)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	private static void tryToAddObjectIndividualIntoOWLModel(OWLModel o, String classname, String individualname) {
		tryToAddClassIntoOWLModel(o, classname);
		System.out.println("准备添加“" + individualname + "”这个个体");
		if (!checkClass(o, individualname)) {
			System.out.println("当前不存在以个体名“" + individualname + "”的这个类");
			if (!checkIfIndividualExistsInModel(o, individualname))
				if (!checkIfIndividualIsAClass(o, individualname))
					tryToAddIndividualIntoOWLNamedClass(o, classname, individualname);
		}

	}

	private static boolean checkIfIndividualIsAClass(OWLModel o, String individualname) {
		// 若最終返回true説明
		// 检查这个个体是否是一个类，若是，返回true
		boolean flag = checkClass(o, individualname);
		// 检查个体是否存在于这个本体，若存在返回true
		flag = checkIfIndividualExistsInModel(o, individualname);
		return flag;
	}

	@SuppressWarnings("unused")
	private static void tryToAddIndividualIntoOWLNamedClass(OWLModel o, String classname, String individualname) {
		OWLIndividual oi = o.getOWLNamedClass(classname).createOWLIndividual(individualname);
	}

	@SuppressWarnings("rawtypes")
	private static boolean checkIfIndividualExistsInModel(OWLModel o, String individualname) {

		boolean flag = false;

		Collection classes = o.getUserDefinedOWLNamedClasses();
		for (Iterator it = classes.iterator(); it.hasNext();) {
			OWLNamedClass cls = (OWLNamedClass) it.next();
			Collection instances = cls.getInstances(true);
			for (Iterator it2 = instances.iterator(); it2.hasNext();) {
				OWLIndividual oi = (OWLIndividual) it2.next();
				if (oi.getBrowserText().equals(individualname)) {
					flag = true;
					break;

				}
			}
		}
		return flag;
	}

	/*
	 * 添加属性的操作
	 */
	private static void tryToAddObjectPropertyIntoOWLModel(OWLModel o, String s) {
		if (!checkProperty(o, s)) {
			if (!checkClass(o, s)) {
				System.out.println("无此 属性");
				operatorforProperty(o, s);
			}
		} else {
			System.out.println("有此属性");
		}
	}

	@SuppressWarnings("rawtypes")
	private static boolean checkProperty(OWLModel o, String s) {
		boolean flag = false;
		Collection c1 = o.getUserDefinedOWLDatatypeProperties();
		for (Iterator it = c1.iterator(); it.hasNext();) {
			try {
				OWLObjectProperty oop = (OWLObjectProperty) it.next();
				if (oop.getBrowserText().equals(s)) {
					flag = true;
					break;
				}
			} catch (Exception e) {
			}
		}

		Collection c2 = o.getUserDefinedRDFProperties();
		for (Iterator it = c2.iterator(); it.hasNext();) {
			try {
				OWLObjectProperty oop = (OWLObjectProperty) it.next();
				if (oop.getBrowserText().equals(s)) {
					flag = true;
					break;
				}
			} catch (Exception e) {
			}
		}
		Collection c3 = o.getUserDefinedOWLObjectProperties();
		for (Iterator it = c3.iterator(); it.hasNext();) {
			try {
				OWLObjectProperty oop = (OWLObjectProperty) it.next();
				if (oop.getBrowserText().equals(s)) {
					flag = true;
					break;
				}
			} catch (Exception e) {
			}
		}
		return flag;
	}

	private static void operatorforProperty(OWLModel o, String s) {
		System.out.println("start to add");
		@SuppressWarnings("unused")
		OWLObjectProperty oop = o.createOWLObjectProperty(s);
	}

	// public static boolean checkIndividualInClass(OWLModel o, String
	// classname, String individualname) {
	// boolean flag = false;
	// Collection classes = o.getUserDefinedOWLNamedClasses();
	// for (Iterator it = classes.iterator(); it.hasNext();) {
	// OWLNamedClass cls = (OWLNamedClass) it.next();
	// if (cls.getBrowserText().equals(classname)) {
	// Collection instances = cls.getInstances(true);
	// for (Iterator it2 = instances.iterator(); it2.hasNext();) {
	// OWLIndividual oi = (OWLIndividual) it2.next();
	// if (oi.getBrowserText().equals(individualname)) {
	// flag = true;
	// break;
	// }
	// }
	// }
	// }
	// return flag;
	// }

	private static void attachPropertyTOClassOrIndividual(OWLModel o, String classname, String propertyname, String propertyvalue) {
		tryToAddObjectPropertyIntoOWLModel(o, propertyname);
		tryToAddClassIntoOWLModel(o, classname);
		System.out.println("添加属性成功");
		OWLObjectProperty oop = o.getOWLObjectProperty(propertyname);

		if (checkClass(o, classname)) {
			System.out.println("##########################类名存在");
			oop.addUnionDomainClass(o.getOWLNamedClass(classname));
		}
		if (checkIndividual(o, classname)) {
			System.out.println("##########################个体名存在");
			o.getOWLIndividual(classname).setPropertyValue(oop, propertyvalue);
		}
	}
}