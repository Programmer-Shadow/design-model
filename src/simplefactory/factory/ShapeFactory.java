package simplefactory.factory;

import simplefactory.model.Circle;
import simplefactory.model.Rectangle;
import simplefactory.model.Shape;
import simplefactory.model.Triangle;

/**
 * 简单工厂：一个类、一个方法、一个 switch，根据参数创建不同对象。
 * 优点：调用方不需要知道具体类名。
 * 缺点：新增图形必须改这个 switch（违反开闭原则）。
 */
public class ShapeFactory {

    public static Shape create(String type, double param1, double param2) {
        switch (type) {
            case "circle":    return new Circle(param1);
            case "rectangle": return new Rectangle(param1, param2);
            case "triangle":  return new Triangle(param1, param2);
            default:          throw new IllegalArgumentException("未知图形类型: " + type);
        }
    }
}
