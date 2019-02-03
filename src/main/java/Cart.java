import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.util.HashMap;

@Data
@RequiredArgsConstructor
class Cart {
    @NonNull private Direction facing;
    @NonNull private Point location;
    @NonNull private Track track;

    public void move() {
        moveInTheDirectionCartIsFacing();
    }

    private void moveInTheDirectionCartIsFacing() {
        location = mapDirectionToMovement(facing, location);
    }

    public static Point mapDirectionToMovement(Direction direction, Point point) {
        HashMap<Direction, Point> directionToMovement = new HashMap<>();

        directionToMovement.put(Direction.UP, (new Point((int) point.getX(), (int) point.getY() - 1)));
        directionToMovement.put(Direction.RIGHT, (new Point((int) point.getX() + 1, (int) point.getY())));
        directionToMovement.put(Direction.DOWN, (new Point((int) point.getX(), (int) point.getY() + 1)));
        directionToMovement.put(Direction.LEFT, (new Point((int) point.getX() - 1, (int) point.getY())));

        return directionToMovement.get(direction);
    }
}
