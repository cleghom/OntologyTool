package webCrawlerforBD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

///////////////资料来源http://www.cnblogs.com/cation/p/3933408.html

/**
 * @author Zhao Hang
 * @date:2015-3-26下午1:07:47
 * @email:1610227688@qq.com
 */
public class HtmlUnitforBD {
	private static int N = 100;// 搜索条数
	private static String keyW = "android";// 搜索词
	private static HtmlPage firstBaiduPage;// 保存第一页搜索结果
	private static String format = "";// Baidu对应每个搜索结果的第一页第二页第三页等等其中包哈“&pn=1”,“&pn=2”,“&pn=3”等等，提取该链接并处理可以获取到一个模板，用于定位某页搜索结果
	private static ArrayList<String> eachurl = new ArrayList<String>();// 用于保存链接

	public static void main(String[] args) throws Exception {
	}

	public static void mainFunction(final int n, final String keyWord, final JTextPane jtp, final JTextPane jtp2)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// 输出欲查询数量
				int page1linknum = 1;
				int x = n / 10;// 页数
				int y = n % 10;// 余外个数
				System.out.println("要提取百度关于“" + keyWord + "”的搜索结果共计" + N + "条" + "应抓取搜索结果的前" + x + "页，另外的" + y + "个为方便提取此处忽略");
				// 获取并输出第一页百度查询内容
				Elements firstPageURL = null;
				try {
					firstPageURL = getFirstPage(keyWord);
				} catch (FailingHttpStatusCodeException | IOException e) {
					e.printStackTrace();
				}// 定义firstPageURL作为第一个搜索页的元素集
				for (Element newlink : firstPageURL) {
					String linkHref = newlink.attr("href");// 提取包含“href”的元素成分，JSoup实现内部具体过程
					String linkText = newlink.text();// 声明变量用于保存每个链接的摘要
					if (linkHref.length() > 14 & linkText.length() > 2) {// 去除某些无效链接
						System.out.println(linkHref + "\n\t\t摘要：" + linkText);// 输出链接和摘要
						eachurl.add(linkHref);// 作为存储手段存储在arrayList里面
						insertWeb(jtp, page1linknum++ + ":" + linkHref);// 在界面中显示该条链接
						try {
							System.out.println("******" + linkHref);
							showWebInfo(jtp2, linkHref);
						} catch (FailingHttpStatusCodeException | BadLocationException | IOException e) {
							e.printStackTrace();
						}
					}
				}
				nextHref(firstBaiduPage);// 以firstBaiduPage作为参数，定义format，即网页格式。
				for (int i = 1; i < x; i++) {
					System.out.println("\n************百度搜索“" + keyW + "”第" + (i + 1) + "页结果************");
					String tempURL = format.replaceAll("&pn=1", "&pn=" + i + "");// 根据已知格式修改生成新的一页的链接
					System.out.println(format.replaceAll("&pn=1", "&pn=" + i + ""));// 显示该搜索模板
					HtmlUnitforBD h = new HtmlUnitforBD();
					String htmls = h.getPageSource(tempURL, "utf-8");// 不知为何此处直接用JSoup的相关代码摘取网页内容会出现问题，所以采用新的编码来实现摘取网页源码
					org.jsoup.nodes.Document doc = Jsoup.parse(htmls);// 网页信息转换为jsoup可识别的doc模式
					Elements links = doc.select("a[data-click]");// 摘取该页搜索链接
					for (Element newlink : links) {// 该处同上getFirstPage的相关实现
						String linkHref = newlink.attr("href");
						String linkText = newlink.text();
						if (linkHref.length() > 14 & linkText.length() > 2) {// 删除某些无效链接，查查看可发现有些无效链接是不包含信息文本的
							System.out.println(linkHref + "\n\t\t摘要：" + linkText);
							eachurl.add(linkHref);// 作为存储手段存储在arrayList里面
							insertWeb(jtp, page1linknum++ + ":" + linkHref);
							try {
								System.out.println("******" + linkHref);
								showWebInfo(jtp2, linkHref);
							} catch (FailingHttpStatusCodeException | BadLocationException | IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
				return;
			}
		});
		thread.start();
	}

	/*
	 * 获取百度搜索第一页内容
	 */
	public static Elements getFirstPage(String w) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// 创建Web Client
		String word = w;
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setJavaScriptEnabled(false);// HtmlUnit对JavaScript的支持不好，关闭之
		webClient.getOptions().setCssEnabled(false);// HtmlUnit对CSS的支持不好，关闭之
		HtmlPage page = (HtmlPage) webClient.getPage("http://www.baidu.com/");// 百度搜索首页页面
		HtmlInput input = (HtmlInput) page.getHtmlElementById("kw");// 获取搜索输入框并提交搜索内容（查看源码获取元素名称）
		input.setValueAttribute(word);// 将搜索词模拟填进百度输入框（元素ID如上）
		HtmlInput btn = (HtmlInput) page.getHtmlElementById("su");// 获取搜索按钮并点击
		firstBaiduPage = btn.click();// 模拟搜索按钮事件
		String WebString = firstBaiduPage.asXml().toString();// 将获取到的百度搜索的第一页信息输出
		org.jsoup.nodes.Document doc = Jsoup.parse(WebString);// 转换为Jsoup识别的doc格式
		System.out.println("************百度搜索“" + word + "”第一页结果************");// 输出第一页结果
		Elements links = doc.select("a[data-click]");// 返回包含类似<a......data-click=" "......>等的元素，详查JsoupAPI
		return links;// 返回此类链接，即第一页的百度搜素链接
	}

	/*
	 * 获取下一页地址
	 */
	public static void nextHref(HtmlPage p) {
		// 输入：HtmlPage格式变量，第一页的网页内容；
		// 输出：format的模板
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		p = firstBaiduPage;
		String morelinks = p.getElementById("page").asXml();// 获取到百度第一页搜索的底端的页码的html代码
		org.jsoup.nodes.Document doc = Jsoup.parse(morelinks);// 转换为Jsoup识别的doc格式
		Elements links = doc.select("a[href]");// 提取这个html中的包含<a href=""....>的部分
		boolean getFormat = true;// 设置只取一次每页链接的模板格式
		for (Element newlink : links) {
			String linkHref = newlink.attr("href");// 将提取出来的<a>标签中的链接取出
			if (getFormat) {
				format = "http://www.baidu.com" + linkHref;// 补全模板格式
				getFormat = false;
			}
		}
	}

	public String getPageSource(String pageUrl, String encoding) {
		// 输入：url链接&编码格式
		// 输出：该网页内容
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(pageUrl);// 构建一URL对象
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), encoding));// 使用openStream得到一输入流并由此构造一个BufferedReader对象
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			in.close();
		} catch (Exception ex) {
			System.err.println(ex);
		}
		return sb.toString();
	}

	public static void insertWeb(JTextPane textpane, String web) {
		SimpleAttributeSet set = new SimpleAttributeSet();
		StyleConstants.setUnderline(set, true);
		try {
			textpane.getDocument().insertString(textpane.getDocument().getLength(), web + "\n", set);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public static void showWebInfo(final JTextPane textpane, final String weburl) throws FailingHttpStatusCodeException, MalformedURLException,
			BadLocationException, IOException {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				SimpleAttributeSet set = new SimpleAttributeSet();
				StyleConstants.setUnderline(set, true);
				String temp = null;
				try {
					textpane.getDocument().insertString(textpane.getDocument().getLength(), ">>>>>>"+transURLtoINFO.trans(weburl, temp) + "\n", set);
					System.out.println("#######################################################\n"+transURLtoINFO.trans(weburl, temp));
				} catch (BadLocationException | FailingHttpStatusCodeException | IOException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
}
