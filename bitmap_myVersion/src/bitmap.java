import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/*此程序中学到了java图形界面通过多个jpanel和layout的组合的排版
以及jtable的直接使用setvalue 以及各部件类型是String时 通过“ ”+int来表示整形
还可以优化的时回收和分配的算法 使其不在同一柱面连续申请存储
而且可以当时不考虑指针的问题最后使用getavaiable函数即可分配两个集合
在界面的最左元素添加字号
 * */
class bitmap implements ActionListener{
    public static void main (String args[]){
        new bitmap();
    }
    JFrame jf;
    JScrollPane js1,js2;
    JTextArea jTextArea;
    JTable jTable;
    JTextField jt1,jt2,jt3;
    JButton b1,b2,b3,b4;
    JPanel p1,p2,p3,p4,p5,p6,p7;
    JLabel l1,l2,l3,l4,l5;
    String map[][];
    String column[];
    int available[];//存放的是相对块号
    int used[];
    int a_pointer;//指向当前数组位置
    int u_pointer;
    int numOfAvailable;
   public bitmap()
    {
        map=new String[40][17];
        column=new String[17];
        column[0] = "";
        for (int i = 1; i < 17; i++) {
            column[i] = "" + (i-1);
        }
        jf=new JFrame("位示图");
        jTable=new JTable(map,column);
        js1=new JScrollPane(jTable);
        jTextArea=new JTextArea(10,28);
        js2=new JScrollPane(jTextArea);

        jt1=new JTextField(6);
        jt2=new JTextField(6);
        jt3=new JTextField(6);

        p1=new JPanel(new BorderLayout());
        b1=new JButton("位示图初始化");
        b2=new JButton("盘块全部回收");
        b1.addActionListener( this);
        b2.addActionListener( this);
        p2=new JPanel(new FlowLayout());
        l1=new JLabel("位示图");
        p2.add(b1);
        p2.add(b2);
        p1.add(l1,BorderLayout.NORTH);
        p1.add(js1,BorderLayout.CENTER);
        p1.add(p2,BorderLayout.SOUTH);

        p3=new JPanel(new BorderLayout());
        p4=new JPanel(new FlowLayout());
        l2=new JLabel("剩余盘块数量");
        l3=new JLabel("运行状况");
        p4.add(l2);
        p4.add(jt1);
        p3.add(l3,BorderLayout.NORTH);
        p3.add(js2,BorderLayout.CENTER);
        p3.add(p4,BorderLayout.SOUTH);

        p5=new JPanel(new BorderLayout());
        p6=new JPanel(new FlowLayout());
        p7=new JPanel(new FlowLayout());
        l4=new JLabel("申请盘块的数量");
        l5=new JLabel("申请回收的块号");
        b3=new JButton("确认分配");
        b4=new JButton("确认回收");
        b3.addActionListener(this);
        b4.addActionListener(this);
        p6.add(l4);
        p6.add(jt2);
        p6.add(b3);
        p7.add(l5);
        p7.add(jt3);
        p7.add(b4);
        p5.add(p6,BorderLayout.CENTER);
        p5.add(p7,BorderLayout.SOUTH);

        jf.setLayout(new BorderLayout());
        jf.add(p1,BorderLayout.WEST);
        jf.add(p3,BorderLayout.CENTER);
        jf.add(p5,BorderLayout.SOUTH);

        jf.pack();
        jf.setVisible(true);
    }
    void init()
    {
        String str;
        int tem;
        jTextArea.append("初始化\n可利用空间有\n");
        for(int i=0; i < 40;i++)
        {
            map[i][0]=""+i;
            jTable.setValueAt(""+i ,i ,0);
        }
        for(int i=0;i < 40;i++)
            for(int j=1;j < 17;j++)
            {
                tem=(int) (Math.random()*2);//随机数0或1
                map[i][j] = "" + tem;
                jTable.setValueAt(""+tem ,i ,j);
                if(tem==0){
                str="柱面--"+(i)+"--磁道--"+(j-1)/4+"--扇区--"+(j-1)%4 + "--相对块号--" + (i * 16 +j-1)  +"\n";
                jTextArea.append(str);
                }
            }
        getAvailable();
    }
    void getAvailable(){//分别将已用和未用的相对块号存储
       available=new int[640];
       used=new int[640];
       a_pointer=0;
       u_pointer=0;
       int a;
       for(int i = 0;i < 40; i++)
           for(int j = 1;j < 17; j++) {
               a = i * 16 + j-1;
               if(map[i][j].equals("0")) {
                   available[a_pointer]= a;
                   a_pointer++;
                   if(a_pointer == 640)//指针不能越界
                       a_pointer--;
               }
               else{
                   used[u_pointer] = a;
                   u_pointer++;
                   if(u_pointer == 640)
                       u_pointer--;
               }
           }

    }
    void recycle_all(){
       int tem;
       for(int i = 0;i < u_pointer; i++){
           tem =used[i];
           map[tem/16][tem%16+1]=""+0;
       }
       u_pointer=0;
       for(int i = 0; i < 40;i++)
           for(int j = 0;j < 16;j++){
               jTable.setValueAt(""+0,i,j+1);
           }
       jTextArea.append("已经初始化\n");
       getAvailable();
    }

    void search()
    {
        int num =Integer.parseInt(jt2.getText());
        int p;
        String str;
        jTextArea.append("申请的盘块为\n");
        if( num <= a_pointer) {
            int count=0;
            p=0;
           while(count<num){
               for(int i=1;i<17;i++){//每行选一个空闲的位置，找到后从下一行开始找
                   if(map[p][i].equals("0")){
                       map[p][i] = "" + 1;
                       jTable.setValueAt("" + 1, p, i);
                       str = "申请 柱面--" + p + "--磁道--" + (i-1) / 4 + "--扇区--" + (i-1) % 4 + "--相对块号--" + (16*p+i-1) + "\n";
                       jTextArea.append(str);
                       //操作
                       count++;
                       break;
                   }
               }
               p=(p+1)%40;
           }
        }
        else {
            jTextArea.append("内存不足，剩余空间"+a_pointer+"\n");
        }
        getAvailable();
    }
    void recycle(){//输入相对块号 输出相应的柱面 磁道 扇区
        int tem =Integer.parseInt(jt3.getText());
        int a,b;
        String str;
        a = tem / 16;
        b = (tem % 16);
            if(map[a][b+1].equals("1")){
            map[a][b+1] = "" + 0;
            jTable.setValueAt(""+0,a,b+1);
            str="回收 柱面--"+a+"--磁道--"+(tem % 16) /4+"--扇区--"+(tem % 16) % 4 + "--相对块号--"+ tem +"\n";
            jTextArea.append(str);

            available[a_pointer] = tem;
            a_pointer++;
            u_pointer--;
            }
            else{
                jTextArea.append("此空间未被占用，不能回收");
            }
    }
    public void actionPerformed(ActionEvent e){
       if(e.getSource() == b1) {
           init();
           jt1.setText(""+(a_pointer));
       }
       else if(e.getSource() == b2){
           recycle_all();
           jt1.setText(""+(a_pointer));
       }
       else if(e.getSource() == b3){
//            apply();
           search();
           jt1.setText(""+(a_pointer));
       }
       else if(e.getSource() == b4){
            recycle();
           jt1.setText(""+(a_pointer));
       }
//       jTextArea.append("a"+a_pointer+"  u"+u_pointer+"\n");

    }
}