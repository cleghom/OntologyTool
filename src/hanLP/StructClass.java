package hanLP;

import java.util.ArrayList;

/**
 * @author Zhao Hang
 * @date:2015-4-11下午1:03:56
 * @email:1610227688@qq.com
 */
public class StructClass {

	public String ontObjectName;// 此类名称
	public String ontObjectParentName;// 父类名称
	public ArrayList<StructClassProperty> ontObjectProperty = new ArrayList<StructClassProperty>();// 该类的属性列表
	public ArrayList<StructClassIndividual> ontObjectIndividual = new ArrayList<StructClassIndividual>();
}
