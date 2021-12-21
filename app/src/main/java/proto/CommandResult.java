package proto;

public class CommandResult {
    int code;
    String text;
    CommandResult (int code, String text){
        this.code = code;
        this.text = text;
    };
    CommandResult(int code){
        this.code = code;
        this.text = "";
    };
    @Override
    public String toString(){
        return "Command Result code:" + code + ":" + text;
    }

    public static CommandResult ok(){
        return new CommandResult(0, "OK");
    } 
    public static CommandResult error(String text ){
        return new CommandResult(1, text);
    }
}
