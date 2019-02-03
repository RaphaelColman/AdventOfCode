import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
public class CartTracker {
    Set<Cart> carts = new HashSet<Cart>();


    void addCart(Cart cart) {
        carts.add(cart);
    }
}
