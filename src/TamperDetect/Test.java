package TamperDetect;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class Test {

	public static void main(String[] args) {
//		int[] test=new int[1000000];
//		Random r=new Random();
//		for(int i=0;i<1000000;i++)
//		{
//			test[i]=r.nextInt(990000);
//		}
//		int[] test={1,2,2,5,3,4,3,4,9,6,4,4,8,0};
		A[] test={new A(1),new A(2),new A(1),new A(3)};
		long start=System.currentTimeMillis();   //获取开始时间  
		System.out.println(findRepeat(test).size()); 
		long end=System.currentTimeMillis(); //获取结束时间  
		System.out.println("程序运行时间： "+(end-start)+"ms");   
		
	}
	public static ArrayList<A> findRepeat(A[] array)
	{
		ArrayList<A> result=new ArrayList<A>();
		Set<A> ts=new TreeSet<A>(new Comparator<A>()
		{
			@Override
			public int compare(A o1, A o2) {
				// TODO Auto-generated method stub
				return o1.value-o2.value;
			}
		});
		for(A a:array)
		{
			if(!ts.add(a))
			{
				result.add(a);
			}
		}
		return result;
	}

}

class A
{
	static int index=0;
	int i;
	int value;
	public A(int x)
	{
		i=++index;
		value=x;
	}
}