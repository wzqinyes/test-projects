package cn.sinobest.ga;

import java.util.HashMap;

public class HashMapTest {

    public static void main(String[] args){
        HashMap<String, String> map = new HashMap<>();
        map.put("a", "aaa");

        int h;
        h = "abb".hashCode();
        System.out.println(h);
        int i = h ^ (h >>> 16);
        System.out.println(i);

        map.putIfAbsent("a", "aab");

        String a = map.get("a");

        System.out.println(a);
    }
}
