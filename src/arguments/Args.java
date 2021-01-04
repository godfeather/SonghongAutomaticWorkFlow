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
    static HashMap<String,Argument> recongnizedArgs = new HashMap<String, Argument>();
    static void injectRecongnizedArgs () {
        recongnizedArgs.put("help",new Argument("-h","--help","帮助"));
        recongnizedArgs.put("mode",new Argument("-m","--mode","调用模式,有效值：auto,calc,manual"));
        recongnizedArgs.put("connectionString",new Argument("-c","--connectionStr","数据库ip:port"));
        recongnizedArgs.put("user",new Argument("-u","--user","数据库用户名"));
        recongnizedArgs.put("password",new Argument("-p","--pwd","数据库密码"));
        recongnizedArgs.put("title",new Argument("-t","--title","流程标题"));
        recongnizedArgs.put("name",new Argument("-n","--name","流程名称"));
        recongnizedArgs.put("reason",new Argument("-r","--reason","发起者"));
        recongnizedArgs.put("reject",new Argument("-R","--reject","运行驳回测试，并指定驳回间隔长度"));
        recongnizedArgs.put("sequence",new Argument("-s","--sequence","流程审核者队列，使用，隔开"));
        recongnizedArgs.put("parameter",new Argument("-P","--parameter","指定参数文件进行驱动程序"));
        recongnizedArgs.put("lineIndex",new Argument("-l","--line","参数行指针，若指定参数文件时可选,默认0"));
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
        Argument ar = recongnizedArgs.get(id);
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
        Set<String> keys = recongnizedArgs.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            Argument ar = recongnizedArgs.get(it.next());
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
