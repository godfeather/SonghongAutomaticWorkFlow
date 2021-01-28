package arguments;

import arguments.Argument;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.*;

public class Args{
    private JsonObject jsonObject;
    private JsonObject __param;
    private JsonObject _param;
    private JsonArray list;
    static HashMap<String,Argument> recognizedArgs = new HashMap<String, Argument>();
    static ArrayList<String> sorter = new ArrayList<String>();

    /**
     * 使命令行帮助以添加的顺序显示
     */
    static void putArgToSorter (String key,Argument arg) {
        recognizedArgs.put(key,arg);
        sorter.add(key);
    }
    static void injectRecongnizedArgs () {
        putArgToSorter("mode",new Argument("-m","--mode","调用模式,有效值：auto(自动计算并根据计算结果开始自动审核流程),calc(查看流程序列计算结果),manual(自动审核，但无流程计算)"));
        putArgToSorter("name",new Argument("-n","--name","数据库中的流程名称"));
        putArgToSorter("title",new Argument("-t","--title","被测系统中已发起流程的流程标题"));
        putArgToSorter("reason",new Argument("-r","--reason","流程发起者"));
        putArgToSorter("reject",new Argument("-R","--reject","执行驳回测试，并指定驳回间隔,指定无效值时使用默认（无间隔）"));
        putArgToSorter("sequence",new Argument("-s","--sequence","流程审核者队列，使用，隔开"));
        putArgToSorter("creationTime",new Argument("-T","--creation-time","指定流程的创建时间，若指定，则查找流程时满足流程title和创建时间同时相同的流程才会被视为找到，否则认定为流程不存在"));
        putArgToSorter("parameter",new Argument("-P","--parameter","指定参数文件进行驱动程序"));
        putArgToSorter("lineIndex",new Argument("-l","--line","参数行指针，若指定参数文件时可选,默认-1,表示不执行驳回"));
        putArgToSorter("connectionString",new Argument("-c","--connectionStr","数据库ip:port"));
        putArgToSorter("user",new Argument("-u","--user","数据库用户名"));
        putArgToSorter("password",new Argument("-p","--pwd","数据库密码"));
        putArgToSorter("help",new Argument("-h","--help","帮助"));
    }
    public Args(String[] args) {
        injectRecongnizedArgs();
        this.jsonObject = (JsonObject) parse(args);
        this.__param = (JsonObject) this.jsonObject.get("--");
        this._param = (JsonObject) this.jsonObject.get("-");
        this.list = (JsonArray) this.jsonObject.get("args");
    }


    public List<String> getArgsList () {
        List<String> li = new ArrayList<String>();
        Iterator<JsonElement> it = list.iterator();
        while (it.hasNext()) {
            JsonElement js = it.next();
            li.add(js.getAsString());
        }
        return li;
    }
    /**
     * 通过参数key获取参数值
     * @param id
     * @return
     */
    public String getValue (String id) {
        Argument ar = recognizedArgs.get(id);
        if (ar == null) {
            return null;
        }
        if (ar._paramName != null) { // 查找选项所对应的单-key
            String _p = ar._paramName;
            JsonElement j = _param.get(_p);
            if (j != null) {
                return j.getAsString();
            }
        }
        if (ar.__paramName != null) { // 查找选项所对应的单--key
            String __p = ar.__paramName;
            JsonElement j = __param.get(__p);
            if (j == null) {
                return null;
            } else {
                return j.getAsString();
            }
        }
        return null;
    }
    public void printHelp () {
        Iterator<String> it = sorter.iterator();
        while (it.hasNext()) {
            Argument ar = recognizedArgs.get(it.next());
            String help = "";
            String _p = ar._paramName;
            String __p = ar.__paramName;
            help += _p.equals("") ? "" : _p + "\t";
            help += __p.equals("") ? "" : __p + ": ";
            help += ar.help;
            System.out.println(help);
        }
    }
    /**
     * 判断是否已设置某个参数
     * @param id
     * @return
     */
    public boolean isSeted (String id) {
        String s = this.getValue(id);
        return s == null ? false : true;
    }
    /**
     * 用于将用户参数解析成Json格式
     * @param args java控制台传入的参数
     * @return
     */
    public static JsonElement parse(String[] args) {
        JsonObject parameter = new JsonObject(); // 参数值
        JsonArray values = new JsonArray(); // 无对应key的参数值列表
        JsonObject _prefix = new JsonObject(); // -前缀的参数对
        JsonObject __prefix = new JsonObject(); // --前缀的参数对
        for (int i = 0;i < args.length;i ++) {
            String str = args[i];
            if (str.startsWith("-")) { // 参数名
                if (str.startsWith("--")) { // 完整参数名
                    if (str.length() > 2) { // 有效--前缀的参数名
                        if (i + 1 < args.length) { // 有下一个值
                            if (args[i + 1].startsWith("-")) {
                                __prefix.addProperty(str,"");
                            } else {
                                __prefix.addProperty(str,args[i + 1]);
                                i ++;
                            }
                        } else {
                            __prefix.addProperty(str,"");
                        }
                    } else {
                    }

                } else { // 以-前缀开始的参数名，只能有一个字母
                    if (str.length() > 1) { //有效单字符参数
                        String singleParam = str.substring(1,str.length());
                        for (int c = 0; c < singleParam.length();c ++) {
                            if (c == singleParam.length() -1) { //最后一个参数
                                if (i + 1 < args.length) {  // 有下一个参数
                                    String next = args[i + 1];
                                    if (next.startsWith("-")) {
                                        _prefix.addProperty("-" + singleParam.substring(c,c + 1),"");
                                    } else {
                                        _prefix.addProperty("-" + singleParam.substring(c,c + 1),next);
                                        i ++;
                                    }
                                }
                            } else { // 无参数值
                                _prefix.addProperty("-" + singleParam.substring(c,c + 1),"");
                            }
                        }
                    } else {
                    }
                }
            } else { // 无对应Key的参数值
                values.add(str);
            }
        }
        parameter.add("args",values);
        parameter.add("-",_prefix);
        parameter.add("--",__prefix);
        return parameter;
    }
}
