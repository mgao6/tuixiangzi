package TuiXiangZi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author GaoMing
 * @date 2021/10/3 - 17:13
 */
public class ReadMap {
    private int level,mx,my;
    private int[][] mymap=new int[20][20];
    FileReader r;
    BufferedReader br;
    String bb="";
    int[] x;int c=0;
    ReadMap(int k)
    {
        level=k;
        String s;
        try
        {
            File f=new File("maps\\"+level+".map");
            r=new FileReader(f);
            br=new BufferedReader(r); // 从缓冲区中读取字符流，提高效率
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        try
        {
            while ((s=br.readLine())!=null)
            {
                bb=bb+s;

            }
        }
        catch (IOException g)
        {
            System.out.println(g);
        }
        byte[] d=bb.getBytes();
        int len=bb.length();
        int[] x=new int[len];
        for(int i=0;i<bb.length();i++)x[i]=d[i]-48;   //ASCII表的零是第48个
		/*
		地图中数字的含义：
		0 外围修饰区域
		1 墙
		2 该位置箱子可移动
		3 箱子
		4 箱子的目的地
		5 小人起始位置
		 */
        for(int i=0;i<20;i++)
        {
            for(int j=0;j<20;j++)
            {
                mymap[i][j]=x[c];
                if(mymap[i][j]==5)
                {
                    mx=j;my=i;
                }
                c++;
            }
        }
    }
    int[][] getmap(){return mymap;}
    int getmanX(){return mx;}
    int getmanY(){return my;}
}
