package ontMainUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;

public class HanLPMain {

	static ArrayList<object> objectlist = new ArrayList<object>();//
	static List<Term> mainTermList = new ArrayList<Term>();
	static String inf = "";

	public static ArrayList<object> getStructclass() {
		return objectlist;
	}

	public static String getinf() {
		return inf;
	}

	// 主方法实现从字符串和文件中读入文本，连续即可。
	public static void main(String[] args) {
		// mainfunction("鸟有两个翅膀，中华人民共和国是国家，麻雀是鸟，鸟有漂亮的翅膀。鸟有强壮的翅膀。麻雀是鸟。鸟的翅膀是毛绒绒的。百度的url是www-baidu-com.南海的岛屿是中国的。南海的边境线是12海里。");
		mainfunction("赵航是一个中国人。黄海是海。东海是海。南海是海。亚洲是洲。非洲是洲。鸟有漂亮的翅膀。鸟有强壮的翅膀。麻雀吃昆虫。");
	}

	// 主方法调用的读字符串分析
	public static ArrayList<object> mainfunction(String string) {
		ArrayList<String> separateList = separateString(string);
		for (String s : separateList) {
			mainfunctionforstring(s);
		}
		return objectlist;
	}

	// 主方法调用的读文件分析
	public static ArrayList<object> mainfunction(File file) {
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String aline = "";
			try {
				while ((aline = br.readLine()) != null) {
					ArrayList<String> separateList = separateString(aline);
					for (String s : separateList) {
						analyseEachString(s);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return objectlist;
	}

	public static void mainfunctionforstring(String string) {
		string += ".";
		analyseEachString(string);
	}

	/*
	 * 将原来句子转化为一个String类型的List
	 */
	public static ArrayList<String> separateString(String s) {

		List<Term> newString = HanLP.segment(s);
		ArrayList<String> list = new ArrayList<String>();
		int termListSize = newString.size();
		int num = 0;
		String temp = "";
		while (num < termListSize) {
			String termTag = newString.get(num).nature.toString();
			String terErem = newString.get(num).word;
			temp += terErem;
			if (termTag.equals("w")) {
				list.add(temp);
				temp = "";
			}
			num++;
		}
		return list;
	}

	public static List<Term> getBaseTermOfthisSentence(String s) {
		List<Term> termList = new ArrayList<Term>();// 初始的每个句子的Term的List
		termList = HanLP.segment(s);// 该句子的List赋值
		return termList;
	}

	public static List<Term> getAdvancedTermOfthisSentencs(List<Term> term) {
		List<Term> termList = new ArrayList<Term>();// 初始的每个句子的Term的List
		termList = getMainTermList(term);
		System.out.println("规范后结果 :\t" + termList);
		return termList;
	}

	/*
	 * 分析单个句子成分，以String为参数
	 */
	public static void analyseEachString(String s) {
		mainTermList = getAdvancedTermOfthisSentencs(getBaseTermOfthisSentence(s));
		getObject(mainTermList, "");
		mainTermList = null;
		System.out.println("ooooooooooooooooooooooooooooooooooooooooooo\n" + inf);
	}

	public static List<Term> getMainTermList(List<Term> list) {
		// 迭代执行简化操作
		List<Term> temp = new ArrayList<Term>();
		temp = simplyTermList(list);
		return temp;
	}

	public static List<Term> simplyTermList(List<Term> list) {
		System.out.println("分析->\t" + list);
		List<Term> termList = new ArrayList<Term>();
		termList = list;
		int termListSize = list.size();
		for (int t = 0; t < termListSize - 1; t++) {
			// 分类实现化简
			// 化简名词
			String x1 = getParent(termList.get(t).nature.toString());
			String x2 = getParent(termList.get(t + 1).nature.toString());
			// System.out.println(x1 + "------------" + x2);
			if (x1.equals("n") && x2.equals("n")) {
				String n1 = termList.get(t).word;
				String n2 = termList.get(t + 1).word;
				String combineword = n1 + n2;
				termList.get(t).word = combineword;
				termList.get(t).nature = Nature.n;
				termList.remove(t + 1);
				break;
			}
			// 化简
		}
		if (termListSize != termList.size())
			simplyTermList(termList);
		return termList;
	}

	// 获取某些元素，为构建OWL准备
	public static ArrayList<object> getObject(List<Term> list, String getbackinfo) {
		object root = new object();
		root.objectName = "root";
		root.objectType = 0;
		root.objectType = 0;
		root.parentObject = null;
		root.subObject = null;

		int termListSize = list.size();
		ArrayList<object> thisstructclass = new ArrayList<object>();
		List<Term> termList = new ArrayList<Term>();
		termList = list;
		// A的B（非“是”）
		for (int t = 0; t < termListSize - 2; t++) {
			if (getParent(termList.get(t).nature.toString()).equals("n") && getParent(termList.get(t + 1).nature.toString()).equals("ude1")
					&& getParent(termList.get(t + 2).nature.toString()).equals("n")
					&& !getParent(termList.get(t + 3).nature.toString()).equals("vshi")) {
				object _o = new object();
				_o.objectName = termList.get(t).word;
				_o.parentObject = null;
				objectlist.add(_o);
				thisstructclass.add(_o);
				System.out.println("A的B非“是”");
			}
		}

		// （非“是”）A的B
		for (int t = 1; t < termListSize - 1; t++) {
			if (!getParent(termList.get(t - 1).nature.toString()).equals("vshi") && getParent(termList.get(t).nature.toString()).equals("n")
					&& getParent(termList.get(t + 1).nature.toString()).equals("ude1")
					&& getParent(termList.get(t + 2).nature.toString()).equals("n")) {
				object _o = new object();
				_o.objectName = termList.get(t).word;
				_o.parentObject = null;
				objectlist.add(_o);
				thisstructclass.add(_o);
				System.out.println("“是”A的B");
			}
		}
		// （非“的”）A是B
		for (int t = 1; t < termListSize - 2; t++) {
			if (!getParent(termList.get(t - 1).nature.toString()).equals("ude1") && getParent(termList.get(t).nature.toString()).equals("n")
					&& getParent(termList.get(t + 1).nature.toString()).equals("vshi")
					&& getParent(termList.get(t + 2).nature.toString()).equals("n")) {
				object scn1 = new object();
				object scn2 = new object();
				scn2.parentObject.add(root);
				scn2.objectName = termList.get(t + 2).word;
				scn1.parentObject.add(scn2);
				scn1.objectName = termList.get(t).word;
				objectlist.add(scn1);
				objectlist.add(scn2);
				thisstructclass.add(scn1);
				thisstructclass.add(scn2);
				System.out.println("“的”A是B");
			}
		}
		// 判断首句是“A是B”
		for (int t = 0; t < 1; t++) {
			if (termList.size() >= 3) {
				if (getParent(termList.get(0).nature.toString()).equals("n") && getParent(termList.get(1).nature.toString()).equals("vshi")
						&& getParent(termList.get(2).nature.toString()).equals("n") && !getParent(termList.get(3).nature.toString()).equals("ude1")) {
					object scn1 = new object();
					object scn2 = new object();
					scn2.parentObject.add(root);
					scn2.objectName = termList.get(2).word;
					scn1.parentObject.add(scn2);
					scn1.objectName = termList.get(0).word;
					scn2.subObject.add(scn1);
					objectlist.add(scn1);
					objectlist.add(scn2);
					thisstructclass.add(scn1);
					thisstructclass.add(scn2);
					System.out.println("A是B");
				}
			}
		}
		// A的B是C（n）
		for (int t = 0; t < termListSize - 4; t++) {
			if (getParent(termList.get(t).nature.toString()).equals("n") && getParent(termList.get(t + 1).nature.toString()).equals("ude1")
					&& getParent(termList.get(t + 2).nature.toString()).equals("n")
					&& getParent(termList.get(t + 3).nature.toString()).equals("vshi")
					&& getParent(termList.get(t + 4).nature.toString()).equals("n")
					&& getParent(termList.get(t + 5).nature.toString()).equals("ude1")) {
				object _o = new object();
				objectProperty scp = new objectProperty();
				_o.parentObject.add(root);
				_o.objectName = termList.get(t).word;
				scp.propertyName = termList.get(t + 2).word;
				scp.propertyValue = termList.get(t + 4).word + "的";
				_o.objectproperty.add(scp);
				objectlist.add(_o);
				thisstructclass.add(_o);
				System.out.println("A的B是n");
			}
		}
		// A的B是C（adj）
		for (int t = 0; t < termListSize - 4; t++) {
			if (getParent(termList.get(t).nature.toString()).equals("n") && getParent(termList.get(t + 1).nature.toString()).equals("ude1")
					&& getParent(termList.get(t + 2).nature.toString()).equals("n")
					&& getParent(termList.get(t + 3).nature.toString()).equals("vshi")
					&& getParent(termList.get(t + 4).nature.toString()).equals("a")
					&& getParent(termList.get(t + 5).nature.toString()).equals("ude1")) {
				object _o = new object();
				objectProperty scp = new objectProperty();
				_o.parentObject.add(root);
				_o.objectName = termList.get(t).word;
				scp.propertyName = termList.get(t + 2).word;
				scp.propertyValue = termList.get(t + 4).word + "的";
				_o.objectproperty.add(scp);
				objectlist.add(_o);
				thisstructclass.add(_o);
				System.out.println("A的B是C（adj）");
			}
		}
		// A是B的C
		for (int t = 0; t < termListSize - 4; t++) {
			if (getParent(termList.get(t).nature.toString()).equals("n") && getParent(termList.get(t + 1).nature.toString()).equals("vshi")
					&& getParent(termList.get(t + 2).nature.toString()).equals("n")
					&& getParent(termList.get(t + 3).nature.toString()).equals("ude1")
					&& getParent(termList.get(t + 4).nature.toString()).equals("n")) {
				object _o = new object();
				objectProperty scp = new objectProperty();
				_o.parentObject.add(root);
				_o.objectName = termList.get(t + 2).word;
				scp.propertyName = termList.get(t + 4).word;
				scp.propertyValue = termList.get(t).word;
				_o.objectproperty.add(scp);
				objectlist.add(_o);
				thisstructclass.add(_o);
				System.out.println("A是B的C");
			}
		}
		// A是一个B
		// 添加B的一个实例A
		for (int t = 0; t < termListSize - 4; t++) {
			if (getParent(termList.get(t).nature.toString()).equals("n") && getParent(termList.get(t + 1).nature.toString()).equals("vshi")
					&& getParent(termList.get(t + 2).nature.toString()).equals("mq") && getParent(termList.get(t + 3).nature.toString()).equals("n")) {
				String mpword = getParent(termList.get(t + 2).word);
				if (mpword.contains("一") || mpword.contains("1")) {
					object _o = new object();
					object sci = new object();
					_o.objectName = termList.get(t + 3).word;
					_o.parentObject.add(root);
					sci.objectType = 1;
					sci.objectName = termList.get(t).word;
					_o.subObject.add(sci);
					objectlist.add(_o);
					thisstructclass.add(_o);
					System.out.println("A是一个B");
				}
			}
		}

		// A有adj的B
		// A有n个B
		for (int t = 0; t < termListSize - 4; t++) {
			if (getParent(termList.get(t).nature.toString()).equals("n") && getParent(termList.get(t + 1).nature.toString()).equals("vyou")
					&& getParent(termList.get(t + 2).nature.toString()).equals("a")
					&& getParent(termList.get(t + 3).nature.toString()).equals("ude1")
					&& getParent(termList.get(t + 4).nature.toString()).equals("n")) {
				object _o = new object();
				objectProperty scp = new objectProperty();
				_o.parentObject.add(root);
				_o.objectName = termList.get(t).word;
				scp.propertyName = termList.get(t + 4).word;
				scp.propertyValue = termList.get(t + 2).word;
				_o.objectproperty.add(scp);
				objectlist.add(_o);
				thisstructclass.add(_o);
				System.out.println("A有adj的B");
//
//				_o.parentObject.add(root);
//				_o.objectName = termList.get(t).word;
//				objectpredicate op = new objectpredicate();
//				op.objectpredicateverb = termList.get(t + 1).word;
//				op.objectpredicateobject = termList.get(t + 4).word;
//				_o.objectpredicate.add(op);
//				objectlist.add(_o);
//				thisstructclass.add(_o);
//				System.out.println("A有B");
			}
		}

		// 主谓宾
		for (int t = 0; t < termListSize - 2; t++) {
			if (getParent(termList.get(t).nature.toString()).equals("n") && getParent(termList.get(t + 1).nature.toString()).equals("v")
					&& getParent(termList.get(t + 2).nature.toString()).equals("n")) {
				object _o = new object();
				_o.parentObject.add(root);
				_o.objectName = termList.get(t).word;
				objectPredicate op = new objectPredicate();
				op.objectPredicateVerb = termList.get(t + 1).word;
				op.objectPredicateObject = termList.get(t + 2).word;
				_o.objectpredicate.add(op);

				objectlist.add(_o);
				thisstructclass.add(_o);
				System.out.println("主谓宾");
			}
		}

		int x = thisstructclass.size();
		String parentobj = "";
		String subobj = "";
		String objproperty = "";
		String objectpredicate = "";

		while (x-- > 0) {
			// object
			String tempinfo = "------------------------------\n";
			String classname = thisstructclass.get(x).objectName;
			if (thisstructclass.get(x).parentObject != null)
				for (object parent : thisstructclass.get(x).parentObject) {
					String parentobjname = parent.objectName;
					if (!parentobjname.equals("root")) {
						parentobj += parentobjname + ";";
						tempinfo += "\t--->父类为" + parentobj + "\n";
					} else
						tempinfo += "\t--->父类为root\n";
				}

			tempinfo += "object:" + classname + "";

			// property
			if (thisstructclass.get(x).objectproperty.size() > 0)
				for (objectProperty property : thisstructclass.get(x).objectproperty) {
					String propertyName = property.propertyName.toString();
					String propertyValue = property.propertyValue.toString();
					objproperty += propertyName + ":" + propertyValue + ";";
					tempinfo += "--->属性" + objproperty + "\n";
				}
			else
				tempinfo += "--->无属性\n";

			// subObject
			if (thisstructclass.get(x).subObject.size() > 0)
				for (object sub : thisstructclass.get(x).subObject) {
					String subobjname = sub.objectName;
					if (!subobj.equals(null)) {
						subobj += subobjname + ";";
					}
					tempinfo += "\t--->子类为" + subobj;
					// "\n";
				}
			else
				tempinfo += "\t--->子类为空";

			// predicate relation
			if (thisstructclass.get(x).objectpredicate.size() > 0)
				for (objectPredicate op : thisstructclass.get(x).objectpredicate) {
					String predicateverb = op.objectPredicateVerb;
					String predicateobject = op.objectPredicateObject;
					tempinfo += "\n谓词：" + predicateverb + "宾语：" + predicateobject;
				}
			else
				tempinfo += "";

			System.out.println(tempinfo);
			System.out.println("**************************");
			getbackinfo += tempinfo + "\n";
			inf = tempinfo;
		}
		System.out.println("----------------------------华丽的分割线----------------------------");
		return thisstructclass;
	}

	/*
	 * 获取某个词的父类词性
	 */
	public static String getParent(String s) {
		String temp = "";
		temp = getWordParent.getParent.getparentAttr(s);
		if (temp == "no") {
			temp = s;
		}
		return temp;
	}

}