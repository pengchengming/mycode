package cn.pcm.test;

public class Dome1 {

	static long sum(long n) {
		if (n == 1)
			return 1;
		return sum(n - 1) + n;

	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		System.out.println(sum(1000));
		long end = System.currentTimeMillis();
		long total = end - start;
		System.out.println("total:" + total);
	}
}
