package getContent;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetContent {
	/*
	 * 匹配消除html元素
	 */
	// 定义script的正则表达式
	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>";
	// 定义style的正则表达式
	private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>";
	// 定义HTML标签的正则表达式
	private static final String regEx_html = "<[^>]+>";
	// 定义空格回车换行符
	private static final String regEx_space = "\\s*|\t|\r|\n";
	// 用于保存所有获得的链接并保存
	static ArrayList<String> hrefList = new ArrayList<String>();
	/*
	 * 分析结果
	 */
	public static String getcontent(String webcontent, String newcontent) {
		String temp = null;
		if ((temp = webcontent) != null) {
			newcontent = delHTMLTag(temp);
			System.out.println(newcontent);
			if (newcontent != null)
				hrefList.add(newcontent);
		}
		System.out.println("从该网址查找的可能相关文本如下：");
		for (int i = 0; i < hrefList.size(); i++) {
			String string = hrefList.get(i);
			string = getTextFromHtml(string);
			if (string.length() >= 14)
				System.out.println("------" + i + ":" + string);
		}
		return newcontent;
	}

	public static String delHTMLTag(String htmlStr) {

		Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
		Matcher m_space = p_space.matcher(htmlStr);
		htmlStr = m_space.replaceAll(""); // 过滤空格回车标签

		Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		return htmlStr.trim(); // 返回文本字符串
	}

	public static String getTextFromHtml(String htmlStr) {
		htmlStr = delHTMLTag(htmlStr);
		htmlStr = htmlStr.replaceAll("&nbsp;", "");
		return htmlStr;
	}
}
