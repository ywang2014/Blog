package create;

import java.io.*;

public class PrototypeSheep implements Cloneable, Serializable {
    private static final long serialVersionUID = 6912171851574549999L;

    private String name;
    private int age;
    private Object category;

    // 浅复制
    public Object clone() throws CloneNotSupportedException {
        return (PrototypeSheep) super.clone();
    }

    // 深复制
    // 要实现深复制，需要采用流的形式读入当前对象的二进制输入，再写出二进制数据对应的对象。
    public Object deepClone() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(this);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(in);
        return ois.readObject();
    }
}
