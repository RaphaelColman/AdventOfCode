import lombok.Data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Data
class TrackPiece {
    Set<Direction> exits;

    public TrackPiece(Direction... exits)
    {
        this.exits = new HashSet<>(Arrays.asList(exits));
    }

    public boolean isStraight() {
        HashSet<Direction> verticalExits = new HashSet<>(Arrays.asList(Direction.UP, Direction.DOWN));
        HashSet<Direction> horizontalExits = new HashSet<>(Arrays.asList(Direction.RIGHT, Direction.LEFT));

        boolean vertical = exits.containsAll(verticalExits);
        boolean horizontal = exits.containsAll(horizontalExits);

        return vertical || horizontal;
    }

    public boolean isJunction() {
        HashSet<Direction> allDirections = new HashSet<>(Arrays.asList(Direction.values()));
        return exits.containsAll(allDirections);
    }

    public boolean isCurved() {
        return !isStraight() && !isJunction();
    }
}
