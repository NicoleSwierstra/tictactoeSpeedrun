package Graphics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Animation {

    public static Animation mainAnimation;

    public List<Node> animations;

    class Node {
        float t;
        public float speed;
        AnimationInterface ai, aiend;

        boolean doAnimation(float dt, Graphics g) {
            t -= speed * dt;
            
            if(t < 0) {
                if(aiend != null) aiend.doanim(0, g);
                return true;
            }
            ai.doanim(t, g);

            return false;
        }

        public Node(AnimationInterface animation, float seconds){
            ai = animation;
            speed = 1 / seconds;
            t = 1.0f;
        }

        public Node(AnimationInterface animation, AnimationInterface end, float seconds){
            ai = animation;
            aiend = end;
            speed = 1 / seconds;
            t = 1.0f;
        }
    }

    Animation(){
        animations = new ArrayList<Node>();
        mainAnimation = this;
    }

    static void Update(float dt, Graphics g){
        List<Node> toRemove = new ArrayList<>();

        for(Node n : mainAnimation.animations){
            if(n.doAnimation(dt, g))
                toRemove.add(n);
        }

        for (Node node : toRemove) {
            mainAnimation.animations.remove(node);
        }
    }

    public static void AddAnimation(AnimationInterface ai, float seconds){
        mainAnimation.addAnimation(ai, seconds);
    }

    void addAnimation(AnimationInterface ai, float seconds){
        animations.add(new Node(ai, seconds));
    }

    public static void AddAnimation(AnimationInterface ai, AnimationInterface aiend, float seconds){
        mainAnimation.addAnimation(ai, aiend, seconds);
    }

    void addAnimation(AnimationInterface ai, AnimationInterface aiend, float seconds){
        animations.add(new Node(ai, aiend, seconds));
    }
}
