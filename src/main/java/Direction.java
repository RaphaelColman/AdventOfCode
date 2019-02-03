import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public enum Direction {
    UP, RIGHT, DOWN, LEFT;

    public static Direction getOppositeDirection(Direction direction) {
        HashMap<Direction, Direction> opposites = new HashMap<>();
        opposites.put(Direction.UP, Direction.DOWN);
        opposites.put(Direction.DOWN, Direction.UP);
        opposites.put(Direction.RIGHT, Direction.LEFT);
        opposites.put(Direction.LEFT, Direction.RIGHT);

        return opposites.get(direction);
    }

    public static HashSet<Direction> getPerpendiculars(Direction direction) {
        HashSet<Direction> directionsToExclude = new HashSet<>();
        directionsToExclude.add(getOppositeDirection(direction));
        directionsToExclude.add(direction);

        HashSet allDirections = new HashSet(Arrays.asList(Direction.values()));
        allDirections.removeAll(directionsToExclude);

        return allDirections;
    }
}
