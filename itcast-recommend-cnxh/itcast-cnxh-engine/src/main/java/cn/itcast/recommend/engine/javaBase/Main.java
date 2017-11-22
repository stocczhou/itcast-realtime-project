package cn.itcast.recommend.engine.javaBase;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by maoxiangyi on 2017/11/19.
 */
public class Main {
    public static void main(String[] args) {
        //
        ArrayList<A> arrayList = new ArrayList<>();
        arrayList.add(new A());
        if(arrayList.contains(new A())){
            System.out.println("包含这个元素");
        }else {
            System.out.println("不包含这个元素");
        }

        ArrayList<B> arrayList1 = new ArrayList<B>();
        arrayList1.add(new B());
        if(arrayList1.contains(new B())){
            System.out.println("包含这个元素");
        }


        ArrayList<C> arrayList2 = new ArrayList<C>();
        arrayList2.add(new C());
        arrayList2.add(new C());
        Collections.sort(arrayList2);
    }
}
