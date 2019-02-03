import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
class Cart {
    @NonNull private Direction facing;
    @NonNull private Point location;
    @NonNull private Track track;

    private ArrayList<RelativeDirections> historicalTurns = new ArrayList<>();

    void move() {
        try {
            moveCart();
        } catch (MoreExitsThanExpectedException e) {
            e.printStackTrace();
        }
    }

    private void moveCart() throws MoreExitsThanExpectedException {
        TrackPiece currentTrackPiece = track.getTrackPieceForLocation(location);
        if (currentTrackPiece.isJunction()) {
            if (historicalTurns.isEmpty()) {
                turnLeft();
                location = mapDirectionToMovement(facing, location);
            } else {
                turnBasedOnHistoricalTurns();
                location = mapDirectionToMovement(facing, location);
            }

        } else if (currentTrackPiece.isStraight()) { //If the trackpiece is straight then just go the direction you're facing.
            location = mapDirectionToMovement(facing, location);
        } else if (currentTrackPiece.isCurved()) {
            HashSet<Direction> perpendiculars = Direction.getPerpendiculars(facing);
            Set<Direction> exits = new HashSet(currentTrackPiece.getExits());

            exits.retainAll(perpendiculars);
            if (exits.size() != 1)
            {
                throw new MoreExitsThanExpectedException();
            }

            facing = exits.iterator().next();
            location = mapDirectionToMovement(facing, location);
        }

    }

    private void turnBasedOnHistoricalTurns() {
        RelativeDirections previousDirection = historicalTurns.get(historicalTurns.size() - 1);
        if (previousDirection.equals(RelativeDirections.LEFT)) {
            historicalTurns.add(RelativeDirections.STRAIGHT);
        } else if (previousDirection.equals(RelativeDirections.STRAIGHT)) {
            turnRight();
        } else if (previousDirection.equals(RelativeDirections.RIGHT)) {
            turnLeft();
        }
    }

    private void turnLeft() {
        facing = Direction.getLeft(facing);
        historicalTurns.add(RelativeDirections.LEFT);
    }

    private void turnRight() {
        facing = Direction.getRight(facing);
        historicalTurns.add(RelativeDirections.RIGHT);
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
