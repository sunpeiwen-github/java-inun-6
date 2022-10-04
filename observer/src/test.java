import java.util.ArrayList;

public class test {
    public static void main(String args[])
    {
        ConcreteAllyControlCenter acc=new ConcreteAllyControlCenter("控制中心");
        Observer play1,play2,play3;
        play1=new Player("玩家1");
        play2=new Player("玩家2");
        play3=new Player("玩家3");
        acc.join(play1);
        acc.join(play2);
        acc.join(play3);
        play3.beAttacked(acc);
    }
}
interface  Observer
{
    String getName();
    void help();
    void beAttacked(AllyControlCenter acc);
}
class Player implements Observer
{
    String name;
    Player(String name){
        this.name=name;
    }

    public String getName(){
        return name;
    }
    public void help(){
        System.out.println(this.getName()+"支援");
    }
    public void beAttacked(AllyControlCenter acc){
        acc.notifyObserver(name);
    }
}
abstract class AllyControlCenter{//需要实例化链表
    String allyName;
    ArrayList<Observer> players;
    AllyControlCenter()//子类继承抽象类时，抽象类的构造函数不会被覆盖。 而且，在实例化子类对象时首先调用的是抽象类中的构造函数再调用子类中的。
    {
        players=new ArrayList<Observer>();
    }
    void setAllyName(String allyName){
        this.allyName=allyName;
    }
    String getAllyName(){
        return allyName;
    }
    void join(Observer observer){
        players.add(observer);
    }
    void quit(Observer observer){
        players.remove(observer);
    }
    void notifyObserver(String name){
    }
}
class ConcreteAllyControlCenter extends AllyControlCenter
{
    ConcreteAllyControlCenter(String allyName)
    {
        this.allyName=allyName;
    }
    void notifyObserver(String name){
        System.out.println(name+"被攻击");
        for(Object obs:players)
        {
            if(!((Observer)obs).getName().equalsIgnoreCase(name))
            {
                ((Observer) obs).help();
            }
        }
    }
}