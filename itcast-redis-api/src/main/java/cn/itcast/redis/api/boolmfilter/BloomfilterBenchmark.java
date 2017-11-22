package cn.itcast.redis.api.boolmfilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BloomfilterBenchmark {
    static int elementCount = 500000; // Number of elements to test

    public static void printStat(long start, long end) {
        double diff = (end - start) / 1000.0;
        System.out.println(diff + "s, " + (elementCount / diff) + " elements/s");
    }

    public static void main(String[] argv) {
        final Random r = new Random();

        // 创建50000个元素，用来添加到过滤器中
        List<String> existingElements = new ArrayList(elementCount);
        for (int i = 0; i < elementCount; i++) {
            byte[] b = new byte[200];
            r.nextBytes(b);
            existingElements.add(new String(b));
        }

        //创建50000个元素，用作比较
        List<String> nonExistingElements = new ArrayList(elementCount);
        for (int i = 0; i < elementCount; i++) {
            byte[] b = new byte[200];
            r.nextBytes(b);
            nonExistingElements.add(new String(b));
        }

        //设置一个空的布隆过滤器，设置命中高精度和预期存放的最大元素个数据
        //这个构造器能够，能够自动算出hash函数的次数
        BloomFilter<String> bf = new BloomFilter<String>(0.001, elementCount);

        //打印测试的元素个数
        System.out.println("Testing " + elementCount + " elements");
        //打印计算出来的最优hash次数
        System.out.println("k is " + bf.getK());

        //添加50w个元素需要3.24秒，平均每秒添加15w个元素。
        //add(): 3.24s, 154320.98765432098 elements/s
        System.out.print("第一回合：插入elementCount到过滤器中时间消耗 ");
        long start_add = System.currentTimeMillis();
        for (int i = 0; i < elementCount; i++) {
            //插入元素
            bf.add(existingElements.get(i));
        }
        long end_add = System.currentTimeMillis();
        printStat(start_add, end_add);

        System.out.print("第二回合：判断list(原始数据)被包含的时间");
        long start_containsAll = System.currentTimeMillis();
        for (int i = 0; i < elementCount; i++) {
            bf.contains(existingElements.get(i));
        }
        long end_containsAll = System.currentTimeMillis();
        printStat(start_containsAll, end_containsAll);

        System.out.print("第三回合：判断新的list中包含的元素");
        long start_ncontains = System.currentTimeMillis();
        for (int i = 0; i < elementCount; i++) {
            bf.contains(nonExistingElements.get(i));
        }
        long end_ncontains = System.currentTimeMillis();
        printStat(start_ncontains, end_ncontains);
    }
}
