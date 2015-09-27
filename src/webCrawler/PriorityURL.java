package webCrawler;


/**
 * @author Zhao Hang
 * @date:2015-3-23ионГ8:28:06
 * @email:1610227688@qq.com
 */
public class PriorityURL implements Comparable<PriorityURL> {
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private double priority;

	public PriorityURL(String url, double priority) {
		super();
		this.url = url;
		this.priority = priority;
	}

	@Override
	public int compareTo(PriorityURL o) {
		if (priority > o.priority)
			return 1;
		else if (priority < o.priority)
			return -1;
		else
			return 0;
	}

}
