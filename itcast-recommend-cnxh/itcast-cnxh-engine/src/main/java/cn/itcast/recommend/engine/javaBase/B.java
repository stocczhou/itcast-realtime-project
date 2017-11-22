package cn.itcast.recommend.engine.javaBase;

/**
 * Created by maoxiangyi on 2017/11/19.
 */
public class B {
    private int id = 0;

    @Override
    public boolean equals(Object obj) {
        B b = (B) obj;
        return this.id == b.getNum();
    }

    public int getNum() {
        return id;
    }

    public void setNum(int num) {
        this.id = num;
    }
}
