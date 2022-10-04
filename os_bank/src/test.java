import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

/*

1 4
4 4
5 8

1 4
4 6
6 8

5 4

0 0 1 2 0 0 1 2
1 0 0 0 1 7 5 0
1 3 5 4 2 3 5 6
0 6 3 2 0 6 5 2
0 0 1 4 0 6 5 6

1 5 2 0
*/
public class test {
    public static void main(String args[]){
        GUI gui=new GUI();
    }
}
class GUI{
    JFrame jf;
    JPanel p1,p11,p2,p3,p12,p21,p22,p3main,p32,p4;
    JLabel l1,l2,l3,l4;
    JTextField t1,t2,t4;
    JTextArea jTextArea,jTextArea2;
    JScrollPane jScrollPane;
    JButton ok1;
    Banker banker;
    GUI()
    {
        jf=new JFrame("银行家算法");
        jf.setSize(550,400);
        jf.setLayout(new BorderLayout());

        p1=new JPanel(new BorderLayout());
        p11=new JPanel();
        p11.setLayout(new FlowLayout(FlowLayout.LEADING));
        l1=new JLabel("输入进程个数：");
        l2=new JLabel("输入资源种类：");
        l3=new JLabel("输入各进程已分配待分配(在同一行)：");
        l4=new JLabel("输入可分配资源：");
        t1=new JTextField(5);
        t2=new JTextField(5);
        jTextArea=new JTextArea(10,10);
        t4=new JTextField(10);
        p11.add(l1);
        p11.add(t1);
        p11.add(l2);
        p11.add(t2);

        p1.add(p11,BorderLayout.CENTER);
        jf.add(p1,BorderLayout.NORTH);

        p2=new JPanel(new FlowLayout());
        p2.add(l3);
        p2.add(jTextArea);
        p2.add(l4);
        p2.add(t4);
        jf.add(p2,BorderLayout.CENTER);

        p3=new JPanel(new FlowLayout());

        jTextArea2=new JTextArea(20,15);
        jScrollPane=new JScrollPane(jTextArea2);
        p3.add(jScrollPane);
        jf.add(p3,BorderLayout.EAST);

        p4=new JPanel(new FlowLayout());
        ok1=new JButton("确定");
        p4.add(ok1);
        jf.add(p4,BorderLayout.SOUTH);

        ok1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int processNum,typeNum;
                String jText3;
                String jText4;


                String[] resourceS;
                int[] resourceI;
                processNum=Integer.parseInt(t1.getText());
                typeNum=Integer.parseInt(t2.getText());
                jText3=jTextArea.getText();
                jText4=t4.getText();

                resourceS=jText4.split("\\s+");//空格分开
                resourceI=new int[typeNum];
                for(int i=0;i<resourceS.length;i++)
                {
                    resourceI[i]=Integer.parseInt(resourceS[i]);
                }

                String[][] result=new String[processNum][2*typeNum];
                int[][] allocation=new int[processNum][typeNum];
                int[][] max=new int[processNum][typeNum];

                String[] line=jText3.split("\\r?\\n");
                for(int i=0;i<line.length;i++)
                {
                    result[i]=line[i].split("\\s+");
                    for(int j=0;j<2*typeNum;j++)
                    {
                        if(j<typeNum)
                        {
                            allocation[i][j]=Integer.parseInt(result[i][j]);
                        }
                        else
                        {
                            max[i][j-typeNum]=Integer.parseInt(result[i][j]);
                        }
                    }
                }
                banker=new Banker(processNum,typeNum,max,allocation,resourceI);
                banker.initial();
                jTextArea2.append("是否安全");
                jTextArea2.append("\n");
                jTextArea2.append(Boolean.toString(banker.safe()));
                jTextArea2.append("\n");
                banker.initial();
                System.out.println(banker.safe());

                jTextArea2.append("安全序列有");
                jTextArea2.append("\n");
                banker.initial();
                banker.getAllSecuritySequence(0);
                for(int i=0;i<banker.num;i++) {
                    jTextArea2.append("序列"+(i+1)+"  ");
                    for(int j=0;j<processNum;j++)
                    {
                        jTextArea2.append(Integer.toString(banker.result[i][j]+1));
                        jTextArea2.append(" ");
                    }
                    jTextArea2.append("\n");
                }

            }
        });

//        jf.pack();
        jf.setDefaultCloseOperation(3);
        jf.setVisible(true);

    }}
class Banker{
    int processNum;
    int typeNum;
    int max[][];//最大需求
    int allocation[][];//已分配个数
    int need[][];//还需个数
    int resource[];//资源个数

    int work[];
    boolean finish[];
    Stack<Integer> stack=new Stack<>();
    int [][] result;
    int num=0;//序列的个数-1

    Banker(int processNum,int typeNum,int max[][],int allocation[][],int resource[])
    {
        this.processNum=processNum;
        this.typeNum=typeNum;
        this.max=max;
        this.allocation=allocation;
        this.resource=resource;
        this.result=new int[100][processNum];//???未知有多少个序列
        this.work=new int[typeNum];
        //need计算
        need=new int[processNum][typeNum];
        for(int i=0;i<processNum;i++){
            for(int j=0;j<typeNum;j++)
            {
                need[i][j]=max[i][j]-allocation[i][j];
            }
        }

    }
    boolean arrayCompare(int a[],int b[]){//如果数组a中元素都小于等于b中元素返回true
        boolean flag=true;
        for(int i=0;i<a.length;i++)
        {
            if(a[i]>b[i])
            {flag=false;
                break;}
        }
        return flag;
    }
    int [] arrayAdd(int a[],int b[])
    {
        for(int i=0;i<a.length;i++)
        {
            a[i]+=b[i];
        }
        return a;
    }
    int[] arraySub(int a[],int b[])
    {
        for(int i=0;i<a.length;i++){
            a[i]-=b[i];
        }
        return a;
    }
    void initial()
    {//初始化不能直接名字附上，这样地址相同原内容被更改  应该逐一赋值
        for(int i=0;i<work.length;i++)
        {
            work[i]=resource[i];
        }

        //初始化赋值work和finish
        finish=new boolean[processNum];
        for(int i=0;i<processNum;i++)
        {
            finish[i]=false;
        }
    }
    boolean safe(){//使用之前初始化

        for(int i=0;i<processNum;i++)
        {
            if(finish[i]==false&&arrayCompare(need[i],work))//i个process的每个资源都小于等于可提供的资源
            {
                work=arrayAdd(work,allocation[i]);
                finish[i]=true;
                System.out.println("进程"+i+"完成");
                i=-1;//从头开始遍历 循环提结束后会i会加1
            }
        }
        //判断是否每个的finish否是true；
        boolean flag=true;
        for(int i=0;i<processNum;i++)
        {
            if(finish[i]==false)
            {
                flag=false;
                break;
            }
        }
        return flag;
    }
    void getAllSecuritySequence(int k){//k是序列中元素个数 num是第几个序列
        if(k==processNum)//数量到达进程数将序列存储
        {
            for(int i=0;i<processNum;i++)
            {
                result[num][i]=stack.elementAt(i);
                System.out.println(i+"元素"+stack.elementAt(i));
            }
            System.out.println("序列"+num);
            num++;
        }
        for(int i=0;i<processNum;i++){
            if(finish[i]==false&&arrayCompare(need[i],work)){
                work=arrayAdd(work,allocation[i]);
                finish[i]=true;
                System.out.println("进程"+i+"完成");
                stack.push(i);
                getAllSecuritySequence(k+1);
                stack.pop();//回溯将其栈出 然后将资源也返还
                arraySub(work,allocation[i]);
                finish[i]=false;
            }
        }
    }
}