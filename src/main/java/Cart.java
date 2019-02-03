import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
class Cart {
    @NonNull private Direction facing;
    @NonNull private Point location;
    @NonNull private Track track;

    void move() {
        try {
            moveInTheDirectionCartIsFacing();
        } catch (MoreExitsThanExpectedException e) {
            e.printStackTrace();
        }
    }

    private void moveInTheDirectionCartIsFacing() throws MoreExitsThanExpectedException {
        TrackPiece currentTrackPiece = track.getTrackPieceForLocation(location);
        if (currentTrackPiece.isStraight()) { //If the trackpiece is straight then just go the direction you're facing.
            location = mapDirectionToMovement(facing, location);
        } else if (currentTrackPiece.isCurved()) {
            HashSet<Direction> perpendiculars = Direction.getPerpendiculars(facing);
            Set<Direction> exits = currentTrackPiece.getExits();

            exits.retainAll(perpendiculars);
            if (exits.size() != 1)
            {
                throw new MoreExitsThanExpectedException();
            }

            facing = exits.iterator().next();
            location = mapDirectionToMovement(facing, location);
        }

    }

    static Point mapDirectionToMovement(Direction direction, Point point) {
        HashMap<Direction, Point> directionToMovement = new HashMap<>();

        directionToMovement.put(Direction.UP, (new Point((int) point.getX(), (int) point.getY() - 1)));
        directionToMovement.put(Direction.RIGHT, (new Point((int) point.getX() + 1, (int) point.getY())));
        directionToMovement.put(Direction.DOWN, (new Point((int) point.getX(), (int) point.getY() + 1)));
        directionToMovement.put(Direction.LEFT, (new Point((int) point.getX() - 1, (int) point.getY())));

        return directionToMovement.get(direction);
    }
}
