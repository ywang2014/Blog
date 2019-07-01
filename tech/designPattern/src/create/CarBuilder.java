package create;

/**
 * 当需要构建不同风格的对象，同时需要避免构造器重叠时使用生成器模式。
 *      动态添加属性
 * 与工厂模式的主要区别在于：当创建过程一步到位时，使用工厂模式，而当创建过程需要多个步骤时，使用生成器模式。
 */
public class CarBuilder {
    private String type;
    private String color;
    private String region;
    private Double length;
    private Double width;
    private Double weight;
    private Double price;

    public CarBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public CarBuilder setColor(String color) {
        this.color = color;
        return this;
    }

    public CarBuilder setRegion(String region) {
        this.region = region;
        return this;
    }

    public CarBuilder setLength(Double length) {
        this.length = length;
        return this;
    }

    public CarBuilder setWidth(Double width) {
        this.width = width;
        return this;
    }

    public CarBuilder setWeight(Double weight) {
        this.weight = weight;
        return this;
    }

    public CarBuilder setPrice(Double price) {
        this.price = price;
        return this;
    }
}
