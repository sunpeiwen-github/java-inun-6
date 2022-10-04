/*
* 可以用链表 方便排序删除
* 没有考虑提交时间大于当前时间的情况 该情况 currentTime应该变化*/
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Scanner;

public class test {
    public static void main(String args[])
    {

        Input input=new Input();
        Algorithm algorithm=new Algorithm(input.getWorks());
        algorithm.start();
    }
}


class Work{
    int tjTime;
    int yxTime;
    int workNum;
    Work(int workNUm,int tjTime,int yxTime)
    {
        this.tjTime=tjTime;
        this.yxTime=yxTime;
        this.workNum=workNUm;
    }
}
class Input{
    int count;
    Work[] works;
    Input()
    {
        Scanner sc=new Scanner(System.in);
        System.out.println("输入作业个数");
        count=sc.nextInt();
        works=new Work[count];
        System.out.println("提交时间 运行时间");
        for(int i=0;i<count;i++)
        {
            Work work=new Work(i+1,sc.nextInt(),sc.nextInt());
            works[i]=work;
        }
    }
    Work[] getWorks()
    {
        return works;
    }
}
class Algorithm
{
    Work[] works;
    int currentTime;
    Algorithm(Work[] works)
    {
        this.works=works;
    }
    void start()
    {
        float avg_zh=0;
        float avg_dazh=0;
        currentTime=works[0].tjTime;
        System.out.println("作业序号  "+"周转时间   "+"带权周转时间");
        System.out.println("1    "+works[0].yxTime+"   "+"1.0");
        avg_zh=works[0].yxTime;
        avg_dazh=1.0f;
        currentTime=getCurrentTime(works[0].yxTime);

        for(int i=1;i<works.length;i++)
        {
            float flag=100;
            int num=1;
            for(int j=1;j<works.length;j++)
            {
//                if(works[i].tjTime==works[i+1].tjTime)
//                {
//
//                }
                float r=getR(j);
                if(r<flag)
                {
                    flag=r;
                    num=j;
                }
            }
            float zh=works[num].yxTime+getWaitTime(works[num].tjTime);
            System.out.println(getWaitTime(works[num].tjTime));
            float dqzh=zh/works[num].yxTime;
            System.out.println(works[num].workNum+"   "+zh+"   "+dqzh);

            avg_dazh=avg_dazh+dqzh;
            avg_zh=avg_zh+zh;
            currentTime=getCurrentTime(works[num].yxTime);
            works[num].tjTime=2400;//运行完后将提交时间最大 应该剔除数组。
        }
        System.out.println("平均周转时间"+avg_zh/works.length+"      平均带权周转时间"+avg_dazh/works.length);
    }
    //运行过的还在数组里
    int getWaitTime(int tjTime)
    {
        int shi=(currentTime/100-tjTime/100);
        int ge=(currentTime%100-tjTime%100);
        return 60*shi+ge;
    }
    int getCurrentTime(int yxTime)
    {
        currentTime=yxTime%60+currentTime;
        currentTime=(yxTime/60)*100+currentTime;
        if(currentTime%100>=60)
        {
            currentTime=currentTime-60+100;//相加后大于60需要检验
        }
        return currentTime;
    }
    float getR(int i)
    {
        float r;
        int wait=getWaitTime(works[i].tjTime);
        if(wait<0)
            r=100;//wait<0说明当前时间还没到 该作业的提交时间 左移r取一个大的值
        else
        {
            r=1+((float)works[i].yxTime/wait);//要确保是浮点数
        }
        return r; }

}