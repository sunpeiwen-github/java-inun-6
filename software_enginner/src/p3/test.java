package p3;

public class test {
    public static void main(String args[])
    {
        Singleton s1=Singleton.getInstance();
        Singleton s2=Singleton.getInstance();
        System.out.println("s1地址  "+s1.hashCode());
        System.out.println("s2地址  "+s2.hashCode());
        System.out.println("地址是否相等");
        System.out.println(s1==s2);
    }
}
class Singleton//懒汉
{
    private Singleton()
    {}
    static Singleton singleton= null;
    public static Singleton getInstance()
    {
        if(singleton==null) {
            synchronized (Singleton.class) {
                if (singleton == null)
                    singleton = new Singleton();
            }
        }
     return singleton;
    }
}
