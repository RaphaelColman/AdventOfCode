import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.awt.*;

@Data
@RequiredArgsConstructor
public class Cart {
    @NonNull private Direction facing;
    @NonNull private Point location;
}
