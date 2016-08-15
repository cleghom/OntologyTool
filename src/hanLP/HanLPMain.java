package hanLP;

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

	static ArrayList<_object> _objectlist = new ArrayList<_object>();//
	static List<Term> mainTermList = new ArrayList<Term>();
	static String inf = "";

	public static ArrayList<_object> getStructclass() {
		return _objectlist;
	}

	public static String getinf() {
		return inf;
	}

	// 主方法实现从字符串和文件中读入文本，连续即可。
	public static void main(String[] args) {
		// mainfunction("鸟有两个翅膀，中华人民共和国是国家，麻雀是鸟，鸟有漂亮的翅膀。鸟有强壮的翅膀。麻雀是鸟。鸟的翅膀是毛绒绒的。百度的url是www-baidu-com.南海的岛屿是中国的。南海的边境线是12海里。");
		mainfunction("赵航是一个中国人。黄海是海。东海是海。南海是海。亚洲是洲。非洲是洲。鸟有漂亮的翅膀。鸟有强壮的翅膀。");
	}

	// 主方法调用的读字符串分析
	public static ArrayList<_object> mainfunction(String string) {
		ArrayList<String> separateList = separateString(string);
		for (String s : separateList) {
			mainfunctionforstring(s);
		}
		return _objectlist;
	}

	// 主方法调用的读文件分析
	public static ArrayList<_object> mainfunction(File file) {
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
		return _objectlist;
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
	public static ArrayList<_object> getObject(List<Term> list, String getbackinfo) {
		_object root = new _object();
		root.objectname = "root";
		root.objecttype = 0;
		root.objecttype = 0;
		root.parent_object = null;
		root.sub_object = null;

		int termListSize = list.size();
		ArrayList<_object> thisstructclass = new ArrayList<_object>();
		List<Term> termList = new ArrayList<Term>();
		termList = list;
		// A的B（非“是”）
		for (int t = 0; t < termListSize - 2; t++) {
			if (getParent(termList.get(t).nature.toString()).equals("n") && getParent(termList.get(t + 1).nature.toString()).equals("ude1")
					&& getParent(termList.get(t + 2).nature.toString()).equals("n")
					&& !getParent(termList.get(t + 3).nature.toString()).equals("vshi")) {
				_object _o = new _object();
				_o.objectname = termList.get(t).word;
				_o.parent_object = null;
				_objectlist.add(_o);
				thisstructclass.add(_o);
				System.out.println("A的B非“是”");
			}
		}

		// （非“是”）A的B
		for (int t = 1; t < termListSize - 1; t++) {
			if (!getParent(termList.get(t - 1).nature.toString()).equals("vshi") && getParent(termList.get(t).nature.toString()).equals("n")
					&& getParent(termList.get(t + 1).nature.toString()).equals("ude1")
					&& getParent(termList.get(t + 2).nature.toString()).equals("n")) {
				_object _o = new _object();
				_o.objectname = termList.get(t).word;
				_o.parent_object = null;
				_objectlist.add(_o);
				thisstructclass.add(_o);
				System.out.println("“是”A的B");
			}
		}
		// （非“的”）A是B
		for (int t = 1; t < termListSize - 2; t++) {
			if (!getParent(termList.get(t - 1).nature.toString()).equals("ude1") && getParent(termList.get(t).nature.toString()).equals("n")
					&& getParent(termList.get(t + 1).nature.toString()).equals("vshi")
					&& getParent(termList.get(t + 2).nature.toString()).equals("n")) {
				_object scn1 = new _object();
				_object scn2 = new _object();
				scn2.parent_object.add(root);
				scn2.objectname = termList.get(t + 2).word;
				scn1.parent_object.add(scn2);
				scn1.objectname = termList.get(t).word;
				_objectlist.add(scn1);
				_objectlist.add(scn2);
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
					_object scn1 = new _object();
					_object scn2 = new _object();
					scn2.parent_object.add(root);
					scn2.objectname = termList.get(2).word;
					scn1.parent_object.add(scn2);
					scn1.objectname = termList.get(0).word;
					scn2.sub_object.add(scn1);
					_objectlist.add(scn1);
					_objectlist.add(scn2);
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
				_object _o = new _object();
				_objectproperty scp = new _objectproperty();
				_o.parent_object.add(root);
				_o.objectname = termList.get(t).word;
				scp.propertyname = termList.get(t + 2).word;
				scp.propertyvalue = termList.get(t + 4).word + "的";
				_o.objectproperty.add(scp);
				_objectlist.add(_o);
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
				_object _o = new _object();
				_objectproperty scp = new _objectproperty();
				_o.parent_object.add(root);
				_o.objectname = termList.get(t).word;
				scp.propertyname = termList.get(t + 2).word;
				scp.propertyvalue = termList.get(t + 4).word + "的";
				_o.objectproperty.add(scp);
				_objectlist.add(_o);
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
				_object _o = new _object();
				_objectproperty scp = new _objectproperty();
				_o.parent_object.add(root);
				_o.objectname = termList.get(t + 2).word;
				scp.propertyname = termList.get(t + 4).word;
				scp.propertyvalue = termList.get(t).word;
				_o.objectproperty.add(scp);
				_objectlist.add(_o);
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
					_object _o = new _object();
					_object sci = new _object();
					_o.objectname = termList.get(t + 3).word;
					_o.parent_object.add(root);
					sci.objecttype = 1;
					sci.objectname = termList.get(t).word;
					_o.sub_object.add(sci);
					_objectlist.add(_o);
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
				_object _o = new _object();
				_objectproperty scp = new _objectproperty();
				_o.parent_object.add(root);
				_o.objectname = termList.get(t).word;
				scp.propertyname = termList.get(t + 4).word;
				scp.propertyvalue = termList.get(t + 2).word;
				_o.objectproperty.add(scp);
				_objectlist.add(_o);
				thisstructclass.add(_o);
				System.out.println("A有adj的B");
			}
		}

		int x = thisstructclass.size();
		String parentobj = "";
		String subobj = "";
		String objproperty = "";

		while (x-- > 0) {
			// object
			String tempinfo = "------------------------------\n";
			String classname = thisstructclass.get(x).objectname;
			if (thisstructclass.get(x).parent_object != null)
				for (_object parent : thisstructclass.get(x).parent_object) {
					String parentobjname = parent.objectname;
					if (!parentobjname.equals("root")) {
						parentobj += parentobjname + ";";
						tempinfo += "\t--->父类为" + parentobj + "\n";
					} else
						tempinfo += "\t--->父类为root\n";
				}

			tempinfo += "object:" + classname + "";

			// property
			if (thisstructclass.get(x).objectproperty.size() > 0)
				for (_objectproperty property : thisstructclass.get(x).objectproperty) {
					String propertyname = property.propertyname.toString();
					String propertyvalue = property.propertyvalue.toString();
					objproperty += propertyname + ":" + propertyvalue + ";";
					tempinfo += "--->属性" + objproperty + "\n";
				}
			else
				tempinfo += "--->无属性\n";

			// subobject
			if (thisstructclass.get(x).sub_object.size() > 0)
				for (_object sub : thisstructclass.get(x).sub_object) {
					String subobjname = sub.objectname;
					if (!subobj.equals(null)) {
						subobj += subobjname + ";";
					}
					tempinfo += "\t--->子类为" + subobj;
					// "\n";
				}
			else
				tempinfo += "\t--->子类为空";
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
		temp = getwordParent.getParent.getparentAttr(s);
		if (temp == "no") {
			temp = s;
		}
		return temp;
	}

}