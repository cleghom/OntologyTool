package webCrawler4Baidu;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class transURLtoINFO {
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

	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		String temp = null;
		temp = trans("http://www.169it.com/article/13284450410759638056.html", temp);
		System.out.println(temp);
		System.out.println("over");
	}

	public static String trans(String url, String info) throws FailingHttpStatusCodeException, MalformedURLException, IOException {

		ArrayList<String> hrefList = new ArrayList<String>();
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		try {
			HtmlPage page = null;
			try {
				page = (HtmlPage) webClient.getPage(url);
			} catch (ConnectException e) {
			}
			InputStream temp = new ByteArrayInputStream(page.asText().getBytes());
			InputStreamReader isr = new InputStreamReader(temp);
			BufferedReader br = new BufferedReader(isr);
			String str = null, rs = null;
			while ((str = br.readLine()) != null) {
				rs = str;
				if (rs != null)
					hrefList.add(rs);
			}
			System.out.println("从该网址" + url + "查找的可能相关文本如下：");
			for (int i = 0; i < hrefList.size(); i++) {
				String string = hrefList.get(i);
				string = getTextFromHtml(string);
				if (string.length() >= 50) {
					info += "\n" + string;
				}
			}
		} catch (IOException e) {
		}
		return info;
	}

	public static ArrayList<String> trans(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException {

		ArrayList<String> hrefList = new ArrayList<String>();
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		try {
			HtmlPage page = null;
			try {
				page = (HtmlPage) webClient.getPage(url);
			} catch (ConnectException e) {
				System.out.println("Connect fails here:" + e.getMessage());
			}
			InputStream temp = new ByteArrayInputStream(page.asText().getBytes());
			InputStreamReader isr = new InputStreamReader(temp);
			BufferedReader br = new BufferedReader(isr);
			String str = null, rs = null;
			while ((str = br.readLine()) != null) {
				rs = str;
				// System.out.println(rs);
				if (rs != null)
					hrefList.add(rs);
			}
			System.out.println("从该网址查找的可能相关文本如下：");
			for (int i = 0; i < hrefList.size(); i++) {
				String string = hrefList.get(i);
				string = getTextFromHtml(string);
				if (string.length() >= 30)
					System.out.println("------" + i + ":" + string);
			}
		} catch (IOException e) {
		}
		return hrefList;
	}
	/*
	 * 从一行开始清除标签
	 *
	 * @return
	 */
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
