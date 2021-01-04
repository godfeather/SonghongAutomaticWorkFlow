package arguments;

public class Argument {
    public String __paramName = "";
    public String _paramName = "";
    public String help = "";
    public Argument(String _paramName, String __paramName, String help) {
        this.__paramName = __paramName;
        this._paramName = _paramName;
        this.help = help;
    }
}
