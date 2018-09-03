package cn.pcm.exception;

public class Demo1 {
	
	public static void main(String[] args) {
		MyException ex = new MyException() ;
        System.out.println(ex.TestException());
	}
}
class MyException {
	public Boolean TestException() {
		boolean flag=true;
		
		try {
			int a=10/0;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			flag=false;
		}
		return flag;

	}
}
