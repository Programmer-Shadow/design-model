package simplefactory.model;

public class Rectangle implements Shape {
    private final double width;
    private final double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public String describe() {
        return "矩形：宽=" + width + "，高=" + height;
    }

    @Override
    public String name() { return "Rectangle"; }
}
