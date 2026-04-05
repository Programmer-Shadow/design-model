package simplefactory.model;

public class Circle implements Shape {
    private final double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public String describe() {
        return "圆形：半径=" + radius;
    }

    @Override
    public String name() { return "Circle"; }
}
