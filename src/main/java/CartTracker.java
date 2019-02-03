import lombok.Data;
import lombok.NonNull;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CartTracker {
    Set<Cart> carts = new HashSet<Cart>();


    void addCart(Cart cart) {
        carts.add(cart);
    }

    //UNTESTED!!!

    void moveCarts() {
        carts.forEach(cart -> cart.move());
    }

    List<Point> getCollisions() {

        HashMap<Point, Integer> pointToCountHm = new HashMap<>();
        carts.forEach(cart -> pointToCountHm.merge(cart.getLocation(), 1, Integer::sum));

        return pointToCountHm.keySet().stream().filter(point -> pointToCountHm.get(point) >= 2).collect(Collectors.toList());
    }
}
