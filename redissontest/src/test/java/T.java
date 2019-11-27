import java.util.regex.Pattern;

public class T {

    public static void main(String[] args){
        String value = "44%1__1%";
        String v = value.replaceAll("%", ".*").replace("_", ".{1}");
        System.out.println(v);
        Pattern compile = Pattern.compile(v);
        System.out.println(compile.matcher("4401121540000").matches());
    }
}
