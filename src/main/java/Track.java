import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class Track {
    @NonNull HashMap<Point, TrackPiece> hmPointToTrackPiece;
    Set<Cart> carts = new HashSet<>();

    public void addCart(Cart cart) {
        carts.add(cart);
    }
}
