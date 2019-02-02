import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.awt.*;

@Data
@RequiredArgsConstructor
class Cart {
    @NonNull private Direction facing;
    @NonNull private Point location;
}
