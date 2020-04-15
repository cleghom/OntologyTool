package translateStructclassToOWL;

import ontMainUtils.HanLPMain;
import ontMainUtils.object;
import ontMainUtils.objectPredicate;
import ontMainUtils.objectProperty;
import ioOperation.WriteToFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter.OWLModelWriter;


import staticVariable.staticvalue;
/*
 * 本方法主要使用了smi的protege3.5的相关jar包
 */
public class TranslateStructclassToOWLMain {
	/**
	 * @param args
	 * @throws OntologyLoadException
	 * @throws IOException
	 */

	public static void main(String[] args) throws OntologyLoadException, IOException {
//		String s = "植物是生物。人是智慧生物。人是劳动工具。智慧生物是生物。劳动工具是工具。人是动物，动物是生物。刘晓涛是一个中国人。德国是一个政权组织。Eclipse是一个代码工具，代码工具是工具中国人是人。美国人是人。奥巴马是一个美国人。德国是一个国家。奥巴马是一个美国人。";
//		String ss = "小刘的昵称是刘二，小刘是一个厨师，刘大伟的昵称是伟伟。刘大伟是一个厨师。小王的性别是男，小王的年龄是27.小王的父亲是老王，小王的母亲是张丽。张丽是一个教师。老王是一个工人。小王是一个厨师，小王是一个裁缝。植物是生物。人是智慧生物。人是劳动工具。智慧生物是生物。刘涛的电话号码是13866655333。赵航的电话号码是14787806414。赵航的学生证封面颜色是红的。赵航是一个中国人。";
//		String sss = "鸡是鸣禽，鸡是鸟。鸣禽是鸟。艾滋病的存活率是低的。鹅是鸟。癌症的致死率是高的。癌症的潜伏期是5年的。癌症是一个疾病。艾滋病的潜伏期是12年。艾滋病的感染性是强的。艾滋病的疾病传播方式是性传播。毛毛是一个麻雀。鸟的翅膀个数是2，大雁是鸟。牛牛的年龄是10.牛牛是一个大雁，麻雀的翅膀个数是2，麻雀是鸟。";
//		String ssss = "Eclipse的性能是ok的。感冒的潜伏期是2天。感冒是一个疾病。植物是生物。鹅是鸟。大雁是鸟。鹊雁是鸟。黑长尾雉是台湾的野生鸟类。大雁的羽毛颜色是白色的。鹅的羽毛颜色是白色的.小美洲黑雁头颈部呈黑色。燕隼属隼形目隼科隼属，燕隼上体是深蓝褐色的，燕隼是中国国家二级保护动物。四川灌木莺是新物种。绿翅雁是濒危鸟类。鸟有漂亮的翅膀。";
		mainfunction("生物吃食物。绿翅雁是濒危鸟类。鸟有漂亮的翅膀。鸡是鸣禽，鸡是鸟。鸣禽是鸟。麻雀是鸟。啄木鸟是鸟。微生物是生物。乌鸦是鸟。动物是生物。鸟是动物。鹊雁是鸟。鸟吃昆虫。蚂蚱是昆虫。", "base");
	}

	public static void writetotempFile(OWLModel o) throws IOException{
		String localaddr = staticvalue.localaddr;
		String filePathOut01 = localaddr + "/" + "tempowl.owl";
		FileOutputStream outFile = new FileOutputStream(filePathOut01);
		Writer out = new OutputStreamWriter(outFile, "UTF-8");
		OWLModelWriter omw = new OWLModelWriter(o, o.getTripleStoreModel().getActiveTripleStore(), out);
		omw.write();
		out.close();
	}

	public static boolean class_exist(OWLModel o, String class_) {
		boolean flag = false;
		OWLNamedClass on = o.getOWLNamedClass(class_);
		if (on == null)
			flag = false;
		else
			flag = true;
		return flag;
	}

	public static boolean individual_exist(OWLModel o, String individual_) {
		boolean flag = false;
		if(!class_exist(o, individual_)){
			OWLIndividual on = o.getOWLIndividual(individual_);
			if (on == null)
				flag = false;
			else
				flag = true;
		}
		return flag;
	}

	public static boolean property_exist(OWLModel o,String prop){
		boolean flag = false;
		OWLProperty op = o.getOWLProperty(prop);
		if (op == null){
			flag = false;
		}
		else
			flag = true;

		return flag;
	}

	public static boolean c_i_conflict(OWLModel o ,String instance){// new instance is conflicted with the class existing in ont
		boolean flag = false;
		OWLNamedClass on = o.getOWLNamedClass(instance);
		if (on == null)
			flag = false;
		else
			flag = true;
		return flag;
	}

	public static boolean i_c_conflict(OWLModel o ,String cls){// new class is conflicted with the instance existing in ont
		boolean flag = false;
		if (!class_exist(o, cls)){
			OWLIndividual on = o.getOWLIndividual(cls);
			if (on == null)
				flag = false;
			else
				flag = true;
		}
		return flag;
	}

	public static void donothing(){}

	public static OWLModel mainfunction(OWLModel o, String string) throws IOException {
		/*
		 * 输入本体，另一部分为新的文本，在原来的model基础上继续添加新的本体
		 * 二次建立本体
		 */
		ArrayList<object> _objlist = new ArrayList<object>();
		HanLPMain.mainfunction(string);// 处理后的句子生成的结构体类赋值给sc
		_objlist = HanLPMain.getStructclass();

		for (object _o : _objlist) {
			int object_type = _o.objectType;
			String object_name = _o.objectName;
			ArrayList<object> object_parent = _o.parentObject;
			ArrayList<object> object_sub = _o.subObject;
			ArrayList<objectProperty> object_property = _o.objectproperty;
			ArrayList<objectPredicate> object_predicate = _o.objectpredicate;

			boolean hasproperty = false;
			boolean haspredicate = false;
			if (object_property != null) {// if property exists in this ont
				hasproperty = true;
				for (objectProperty _op : object_property)
					if (!property_exist(o, _op.propertyName))
						o.createOWLObjectProperty(_op.propertyName);
			}

			if(object_predicate != null){//添加非关系型属性列表
				haspredicate = true;
				for(objectPredicate _opre:object_predicate)
					if (!property_exist(o, _opre.objectPredicateVerb))
						o.createOWLObjectProperty(_opre.objectPredicateVerb);
			}

			if (object_type == 0) {// if object is a class
				if (!class_exist(o, object_name)) {// if class does't exists
					o.createOWLNamedClass(object_name);// create this class
					if (object_parent != null) {// if parent classes exist
						for (object oparent : object_parent) {// add this class's parent classes
							if (!class_exist(o, oparent.objectName)) {// if parent classes don't exist
								o.createOWLNamedClass(oparent.objectName);// add parent classes
							}
							if (!(oparent.objectName).equals("root")) {
								o.getOWLNamedClass(object_name).addSuperclass(o.getOWLNamedClass(oparent.objectName));
								o.getOWLNamedClass(object_name).removeSuperclass(o.getOWLThingClass());
							}
						}
					}
					if (hasproperty) {// if this class has properties
						for (objectProperty oprop : object_property) {// add properties
							OWLObjectProperty oop = o.getOWLObjectProperty(oprop.propertyName);
							o.getOWLNamedClass(object_name).setPropertyValue(oop, oprop.propertyValue);
						}
					}
					if(haspredicate){
						for(objectPredicate opre:object_predicate){
							OWLObjectProperty oopre = o.getOWLObjectProperty(opre.objectPredicateVerb);//获取非关系型属性谓词
							oopre.addUnionDomainClass(o.getOWLNamedClass(object_name));
							if(!class_exist(o, opre.objectPredicateObject))
								o.createOWLNamedClass(opre.objectPredicateObject);
							oopre.addUnionRangeClass(o.getOWLNamedClass(opre.objectPredicateObject));
						}
					}
					if (object_sub != null) {// if subclasses exist
						for (object osub : object_sub) {// add sub classes
							if (osub.objectType == 0) {// sub is class
								if (!class_exist(o, osub.objectName)) {// if this class not exist
									o.createOWLNamedClass(osub.objectName); // create this class
								}
								o.getOWLNamedClass(osub.objectName).addSuperclass(o.getOWLNamedClass(object_name));
							}
							if (osub.objectType == 1) {// sub is anindividual
								if (!individual_exist(o, osub.objectName) & !class_exist(o, osub.objectName)) {
									o.getOWLNamedClass(object_name).createOWLIndividual(osub.objectName);
								}
							}
						}
					}
				} else if (class_exist(o, object_name)) {// if class exists
					if (i_c_conflict(o, object_name)) {// if this class does't have a instance-class conflict
						donothing();
					} else if (!i_c_conflict(o, object_name)) {// if this class doesn't have a instance-class conflict
						if (object_parent != null) {
							for (object oparent : object_parent) {// add this class's parent classes
								if (!class_exist(o, oparent.objectName)) {// if parent classes don't exist
									o.createOWLNamedClass(oparent.objectName);// add parent classes
								}
								if (!(oparent.objectName).equals("root")) {
									o.getOWLNamedClass(object_name).addSuperclass(o.getOWLNamedClass(oparent.objectName));
									o.getOWLNamedClass(object_name).removeSuperclass(o.getOWLThingClass());
								}
							}
						}
						if (hasproperty) {// if this class has properties
							for (objectProperty oprop : object_property) {// add properties
								OWLObjectProperty oop = o.getOWLObjectProperty(oprop.propertyName);
								o.getOWLNamedClass(object_name).setPropertyValue(oop, oprop.propertyValue);
							}
						}
						if(haspredicate){
							for(objectPredicate opre:object_predicate){
								OWLObjectProperty oopre = o.getOWLObjectProperty(opre.objectPredicateVerb);//获取非关系型属性谓词
								oopre.addUnionDomainClass(o.getOWLNamedClass(object_name));
								if(!class_exist(o, opre.objectPredicateObject))
									o.createOWLNamedClass(opre.objectPredicateObject);
								oopre.addUnionRangeClass(o.getOWLNamedClass(opre.objectPredicateObject));
							}
						}
						if (object_sub != null) {// if subclasses exist
							for (object osub : object_sub) {// add sub classes
								if (osub.objectType == 0) {// sub is class
									if (!class_exist(o, osub.objectName)) {// if this class not exist
										o.createOWLNamedClass(osub.objectName); // create this class
									}
									o.getOWLNamedClass(osub.objectName).addSuperclass(o.getOWLNamedClass(object_name));
								}
								if (osub.objectType == 1) {// sub is anindividual
									if (!individual_exist(o, osub.objectName) & !class_exist(o, osub.objectName)) {
										o.getOWLNamedClass(object_name).createOWLIndividual(osub.objectName);
									}
								}
							}
						}
					}
				}
			}
			if (object_type == 1) {// if object is an instance
				if (individual_exist(o, object_name)) {// if this instance exists in ont'
					System.out.println("instance exists.");
					if (c_i_conflict(o, object_name)) {// if this instance has a class-instance conflict
						@SuppressWarnings("unchecked")
						Collection<OWLNamedClass> onc = o.getOWLNamedClass(object_name).getNamedSuperclasses();
						for (OWLNamedClass _onc : onc) {
							_onc.createOWLIndividual(object_name);
						}
						@SuppressWarnings({ "rawtypes", "unused" })
						Collection op = o.getOWLNamedClass(object_name).getRDFProperties();

						o.getOWLNamedClass(object_name).delete();
						if (object_parent != null) {
							for (object oparent : object_parent) {// add this class's parent classes
								if (!class_exist(o, oparent.objectName)) {// if parent classes don't exist
									o.createOWLNamedClass(oparent.objectName);// add parent classes
								}
								o.getOWLNamedClass(object_name).addSuperclass(o.getOWLNamedClass(oparent.objectName));
							}
						}
						if (hasproperty) {// if this class has properties
							for (objectProperty oprop : object_property) {// add properties
								OWLObjectProperty oop = o.getOWLObjectProperty(oprop.propertyName);
								o.getOWLIndividual(object_name).setPropertyValue(oop, oprop.propertyValue);
							}
						}
					}
				} else if (!individual_exist(o, object_name)) {
					if (object_parent != null) {
						for (object oparent : object_parent) {// add this class's parent classes
							if (!class_exist(o, oparent.objectName)) {// if parent classes don't exist
								o.createOWLNamedClass(oparent.objectName);// add parent classes
							}
							// o.getOWLNamedClass(object_name).addSuperclass(o.getOWLNamedClass(oparent.objectName));
							o.getOWLNamedClass(oparent.objectName).createOWLIndividual(object_name);
						}
					}
					if (hasproperty) {// if this class has properties
						for (objectProperty oprop : object_property) {// add properties
							OWLObjectProperty oop = o.getOWLObjectProperty(oprop.propertyName);
							o.getOWLIndividual(object_name).setPropertyValue(oop, oprop.propertyValue);
						}
					}
				}
			}
		}
		Format format = new SimpleDateFormat("yyMMdd-hhmmss");
		String time = format.format(new Date());
		staticvalue.tempfilename = time;
		// writetoFile(o);
		WriteToFile.writetoFile(o);
		return o;
	}

	public static OWLModel mainfunction(String string, String Resource) throws OntologyLoadException, IOException {
		/*
		 * 首次建立 string:文本内容 Resource:本体的根节点内容（文本词汇形式）
		 */
		OWLModel o = ProtegeOWL.createJenaOWLModel();
		o.getNamespaceManager().setDefaultNamespace("http://" + Resource + "#");

		ArrayList<object> _objlist = new ArrayList<object>();
		HanLPMain.mainfunction(string);// 处理后的句子生成的结构体类赋值给sc
		_objlist = HanLPMain.getStructclass();

		for (object _o : _objlist) {
			int object_type = _o.objectType;
			String object_name = _o.objectName;
			ArrayList<object> object_parent = _o.parentObject;
			ArrayList<object> object_sub = _o.subObject;
			ArrayList<objectProperty> object_property = _o.objectproperty;
			ArrayList<objectPredicate> object_predicate = _o.objectpredicate;

			boolean hasproperty = false;
			boolean haspredicate = false;
			if (object_property != null) {// if property exists in this ont
				hasproperty = true;
				for (objectProperty _op : object_property)
					if (!property_exist(o, _op.propertyName))
						o.createOWLObjectProperty(_op.propertyName);
			}
			if(object_predicate != null){//添加非关系型属性列表
				haspredicate = true;
				for(objectPredicate _opre:object_predicate)
					if (!property_exist(o, _opre.objectPredicateVerb))
						o.createOWLObjectProperty(_opre.objectPredicateVerb);
			}
			if (object_type == 0) {// if object is a class
				if (!class_exist(o, object_name)) {// if class does't exists
					o.createOWLNamedClass(object_name);// create this class
					if (object_parent != null) {// if parent classes exist
						for (object oparent : object_parent) {// add this class's parent classes
							if (!class_exist(o, oparent.objectName)) {// if parent classes don't exist
								o.createOWLNamedClass(oparent.objectName);// add parent classes
							}
							if (!(oparent.objectName).equals("root")) {
								o.getOWLNamedClass(object_name).addSuperclass(o.getOWLNamedClass(oparent.objectName));
								o.getOWLNamedClass(object_name).removeSuperclass(o.getOWLThingClass());
							}
						}
					}
					if (hasproperty) {// if this class has properties
						for (objectProperty oprop : object_property) {// add
							// properties
							OWLObjectProperty oop = o.getOWLObjectProperty(oprop.propertyName);
							o.getOWLNamedClass(object_name).setPropertyValue(oop, oprop.propertyValue);
						}
					}
					if(haspredicate){
						for(objectPredicate opre:object_predicate){
							OWLObjectProperty oopre = o.getOWLObjectProperty(opre.objectPredicateVerb);//获取非关系型属性谓词
							oopre.addUnionDomainClass(o.getOWLNamedClass(object_name));
							if(!class_exist(o, opre.objectPredicateObject))
								o.createOWLNamedClass(opre.objectPredicateObject);
							oopre.addUnionRangeClass(o.getOWLNamedClass(opre.objectPredicateObject));
						}
					}
					if (object_sub != null) {// if subclasses exist
						for (object osub : object_sub) {// add sub classes
							if (osub.objectType == 0) {// sub is class
								if (!class_exist(o, osub.objectName)) {// if this class not exist
									o.createOWLNamedClass(osub.objectName); // create this class
								}
								o.getOWLNamedClass(osub.objectName).addSuperclass(o.getOWLNamedClass(object_name));
							}
							if (osub.objectType == 1) {// sub is anindividual
								if (!individual_exist(o, osub.objectName) & !class_exist(o, osub.objectName)) {
									o.getOWLNamedClass(object_name).createOWLIndividual(osub.objectName);
								}
							}
						}
					}
				} else if (class_exist(o, object_name)) {// if class exists
					if (i_c_conflict(o, object_name)) {// if this class does't have a instance-class conflict
						donothing();
					} else if (!i_c_conflict(o, object_name)) {// if this class doesn't have a instance-class conflict
						if (object_parent != null) {
							for (object oparent : object_parent) {// add this class's parent classes
								if (!class_exist(o, oparent.objectName)) {// if parent classes don't exist
									o.createOWLNamedClass(oparent.objectName);// add parent classes
								}
								if (!(oparent.objectName).equals("root")) {
									o.getOWLNamedClass(object_name).addSuperclass(o.getOWLNamedClass(oparent.objectName));
									o.getOWLNamedClass(object_name).removeSuperclass(o.getOWLThingClass());
								}
							}
						}
						if (hasproperty) {// if this class has properties
							for (objectProperty oprop : object_property) {// add properties
								OWLObjectProperty oop = o.getOWLObjectProperty(oprop.propertyName);
								o.getOWLNamedClass(object_name).setPropertyValue(oop, oprop.propertyValue);
							}
						}
						if(haspredicate){
							for(objectPredicate opre:object_predicate){
								OWLObjectProperty oopre = o.getOWLObjectProperty(opre.objectPredicateVerb);//获取非关系型属性谓词
								oopre.addUnionDomainClass(o.getOWLNamedClass(object_name));
								if(!class_exist(o, opre.objectPredicateObject))
									o.createOWLNamedClass(opre.objectPredicateObject);
								oopre.addUnionRangeClass(o.getOWLNamedClass(opre.objectPredicateObject));
							}
						}
						if (object_sub != null) {// if subclasses exist
							for (object osub : object_sub) {// add sub classes
								if (osub.objectType == 0) {// sub is class
									if (!class_exist(o, osub.objectName)) {// if this class not exist
										o.createOWLNamedClass(osub.objectName); // create this class
									}
									o.getOWLNamedClass(osub.objectName).addSuperclass(o.getOWLNamedClass(object_name));
								}
								if (osub.objectType == 1) {// sub is anindividual
									if (!individual_exist(o, osub.objectName) & !class_exist(o, osub.objectName)) {
										o.getOWLNamedClass(object_name).createOWLIndividual(osub.objectName);
									}
								}
							}
						}
					}
				}
			}
			if (object_type == 1) {// if object is an instance
				if (individual_exist(o, object_name)) {// if this instance exists in ont'
					System.out.println("instance exists.");
					if (c_i_conflict(o, object_name)) {// if this instance has a class-instance conflict
						@SuppressWarnings("unchecked")
						Collection<OWLNamedClass> onc = o.getOWLNamedClass(object_name).getNamedSuperclasses();
						for (OWLNamedClass _onc : onc) {
							_onc.createOWLIndividual(object_name);
						}
						@SuppressWarnings("rawtypes")
						Collection op = o.getOWLNamedClass(object_name).getRDFProperties();
						System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"+op);

						o.getOWLNamedClass(object_name).delete();
						if (object_parent != null) {
							for (object oparent : object_parent) {// add this class's parent classes
								if (!class_exist(o, oparent.objectName)) {// if parent classes don't exist
									o.createOWLNamedClass(oparent.objectName);// add parent classes
								}
								o.getOWLNamedClass(object_name).addSuperclass(o.getOWLNamedClass(oparent.objectName));
							}
						}
						if (hasproperty) {// if this class has properties
							for (objectProperty oprop : object_property) {// add properties
								OWLObjectProperty oop = o.getOWLObjectProperty(oprop.propertyName);
								o.getOWLIndividual(object_name).setPropertyValue(oop, oprop.propertyValue);
							}
						}
					}
				} else if (!individual_exist(o, object_name)) {
					if (object_parent != null) {
						for (object oparent : object_parent) {// add this class's parent classes
							if (!class_exist(o, oparent.objectName)) {// if parent classes don't exist
								o.createOWLNamedClass(oparent.objectName);// add parent classes
							}
							// o.getOWLNamedClass(object_name).addSuperclass(o.getOWLNamedClass(oparent.objectName));
							o.getOWLNamedClass(oparent.objectName).createOWLIndividual(object_name);
						}
					}
					if (hasproperty) {// if this class has properties
						for (objectProperty oprop : object_property) {// add properties
							OWLObjectProperty oop = o.getOWLObjectProperty(oprop.propertyName);
							o.getOWLIndividual(object_name).setPropertyValue(oop, oprop.propertyValue);
						}
					}
				}
			}
		}
		Format format = new SimpleDateFormat("yyMMdd-hhmmss");
		String time = format.format(new Date());
		staticvalue.tempfilename = time;
		// writetoFile(o);
		WriteToFile.writetoFile(o);
		return o;
	}
}