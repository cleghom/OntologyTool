package getContent;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zhao Hang
 * @date:2015-5-26下午1:05:37
 * @email:1610227688@qq.com 。
 */
public class GetContent {
	private static final String regEx_all = "<script[^>]*?>[\\s\\S]*?<\\/script>|<style[^>]*?>[\\s\\S]*?<\\/style>|<[^>]+>|\\s*|\t|\r|\n|\\-";
	// 分别消除script，style，html，空格换行的正则表达式
	static ArrayList<String> hrefList = new ArrayList<String>();

	/*
	 * 分析结果
	 */
	public static String getcontent(String webcontent, String newcontent) {
		if (webcontent != null) {
			newcontent = delHTMLTag(newcontent);
			System.out.println(newcontent);
			if (newcontent != null)
				hrefList.add(newcontent);
		}
		String string = null;
		for (int i = 0; i < hrefList.size(); i++) {
			string = getTextFromHtml(hrefList.get(i));
			if (string.length() >= 14)
				System.out.println("------" + i + ":" + string);
		}
		return newcontent;
	}

	public static String getTextFromHtml(String htmlStr) {
		delHTMLTag(htmlStr).replaceAll("&nbsp;", "");
		return htmlStr;
	}

	public static String delHTMLTag(String htmlStr) {
		Pattern p_all = Pattern.compile(regEx_all, Pattern.CASE_INSENSITIVE);
		Matcher m_all = p_all.matcher(regEx_all);
		htmlStr = m_all.replaceAll("");
		return htmlStr.trim();
	}
}
