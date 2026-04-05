package simplefactory.model;

public class Triangle implements Shape {
    private final double base;
    private final double height;

    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }

    @Override
    public double area() {
        return 0.5 * base * height;
    }

    @Override
    public String describe() {
        return "三角形：底=" + base + "，高=" + height;
    }

    @Override
    public String name() { return "Triangle"; }
}
