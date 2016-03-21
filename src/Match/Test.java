package Match;

public class Test {

	public static void main(String[] args) 
	{
		System.out.println(NumberOf1(128));
	}
	
	public static int NumberOf1(long l)
	{
		int count = 0;
		long flag=1;
		while(flag!=0)
		{
			if((l&flag)!=0)
			{
				count++;
			}
			flag=flag<<1;
		}
		return count;
	}
}
