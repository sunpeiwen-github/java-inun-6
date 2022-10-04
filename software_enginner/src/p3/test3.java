package p3;

public class test3 {
    public static void main(String args[])
    {
        Single s1=Single.instance;
        Single s2=Single.instance;
        System.out.println("s1地址  "+s1.hashCode());
        System.out.println("s2地址  "+s2.hashCode());
        System.out.println("地址是否相等");
        System.out.println(s1==s2);
    }
}
enum Single{
    instance;
    void methods()
    {}
}

