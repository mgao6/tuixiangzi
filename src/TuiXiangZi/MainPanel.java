package TuiXiangZi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Stack;

/**
 * @author GaoMing
 * @date 2021/10/3 - 17:12
 */
public class MainPanel extends JPanel implements KeyListener
{
    int max=50;          // 关卡数，一共50关
    int[][] map;         // map数组不同位置的数值不同，其值对应pic文件夹中图片的序号，并通过379行drawImage方法画到面板上，小人移动时
    // 其值会根据相应逻辑更新，并通过repaint方法更新面板，由于更新逻辑类似，仅以moveup()方法为例做了注释
    int[][] maptmp;      // 该数组记录地图最初的情况，其值不会随小人移动而变化
    int manX,manY;       // 人小所在位置的坐标
    Image[] myImage;     // 用于存储pic文件夹下的照片
    ReadMap Levelmap;    // 关卡地图对象，会被getmap()方法解析为map数组对象
    ReadMap Levelmaptmp; // 临时关卡地图对象，会被getmap()方法解析为maptmp数组对象
    int len=30;
    public int level=1;  // 当前关卡数
    Stack mystack=new Stack();  // 用于存储小人移动的情况，根据上下左右移动的情况，以及移动时相应路线上箱子等的情况，存在不同的
    // 状态符号数，将该符号数压入栈中，以便回撤时，根据状态符号数执行相应的相反操作

    MainPanel()
    {
        setBounds(15,50,600,600);
        setBackground(Color.white);
        addKeyListener(this);
        myImage=new Image[10];
        for(int i=0; i<10; i++)
        {
            myImage[i] = Toolkit.getDefaultToolkit().getImage("pic\\"+i+".gif"); // 将pic文件夹下0-9的图片读入数组
        }

        setVisible(true);
    }

    void Tuixiangzi(int i)
    {
        Levelmap=new ReadMap(i);     // 根据关数生成地图
        Levelmaptmp=new ReadMap(i);
        map=Levelmap.getmap();       // 将地图转化为二维数组
        manX=Levelmap.getmanX();
        manY=Levelmap.getmanY();     // 获得小人初始坐标
        maptmp=Levelmaptmp.getmap();
        repaint();					 // 根据小人移动，更新地图
    }
    int maxlevel(){return max;}

    public void paint(Graphics g)    // 将图片加载到面板上
    {
        for(int i=0; i<20; i++)
            for(int j=0; j<20; j++)
            {
                g.drawImage(myImage[map[j][i]],i*len,j*len,this);
            }
        g.setColor(new Color(255,255,255));
        g.setFont(new Font("楷体_2312",Font.BOLD,30));
        g.drawString("第",240,40);
        g.drawString(String.valueOf(level),310,40);
        g.drawString("层",360,40);
    }

    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode()==KeyEvent.VK_UP){moveup();}
        if(e.getKeyCode()==KeyEvent.VK_DOWN){movedown();}
        if(e.getKeyCode()==KeyEvent.VK_LEFT){moveleft();}
        if(e.getKeyCode()==KeyEvent.VK_RIGHT){moveright();}
        if(iswin())
        {
            if(level==max){JOptionPane.showMessageDialog(this, "恭喜您通过最后一关！！！");}
            else
            {
                String msg="恭喜您通过第"+level+"关!!!\n是否要进入下一关？";
                int type=JOptionPane.YES_NO_OPTION;
                String title="恭喜过关！";
                int choice=0;
                choice=JOptionPane.showConfirmDialog(null,msg,title,type);
                if(choice==1)System.exit(0);
                else if(choice==0)
                {
                    level++;
                    Tuixiangzi(level);
                }
            }
            mystack.removeAllElements();
        }
    }
    public void keyTyped(KeyEvent e){}
    public void keyReleased(KeyEvent e){}

    boolean isMystackEmpty(){return mystack.isEmpty();}

    int  back(){return (Integer)mystack.pop();}

    void remove(){mystack.removeAllElements();}
    /*
    推箱子小人移动时，需要更新相应位置的图片，maptmp和map数组中存储的数字
    会被379行 graphics.drawImage方法调用生成相应的图片，再通过repaint方法更新mainpanel
    map数组值1 2 3 4 5的含义见857行
     */
    void moveup() // 向上移动
    {
        if(map[manY-1][manX]==2||map[manY-1][manX]==4) // 如果要移动的地方可以移动或者是箱子的目的地
        {
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9) // 如果原始地图中该位置是箱子的目的地，或者箱子已经放在目的地了
                map[manY][manX]=4; // 小人往上移动或者推箱子，这个地方会更新成箱子的目的地
            else map[manY][manX]=2; // 否则表示该位置可移动
            map[manY-1][manX]=8;    // 将该位置更新为图片8
            repaint();manY--;mystack.push(10);  // 更新画板，更新Y方向位置坐标，将该操作压入栈中，且该状态符号数为10，用于回撤
        }
        else if(map[manY-1][manX]==3)   // 如果要移动的地方是箱子
        {
            if(map[manY-2][manX]==4)    // 如果向上两格是箱子的目的地
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;  // 更新小人移动后，该位置的图片，逻辑同上
                map[manY-1][manX]=8;     // 更新小人图片
                map[manY-2][manX]=9;	 // 表示箱子移动到了目的地
                repaint();manY--;mystack.push(11);  // 该操作状态符号数为11
            }
            else if(map[manY-2][manX]==2)   // 如果向上两格是可以移动
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;  // 更新小人移动后，该位置的图片，逻辑同上
                map[manY-1][manX]=8;     // 更新小人图片
                map[manY-2][manX]=3;     // 目前该位置是箱子
                repaint();manY--;mystack.push(11);
            }
            else {map[manY][manX]=8;repaint();}  // 否则，上两格可能是墙或者箱子，则只更新该位置的图片，由于没有有效移动，没有符号数压入栈
        }
        else if(map[manY-1][manX]==9)    // 如果要移动的地方是箱子已在目的地
        {
            if(map[manY-2][manX]==4)     // 如果上两格也是箱子的目的地
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;  // 更新小人移动后，该位置的图片，逻辑同上
                map[manY-1][manX]=8;
                map[manY-2][manX]=9;
                repaint();manY--;mystack.push(11);
            }
            else if(map[manY-2][manX]==2)
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY-1][manX]=8;
                map[manY-2][manX]=3;
                repaint();manY--;mystack.push(11);
            }
            else {map[manY][manX]=8;repaint();}
        }
        if(map[manY-1][manX]==1)     // 如果要移动的地方是墙，则只更新该位置小人的图片
        {
            map[manY][manX]=8;repaint();
        }
    }

    void backup(int t)
    {
        int n=t;
        if(n==10)    // 目的地可以移动
        {
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
            {
                map[manY][manX]=4;
            }
            else map[manY][manX]=2;
        }
        else if(n==11)  // 移动的是箱子
        {
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)  // 此时的manY是移动后的坐标，对应的是moveup中manY-1
            {
                map[manY][manX]=9;    // 因为状态符号数11表示该操作移动了箱子，所以，该位置原先箱子已在目的地
            }
            else map[manY][manX]=3;
            if(maptmp[manY-1][manX]==4||maptmp[manY-1][manX]==9)
            {
                map[manY-1][manX]=4;
            }
            else map[manY-1][manX]=2;
        }
        map[manY+1][manX]=8;
        repaint();manY++;
    }

    void movedown()
    {
        if(map[manY+1][manX]==2||map[manY+1][manX]==4)
        {
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                map[manY][manX]=4;
            else map[manY][manX]=2;
            map[manY+1][manX]=5;
            repaint();manY++;mystack.push(20);
        }
        else if(map[manY+1][manX]==3)
        {
            if(map[manY+2][manX]==4)
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY+1][manX]=5;
                map[manY+2][manX]=9;
                repaint();manY++;mystack.push(21);
            }
            else if(map[manY+2][manX]==2)
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY+1][manX]=5;
                map[manY+2][manX]=3;
                repaint();manY++;mystack.push(21);
            }
            else {map[manY][manX]=5;repaint();}
        }
        else if(map[manY+1][manX]==9)
        {
            if(map[manY+2][manX]==4)
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY+1][manX]=5;
                map[manY+2][manX]=9;
                repaint();manY++;mystack.push(21);
            }
            else if(map[manY+2][manX]==2)
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY+1][manX]=5;
                map[manY+2][manX]=3;
                repaint();manY++;mystack.push(21);
            }
            else {map[manY][manX]=5;repaint();}
        }
        else if(map[manY+1][manX]==1)
        {
            map[manY][manX]=5;repaint();
        }
    }

    void backdown(int t)
    {
        int n=t;
        if(n==20)
        {
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
            {
                map[manY][manX]=4;
            }
            else map[manY][manX]=2;
        }
        else if(n==21)
        {
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
            {
                map[manY][manX]=9;
            }
            else map[manY][manX]=3;
            if(maptmp[manY+1][manX]==4||maptmp[manY+1][manX]==9)
            {
                map[manY+1][manX]=4;
            }
            else map[manY+1][manX]=2;
        }
        map[manY-1][manX]=5;
        repaint();manY--;
    }

    void moveleft()
    {
        if(map[manY][manX-1]==2||map[manY][manX-1]==4)
        {
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                map[manY][manX]=4;
            else map[manY][manX]=2;
            map[manY][manX-1]=6;
            repaint();manX--;mystack.push(30);
        }
        else if(map[manY][manX-1]==3)
        {
            if(map[manY][manX-2]==4)
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY][manX-1]=6;
                map[manY][manX-2]=9;
                repaint();manX--;mystack.push(31);
            }
            else if(map[manY][manX-2]==2)
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY][manX-1]=6;
                map[manY][manX-2]=3;
                repaint();manX--;mystack.push(31);
            }
            else {map[manY][manX]=6;repaint();}
        }
        else if(map[manY][manX-1]==9)
        {
            if(map[manY][manX-2]==4)
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY][manX-1]=6;
                map[manY][manX-2]=9;
                repaint();manX--;mystack.push(31);
            }
            else if(map[manY][manX-2]==2)
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY][manX-1]=6;
                map[manY][manX-2]=3;
                repaint();manX--;mystack.push(31);
            }
            else {map[manY][manX]=6;repaint();}
        }
        else if(map[manY][manX-1]==1)
        {
            map[manY][manX]=6;repaint();
        }
    }

    void backleft(int t)
    {
        int n=t;
        if(n==30)
        {
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
            {
                map[manY][manX]=4;
            }
            else map[manY][manX]=2;
        }
        else if(n==31)
        {
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
            {
                map[manY][manX]=9;
            }
            else map[manY][manX]=3;
            if(maptmp[manY][manX-1]==4||maptmp[manY][manX-1]==9)
            {
                map[manY][manX-1]=4;
            }
            else map[manY][manX-1]=2;
        }
        map[manY][manX+1]=6;
        repaint();manX++;
    }

    void moveright()
    {
        if(map[manY][manX+1]==2||map[manY][manX+1]==4)
        {
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                map[manY][manX]=4;
            else map[manY][manX]=2;
            map[manY][manX+1]=7;
            repaint();manX++;mystack.push(40);
        }
        else if(map[manY][manX+1]==3)
        {
            if(map[manY][manX+2]==4)
            {
                if(maptmp[manY][manX]==4)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY][manX+1]=7;
                map[manY][manX+2]=9;
                repaint();manX++;mystack.push(41);
            }
            else if(map[manY][manX+2]==2)
            {
                if(maptmp[manY][manX]==4)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY][manX+1]=7;
                map[manY][manX+2]=3;
                repaint();manX++;mystack.push(41);
            }
            else {map[manY][manX]=7;repaint();}
        }
        else if(map[manY][manX+1]==9)
        {
            if(map[manY][manX+2]==4)
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY][manX+1]=7;
                map[manY][manX+2]=9;
                repaint();manX++;mystack.push(41);
            }
            else if(map[manY][manX+2]==2)
            {
                if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
                    map[manY][manX]=4;
                else map[manY][manX]=2;
                map[manY][manX+1]=7;
                map[manY][manX+2]=3;
                repaint();manX++;mystack.push(41);
            }
            else {map[manY][manX]=7;repaint();}
        }
        else if(map[manY][manX+1]==1)
        {
            map[manY][manX]=7;repaint();
        }
    }

    void backright(int t)
    {
        int n=t;
        if(n==40)
        {
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
            {
                map[manY][manX]=4;
            }
            else map[manY][manX]=2;
        }
        else if(n==41)
        {
            if(maptmp[manY][manX]==4||maptmp[manY][manX]==9)
            {
                map[manY][manX]=9;
            }
            else map[manY][manX]=3;
            if(maptmp[manY][manX+1]==4||maptmp[manY][manX+1]==9)
            {
                map[manY][manX+1]=4;
            }
            else map[manY][manX+1]=2;
        }
        map[manY][manX-1]=7;
        repaint();manX--;
    }

    boolean iswin()
    {
        boolean num=false;
        out: for(int i=0; i<20; i++)
             for (int j = 0; j < 20; j++) {
                if (maptmp[i][j] == 4 || maptmp[i][j] == 9)
                    if (map[i][j] == 9) num = true;
                    else {
                        num = false;
                        break out; // 跳出最外层循环
                    }
            }


        return num;
    }
}