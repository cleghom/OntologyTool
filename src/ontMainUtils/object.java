package ontMainUtils;

import java.util.ArrayList;


public class object{
    public int objectType;
    public String objectName;
    public ArrayList<object> parentObject = new ArrayList<object>();
    public ArrayList<object> subObject = new ArrayList<object>();
    public ArrayList<objectProperty> objectproperty = new ArrayList<objectProperty>();
    public ArrayList<objectPredicate> objectpredicate = new ArrayList<objectPredicate>();
}
