package p3;

public class test2 {
    public static void main(String args[])
    {
        Hungry h1=Hungry.getInstance();
        Hungry h2=Hungry.getInstance();
        System.out.println("h1地址  "+h1.hashCode());
        System.out.println("h2地址  "+h2.hashCode());
        System.out.println("地址是否相等");
        System.out.println(h1==h2);
    }
}
class Hungry//懒汉
{
    private Hungry()
    {}
    private final static Hungry hungry=new Hungry();
    public static Hungry getInstance()
    {
        return hungry;
    }
}