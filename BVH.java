import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class BVH implements Iterable<Circle>{
    Box boundingBox;
    BVH child1;
    BVH child2;
    Circle containedCircle;

    // todo for students
    public BVH(ArrayList<Circle> circles) {
        if (circles.size() == 1){
            boundingBox = circles.get(0).getBoundingBox();
            child1 = null;
            child2 = null;
            containedCircle = circles.get(0);
        }
        else {
            boundingBox = buildTightBoundingBox(circles);
            child1 = new BVH(split(circles, boundingBox)[0]);
            child2 = new BVH(split(circles, boundingBox)[1]);
            containedCircle = null;
        }

    }

    public void draw(Graphics2D g2) {
        this.boundingBox.draw(g2);
        if (this.child1 != null) {
            this.child1.draw(g2);
        }
        if (this.child2 != null) {
            this.child2.draw(g2);
        }
    }

    // todo for students
    public static ArrayList<Circle>[] split(ArrayList<Circle> circles, Box boundingBox) {
        ArrayList<Circle> lessThanMid = new ArrayList<Circle>();
        ArrayList<Circle> moreThanMid = new ArrayList<Circle>();
        if (boundingBox.getHeight() >= boundingBox.getWidth()){
            for (Circle this_circle:circles){ // split across y axis
                if (this_circle.position.y <= boundingBox.getMidY()){ // arbitrarily define the smaller dimension to contain the circles equal to midpoint
                   lessThanMid.add(this_circle);
                }
                else {
                    moreThanMid.add(this_circle);
                }
            }
        }
        else {
            for (Circle this_circle:circles){
                if (this_circle.position.x <= boundingBox.getMidX()){
                    lessThanMid.add(this_circle);
                }
                else {
                    moreThanMid.add(this_circle);
                }
            }
        }
        ArrayList<Circle>[] children = new ArrayList[] {lessThanMid, moreThanMid};
        return children;
    }

    // returns the smallest possible box which fully encloses every circle in circles
    public static Box buildTightBoundingBox(ArrayList<Circle> circles) {
        Vector2 bottomLeft = new Vector2(Float.POSITIVE_INFINITY);
        Vector2 topRight = new Vector2(Float.NEGATIVE_INFINITY);

        for (Circle c : circles) {
            bottomLeft = Vector2.min(bottomLeft, c.getBoundingBox().bottomLeft);
            topRight = Vector2.max(topRight, c.getBoundingBox().topRight);
        }

        return new Box(bottomLeft, topRight);
    }

    // METHODS BELOW RELATED TO ITERATOR

    // todo for students
    @Override
    public Iterator<Circle> iterator() {
        return new BVHIterator(this);
    }

    public class BVHIterator implements Iterator<Circle> {
        Stack<Circle> circles = new Stack<>();

        // todo for students
        public BVHIterator(BVH bvh) {
            iteratorHelper(bvh);
        }

        public void iteratorHelper(BVH bvh){
            if (bvh.child1==null && bvh.child2==null){
                circles.push(bvh.containedCircle);
            }
            else {
                iteratorHelper(bvh.child1);
                iteratorHelper(bvh.child2);
            }
        }

        // todo for students
        @Override
        public boolean hasNext() {
            return !(circles.empty());
        }

        // todo for students
        @Override
        public Circle next() {
            return circles.pop();
        }
    }
}