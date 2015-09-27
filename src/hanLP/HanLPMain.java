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

/**
 * @author Zhao Hang
 * @date:2015-4-11下午1:07:48
 * @email:1610227688@qq.com
 */
public class HanLPMain {

	static ArrayList<StructClass> structclass = new ArrayList<StructClass>();//
	static List<Term> mainTermList = new ArrayList<Term>();

	public static ArrayList<StructClass> getStructclass() {
		return structclass;
	}

	// 主方法实现从字符串和文件中读入文本，连续即可。
	public static void main(String[] args) {
		// String s = "艾滋病的疾病传播方式是性传播。脊椎动物。无脊椎动物。脊椎动物有骨。无脊椎动物没有骨.";
		// String s2 =
		// "鸟是脊椎动物，人是脊椎动物。麻雀是鸟。国际古生物学界的关注，兰州面条食物。鸟有2个翅膀，鸟的体温类型是恒温，鸟的生殖方式是卵生，鸟的食物是种籽，鸟的食物是果实，鸟的食物是昆虫。化学证据支持了这个假说。鸟会飞。孔雀是鸟，孔雀不会飞，麻雀会飞，企鹅不会飞，孔雀有两只脚，麻雀有2只脚，麻雀是鸟，企鹅是鸟，麻雀吃昆虫，企鹅吃鱼，孔雀不吃昆虫。鸟有2个翅膀，鸟的体温类型是恒温，鸟的生殖方式是卵生，孔雀的食物是苗芽，鸟的类别是动物，孔雀的类别是鸟。";
		// String s3 =
		// "鸟是脊椎动物，人是脊椎动物。麻雀是鸟。乌鸦是鸟，脊椎动物是动物。蚂蚁是无脊椎动物。骨头的化学成分是钙。人的寿命不确定。钙是金属。艾滋病的疾病传播方式是性传.鸟的体温类型是恒温，鸟的生殖方式是卵生，书的封面是纸张。骨头的味道是无味。纸张的原料是木材。";
		// mainfunction(s3);
		// File f = new File("C:/Users/ZH/Desktop/bird/bird原始信息.txt");
		// mainfunction(f);
		// /////////////////////// String s =
		// "赵航的性别是男。赵航的姓名是赵航大帝。赵航的昵称是悦悦。赵航的年龄是23.中华人民共和国是国家。胡锦涛是中华人民共和国的主席.艾滋病的感染性是强的。艾滋病的疾病传播方式是性传播和母婴传播。鸟的翅膀数量是2。麻雀是鸟，啄木鸟是鸟，乌鸦是鸟。";

		System.out.println("全句。");
		mainfunction("胡锦涛是一个中国人。Eclipse是一个编程工具。中华人民共和国是国家。胡锦涛是中华人民共和国的主席.艾滋病的感染性是强的。艾滋病的疾病传播方式是性传播和母婴传播。鸟的翅膀数量是2。麻雀是鸟，啄木鸟是鸟，乌鸦是鸟。");

	}

	// 主方法调用的读字符串分析
	public static ArrayList<StructClass> mainfunction(String string) {
		ArrayList<String> separateList = separateString(string);
		for (String s : separateList) {
			mainfunctionforstring(s);
		}
		return structclass;
	}

	// 主方法调用的读文件分析
	public static ArrayList<StructClass> mainfunction(File file) {
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
		return structclass;
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
				System.out.println(temp);
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
		System.out.println("advanced format is :" + termList);
		return termList;
	}

	/*
	 * 分析单个句子成分，以String为参数
	 */
	public static void analyseEachString(String s) {
		mainTermList = getAdvancedTermOfthisSentencs(getBaseTermOfthisSentence(s));
		getObject(mainTermList);
		mainTermList = null;
	}

	public static List<Term> getMainTermList(List<Term> list) {
		// 迭代执行简化操作
		List<Term> temp = new ArrayList<Term>();
		temp = simplyTermList(list);
		return temp;
	}

	public static List<Term> simplyTermList(List<Term> list) {
		System.out.println("分析->" + list);
		List<Term> termList = new ArrayList<Term>();
		termList = list;
		int termListSize = list.size();
		for (int t = 0; t < termListSize - 1; t++) {
			// 分类实现化简
			// 化简名词
			if (getParent(termList.get(t).nature.toString()) == "n" && getParent(termList.get(t + 1).nature.toString()) == "n") {
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
	public static String getObject(List<Term> list) {
		int termListSize = list.size();
		ArrayList<StructClass> thisstructclass = new ArrayList<StructClass>();
		List<Term> termList = new ArrayList<Term>();
		termList = list;
		// A的B（非“是”）
		for (int t = 0; t < termListSize - 2; t++) {
			if (getParent(termList.get(t).nature.toString()) == "n" && getParent(termList.get(t + 1).nature.toString()) == "ude1"
					&& getParent(termList.get(t + 2).nature.toString()) == "n" && getParent(termList.get(t + 3).nature.toString()) != "vshi") {
				StructClass sc = new StructClass();
				sc.ontObjectName = termList.get(t).word;
				sc.ontObjectParentName = "root";
				structclass.add(sc);
				thisstructclass.add(sc);
			}
		}
		// （非“是”）A的B
		for (int t = 1; t < termListSize - 1; t++) {
			if (getParent(termList.get(t - 1).nature.toString()) != "vshi" && getParent(termList.get(t).nature.toString()) == "n"
					&& getParent(termList.get(t + 1).nature.toString()) == "ude1" && getParent(termList.get(t + 2).nature.toString()) == "n") {
				StructClass sc = new StructClass();
				sc.ontObjectName = termList.get(t).word;
				sc.ontObjectParentName = "root";
				structclass.add(sc);
				thisstructclass.add(sc);
			}
		}
		// （非“的”）A是B
		for (int t = 1; t < termListSize - 2; t++) {
			if (getParent(termList.get(t - 1).nature.toString()) != "ude1" && getParent(termList.get(t).nature.toString()) == "n"
					&& getParent(termList.get(t + 1).nature.toString()) == "vshi" && getParent(termList.get(t + 2).nature.toString()) == "n") {
				StructClass scn1 = new StructClass();
				StructClass scn2 = new StructClass();
				scn2.ontObjectParentName = "root";
				scn2.ontObjectName = termList.get(t + 2).word;
				scn1.ontObjectParentName = scn2.ontObjectName;
				scn1.ontObjectName = termList.get(t).word;
				structclass.add(scn1);
				structclass.add(scn2);
				thisstructclass.add(scn1);
				thisstructclass.add(scn2);
			}
		}
		// 判断首句是“A是B”
		for (int t = 0; t < 1; t++) {
			if (termList.size() >= 3) {
				if (getParent(termList.get(0).nature.toString()) == "n" && getParent(termList.get(1).nature.toString()) == "vshi"
						&& getParent(termList.get(2).nature.toString()) == "n" && getParent(termList.get(3).nature.toString()) != "ude1") {
					StructClass scn1 = new StructClass();
					StructClass scn2 = new StructClass();
					scn2.ontObjectParentName = "root";
					scn2.ontObjectName = termList.get(2).word;
					scn1.ontObjectParentName = scn2.ontObjectName;
					scn1.ontObjectName = termList.get(0).word;
					System.out.println(scn2.ontObjectParentName + ":" + scn2.ontObjectName + ":" + scn1.ontObjectParentName + ":"
							+ scn1.ontObjectName);
					structclass.add(scn1);
					structclass.add(scn2);
					thisstructclass.add(scn1);
					thisstructclass.add(scn2);
				}
			}
		}
		// A的B是C（n）
		for (int t = 0; t < termListSize - 4; t++) {
			if (getParent(termList.get(t).nature.toString()) == "n" && getParent(termList.get(t + 1).nature.toString()) == "ude1"
					&& getParent(termList.get(t + 2).nature.toString()) == "n" && getParent(termList.get(t + 3).nature.toString()) == "vshi"
					&& getParent(termList.get(t + 4).nature.toString()) == "n") {
				StructClass sc = new StructClass();
				StructClassProperty scp = new StructClassProperty();
				sc.ontObjectParentName = "root";
				sc.ontObjectName = termList.get(t).word;
				scp.ontObjectPropertyName = termList.get(t + 2).word;
				scp.ontObjectPropertyValue = termList.get(t + 4).word;
				sc.ontObjectProperty.add(scp);
				structclass.add(sc);
				thisstructclass.add(sc);
			}
		}
		// A的B是C（adj）
		for (int t = 0; t < termListSize - 4; t++) {
			if (getParent(termList.get(t).nature.toString()) == "n" && getParent(termList.get(t + 1).nature.toString()) == "ude1"
					&& getParent(termList.get(t + 2).nature.toString()) == "n" && getParent(termList.get(t + 3).nature.toString()) == "vshi"
					&& getParent(termList.get(t + 4).nature.toString()) == "a") {
				StructClass sc = new StructClass();
				StructClassProperty scp = new StructClassProperty();
				sc.ontObjectParentName = "root";
				sc.ontObjectName = termList.get(t).word;
				scp.ontObjectPropertyName = termList.get(t + 2).word;
				scp.ontObjectPropertyValue = termList.get(t + 4).word;
				sc.ontObjectProperty.add(scp);
				structclass.add(sc);
				thisstructclass.add(sc);
			}
		}
		// A的B是C（adj）
		for (int t = 0; t < termListSize - 4; t++) {
			if (getParent(termList.get(t).nature.toString()) == "n" && getParent(termList.get(t + 1).nature.toString()) == "ude1"
					&& getParent(termList.get(t + 2).nature.toString()) == "n" && getParent(termList.get(t + 3).nature.toString()) == "vshi"
					&& getParent(termList.get(t + 4).nature.toString()) == "m") {
				StructClass sc = new StructClass();
				StructClassProperty scp = new StructClassProperty();
				sc.ontObjectParentName = "root";
				sc.ontObjectName = termList.get(t).word;
				scp.ontObjectPropertyName = termList.get(t + 2).word;
				scp.ontObjectPropertyValue = termList.get(t + 4).word;
				sc.ontObjectProperty.add(scp);
				structclass.add(sc);
				thisstructclass.add(sc);
			}
		}
		// A是B的C
		for (int t = 0; t < termListSize - 4; t++) {
			if (getParent(termList.get(t).nature.toString()) == "n" && getParent(termList.get(t + 1).nature.toString()) == "vshi"
					&& getParent(termList.get(t + 2).nature.toString()) == "n" && getParent(termList.get(t + 3).nature.toString()) == "ude1"
					&& getParent(termList.get(t + 4).nature.toString()) == "n") {
				StructClass sc = new StructClass();
				StructClassProperty scp = new StructClassProperty();
				sc.ontObjectParentName = "root";
				sc.ontObjectName = termList.get(t + 2).word;
				scp.ontObjectPropertyName = termList.get(t + 4).word;
				scp.ontObjectPropertyValue = termList.get(t).word;
				sc.ontObjectProperty.add(scp);
				structclass.add(sc);
				thisstructclass.add(sc);
			}
		}
		// A是一个B
		// 添加B的一个实例A
		for (int t = 0; t < termListSize - 4; t++) {
			if (getParent(termList.get(t).nature.toString()) == "n" && getParent(termList.get(t + 1).nature.toString()) == "vshi"
					&& getParent(termList.get(t + 2).nature.toString()) == "mq" && getParent(termList.get(t + 3).nature.toString()) == "n") {
				String mpword = getParent(termList.get(t + 2).word);
				if (mpword.contains("一") || mpword.contains("1")) {
					StructClass sc = new StructClass();
					StructClassIndividual sci = new StructClassIndividual();
					sc.ontObjectName = termList.get(t + 3).word;
					sc.ontObjectParentName = "root";
					sci.ontObjectIndividualName = termList.get(t).word;
					sc.ontObjectIndividual.add(sci);
					structclass.add(sc);
					thisstructclass.add(sc);
				}
			}
		}

		System.out.println("The Great 赵航大帝 写的类结构体大小：" + structclass.size());
		int x = thisstructclass.size();
		String geteachObject = "";
		String geteachIndividual = "";
		while (x-- > 0) {
			// System.out.println("类关系");
			String classname = thisstructclass.get(x).ontObjectName;
			String parentclassname = thisstructclass.get(x).ontObjectParentName;
			int classpropertynumber = thisstructclass.get(x).ontObjectProperty.size();
			if (!classname.equals("")) {
				geteachObject = "子类：" + classname + "---->父类：" + parentclassname + "\n\t类的属性个数:" + classpropertynumber;
			}
			int y = thisstructclass.get(x).ontObjectProperty.size();
			while (y-- > 0) {
				String propertyname = thisstructclass.get(x).ontObjectProperty.get(y).ontObjectPropertyName.toString();
				String propertyvalue = thisstructclass.get(x).ontObjectProperty.get(y).ontObjectPropertyValue.toString();
				if (!propertyname.equals(""))
					geteachObject += "\n\t\t" + propertyname + "---->" + propertyvalue;
			}
			System.out.println("new object is " + list.toString() + "\n" + geteachObject);
			int z = thisstructclass.get(x).ontObjectIndividual.size();
			geteachIndividual = "\n\t类的个体数:" + z;
			while (z-- > 0) {
				String individualname = thisstructclass.get(x).ontObjectIndividual.get(z).ontObjectIndividualName;
				// geteachIndividual = "";
				geteachIndividual += "\n\t\t个体：" + individualname + "--------->父类：" + classname;
				System.out.println(geteachIndividual);
			}
		}
		System.out.println(geteachObject + "\n\t\t" + geteachIndividual);
		return geteachObject + geteachIndividual;
	}

	/*
	 * 获取某个词的父类词性
	 */
	public static String getParent(String s) {
		String temp = "";
		switch (s) {
		case "vn":// 动名词
			temp = "n";
			break;
		case "nf":// 食物
			temp = "n";
			break;
		case "ns":// 地点
			temp = "n";
			break;
		case "ng":
			temp = "n";
			break;
		case "nhd":
			temp = "n";
			break;
		case "nsf":
			temp = "n";
			break;
		case "nrf":
			temp = "n";
			break;
		case "nz":
			temp = "n";
			break;
		case "nr":// 人名
			temp = "n";
			break;
		case "nrj":// 人名
			temp = "n";
			break;
		case "nnt":
			temp = "n";
			break;
		case "b":// 性别
			temp = "n";
			break;
		case "nx":// 性别
			temp = "n";
			break;
		case "nis"://
			temp = "n";
			break;
		case "t":// 时间词
			temp = "n";
			break;
		case "gp":
			temp = "n";
			break;
		case "gb":
			temp = "n";
			break;
		case "gc":
			temp = "n";
			break;
		case "gm":
			temp = "n";
		case "vi":
			temp = "n";
			break;
		case "n":
			temp = "n";
			break;
		default:
			temp = s;
		}
		return temp;
	}
}
