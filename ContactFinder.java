import java.util.ArrayList;
import java.util.HashSet;

public class ContactFinder {
    // todo for students
    public static HashSet<ContactResult> individualResults = new HashSet<ContactResult>();
    // Returns a HashSet of ContactResult objects representing all the contacts between circles in the scene.
    // The runtime of this method should be O(n^2) where n is the number of circles.
    public static HashSet<ContactResult> getContactsNaive(ArrayList<Circle> circles) {
        HashSet<ContactResult> results = new HashSet<ContactResult>();
        for (Circle circle1:circles){
            for (Circle circle2:circles){
                if (circle1.isContacting(circle2) != null && circle1.id != circle2.id){
                    results.add(circle1.isContacting(circle2));
                }
            }
        }
        return results;
    }

    // todo for students
    // Returns a HashSet of ContactResult objects representing all the contacts between circles in the scene.
    // The runtime of this method should be O(n*log(n)) where n is the number of circles.
    public static HashSet<ContactResult> getContactsBVH(ArrayList<Circle> circles, BVH bvh) {
        HashSet<ContactResult> results = new HashSet<ContactResult>();
        for (Circle circle:circles){
            results.addAll(getContactBVH(circle,bvh));
            individualResults.clear();
        }
        return results;
    }

    // todo for students
    // Takes a single circle c and a BVH bvh.
    // Returns a HashSet of ContactResult objects representing contacts between c
    // and the circles contained in the leaves of the bvh.
    public static HashSet<ContactResult> getContactBVH(Circle c, BVH bvh) {
        if (bvh.child1==null && bvh.child2==null){
            Vector2 p1 = c.position;
            Vector2 p2 = bvh.containedCircle.position;
            if (c.id != bvh.containedCircle.id){
                ContactResult contacting = c.isContacting(bvh.containedCircle);
                if (contacting!=null){
                    individualResults.add(contacting);
                }
            }
        }
        else {
            BVH child1 = bvh.child1;
            BVH child2 = bvh.child2;
            double hyp_1 = Math.sqrt((child1.boundingBox.getHeight()/2)*(child1.boundingBox.getHeight()/2) + (child1.boundingBox.getWidth()/2)*(child1.boundingBox.getWidth()/2)) + c.radius;
            double hyp_2 = Math.sqrt((child2.boundingBox.getHeight()/2)*(child2.boundingBox.getHeight()/2) + (child2.boundingBox.getWidth()/2)*(child2.boundingBox.getWidth()/2)) + c.radius;
            if (Math.sqrt((child1.boundingBox.getMidX()-c.position.x)*(child1.boundingBox.getMidX()-c.position.x)+(child1.boundingBox.getMidY()-c.position.y)*(child1.boundingBox.getMidY()-c.position.y))
                    < hyp_1){
                getContactBVH(c,child1);
            }
            if (Math.sqrt((child2.boundingBox.getMidX()-c.position.x)*(child2.boundingBox.getMidX()-c.position.x)+(child2.boundingBox.getMidY()-c.position.y)*(child2.boundingBox.getMidY()-c.position.y))
                    < hyp_2){
                getContactBVH(c,child2);
            }
        }

        return individualResults;
    }
}

