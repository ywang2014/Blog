package create;

public class Car {
    private String type;
    private String color;
    private String region;
    private Double length;
    private Double width;
    private Double weight;
    private Double price;

    public Car(CarBuilder builder) {
        this.type = builder.type;
        this.color = builder.color;
        this.region = builder.region;
        this.length = builder.length;
        this.width = builder.width;
        this.weight = builder.weight;
        this.price = builder.price;
    }


    public static class CarBuilder {
        private String type;
        private String color;
        private String region;
        private Double length;
        private Double width;
        private Double weight;
        private Double price;

        public CarBuilder addType(String type) {
            this.type = type;
            return this;
        }

        public CarBuilder addColor(String color) {
            this.color = color;
            return this;
        }

        public CarBuilder addRegion(String region) {
            this.region = region;
            return this;
        }

        public CarBuilder addLength(Double length) {
            this.length = length;
            return this;
        }

        public CarBuilder addWidth(Double width) {
            this.width = width;
            return this;
        }

        public CarBuilder addWeight(Double weight) {
            this.weight = weight;
            return this;
        }

        public CarBuilder addPrice(Double price) {
            this.price = price;
            return this;
        }

        public Car build() {
            return new Car(this);
        }
    }
}
