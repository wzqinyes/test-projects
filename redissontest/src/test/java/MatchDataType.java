public enum MatchDataType {

    Text("文本"),

    StaticDict("静态字典"),

    DynamicDict("动态字典");

    //private final int code;     //直接用ordinal()当code值

    private final String detail;  //描述

    MatchDataType(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public static MatchDataType valueOf(int code) {
        for (MatchDataType matchDataType : MatchDataType.values()) {
            if (matchDataType.ordinal() == code) {
                return matchDataType;
            }
        }
        return Text;
    }

    public static void main(String[] args){
        System.out.println(StaticDict.ordinal());
        System.out.println(MatchDataType.valueOf(2));
    }

}