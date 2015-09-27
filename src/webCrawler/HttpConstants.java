package webCrawler;

/**
 * @author Zhao Hang
 * @date:2015-3-23ÉÏÎç8:27:43
 * @email:1610227688@qq.com
 */
public class HttpConstants {
	public final static int HTTP_OK = 200;
	public final static int HTTP_FOUND = 302;
	public final static int HTTP_MOVEDPERMANENTLY = 301;
	public final static int HTTP_UNAUTHORIZED = 401;
	public final static int HTTP_FORBIDDEN = 403;
	public final static int HTTP_NOTFOUND = 404;
	public final static int HTTP_NOTMODIFIED = 304;

	/** HTTP GET request **/
	public final static int GET = 1;

	/** HTTP POST request **/
	public final static int POST = 2;

	/** HTTP HEAD request **/
	public final static int HEAD = 3;
}