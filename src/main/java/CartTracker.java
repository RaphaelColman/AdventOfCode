import lombok.Data;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class CartTracker {
    Set<Cart> carts = new HashSet<>();
    Map<Integer, Set<Point>> allCollisions = new HashMap<>();


    void addCart(Cart cart) {
        carts.add(cart);
    }


    void moveCarts(int tick) {

        ArrayList<Cart> carts = new ArrayList<>(this.carts);
        Collections.sort(carts, Comparator.comparingDouble((Cart cart) -> cart.getLocation().getY()).thenComparingDouble(cart -> cart.getLocation().getX()));

        carts.forEach(cart -> cart.move());
        Set<Point> collisions = getCollisions();

        if (collisions.size() > 0) {
            allCollisions.put(tick, collisions);
            System.out.println(String.format("Tick: %d, Collision: %s", tick, collisions));
        }

        this.carts = carts.stream().filter(cart -> !collisions.contains(cart.getLocation())).collect(Collectors.toSet());

        if (this.carts.size() == 1) {
            System.out.println(String.format("One cart left: %s", carts.iterator().next().toString()));
        }
    }

    Set<Point> getCollisions() {

        HashMap<Point, Integer> pointToCountHm = new HashMap<>();
        carts.forEach(cart -> pointToCountHm.merge(cart.getLocation(), 1, Integer::sum));

        return pointToCountHm.keySet().stream().filter(point -> pointToCountHm.get(point) >= 2).collect(Collectors.toSet());
    }

    public Point getFinalCartLocation() {
        int tick = 1;
        while (carts.size() > 1) {
            moveCarts(tick);
            tick++;
        }
        System.out.println("All Collisions: " + allCollisions.toString());
        return carts.iterator().next().getLocation();
    }
}
