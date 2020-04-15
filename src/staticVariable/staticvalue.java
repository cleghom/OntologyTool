package staticVariable;

public class staticvalue {
    /**
     * @param args
     * 全局变量
     */
    //文件路径
    //    public static String localaddr = "D:\\Program Files\\Ontologytool";
    public static String localaddr = System.getProperty("user.dir"); // 保存输出的owl或者rdf文件
    public static String tempfilename = "";
    //    public static String projecturl = "E:\\workspacefortest\\workspace\\OntologyTool - 0725\\";
    public static String projecturl = System.getProperty("user.dir");
}
