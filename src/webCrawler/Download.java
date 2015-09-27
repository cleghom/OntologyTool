package webCrawler;

/**
 * @author Zhao Hang
 * @date:2015-3-23上午8:25:47
 * @email:1610227688@qq.com
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Download {
	private static long count = 1;

	private boolean checkURL(URL url) {
		String s = url.toString();
		if (s.endsWith(".zip") || s.endsWith(".gz") || s.endsWith(".rar") || s.endsWith(".exe") || s.endsWith(".exe") || s.endsWith(".jpg")
				|| s.endsWith(".png") || s.endsWith(".tar") || s.endsWith(".chm") || s.endsWith(".iso") || s.endsWith(".gif") || s.endsWith(".csv")
				|| s.endsWith(".pdf") || s.endsWith(".doc"))
			return false;
		else
			return true;
	}

	public String downloadHttp(URL url) {
		boolean isOK = checkURL(url);
		if (!isOK)
			return null;
		StringBuffer content = new StringBuffer();
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpConstants.HTTP_NOTFOUND || responseCode == HttpConstants.HTTP_FORBIDDEN)
				return null;
			// int i = 1;
			String value;
			// String key;
			boolean hascharset = false;
			value = connection.getHeaderField("Content-Type");
			// System.out.println("value is :" + value);
			if (value != null) {
				int index = value.indexOf("charset=");
				if (index >= 0)// 如果在Content-Type中指定charset，我们就用该编码，否则在读完之后查找编码
				{
					value = value.substring(index + 8);
					// System.out.println(value+"hahaha");
					hascharset = true;
				}
			}

			BufferedReader reader = null;
			if (hascharset) {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), value));
			} else
			// 如果找不到编码，我们默认将其设置为ISO-8859-1
			{
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "ISO-8859-1"));
			}
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line + "\n");
			}
			if (!hascharset)// 在文件中查找编码，然后将读取的内容转换成该编码
			{
				int index = content.indexOf("charset=");
				// System.out.println(index);
				if (index >= 0) {
					String charset = content.substring(index + 8, index + 18).split("\"")[0];
					byte[] b;
					b = content.toString().getBytes("ISO-8859-1");
					return new String(b, charset);
				}
			}
			return content.toString();
		} catch (IOException e) {
			return null;
		}
	}

	public String getContent(String content) {
		System.out.println(content + "*******************");

		Pattern p = Pattern.compile("<.+>");
		Matcher m = p.matcher(content);
		content = m.replaceAll("");
		System.out.println(content + "*******************");

		p = Pattern.compile("\\s+");
		m = p.matcher(content);
		content = m.replaceAll("");
		System.out.println(content + "*******************");

		p = Pattern.compile("\\{.+\\}");
		m = p.matcher(content);
		content = m.replaceAll("");
		System.out.println(content + "*******************");
		return content;
	}

	public void writeToFile(String content) {
		try {
			DecimalFormat format = new DecimalFormat("00000000");
			String name = format.format(count++);
			PrintWriter writer = new PrintWriter(new FileOutputStream(name));
			writer.print(content);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			URL url = new URL(" http://toolbarqueries.google.com.hk/search?client=navclient-auto"
					+ "&hl=en&ch=63152358434&ie=UTF-8&oe=UTF-8&features=Rank&q=info:www.baidu.com");
			Download d = new Download();
			String content = d.downloadHttp(url);
			System.out.println(content);
			d.getContent(content);
			d.writeToFile(content);

			String s = "<gdf dfgdf" + ">";
			Pattern p = Pattern.compile("<(.*|\\s*)*>");
			Matcher m = p.matcher(s);
			if (m.matches())
				s = m.replaceAll("");
			System.out.println("fgf" + s);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
