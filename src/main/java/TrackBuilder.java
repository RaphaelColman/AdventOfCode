import lombok.Data;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.IntStream;

@Data
class TrackBuilder {
    HashMap<Point, Character> hmPointToChar;
    Track track;
    CartTracker cartTracker;

    TrackBuilder(List<String> lines, CartTracker cartTracker) {
        this.cartTracker = cartTracker;

        hmPointToChar = buildHashMapOfTrackChars(lines);
        buildTrack();
        placeCarts();
    }

    private void buildTrack() {
        HashMap<Point, TrackPiece> hmPointToTrackPiece = new HashMap<>();
        hmPointToChar.keySet().forEach(point -> {
            try {
                getTrackPieceForPoint(point).ifPresent(trackPiece ->
                        hmPointToTrackPiece.put(new Point(point), trackPiece));
            } catch (TrackPieceNotRecognisedException e) {
                e.printStackTrace();
            }
        });

        track = new Track(hmPointToTrackPiece);
    }

    private void placeCarts() {
        hmPointToChar.keySet().forEach(point -> {
            Character character = hmPointToChar.get(point);
            if (isCart(character)) {
                cartTracker.addCart(new Cart(getDirectionForCartCharacter(character), new Point(point), track));
            }
        });
    }

    private static Direction getDirectionForCartCharacter(Character character) {
        HashMap<Character, Direction> characterDirectionHashMap = new HashMap<>();
        characterDirectionHashMap.put('>', Direction.RIGHT);
        characterDirectionHashMap.put('<', Direction.LEFT);
        characterDirectionHashMap.put('^', Direction.UP);
        characterDirectionHashMap.put('v', Direction.DOWN);

        return characterDirectionHashMap.get(character);
    }

    private static HashMap<Point, Character> buildHashMapOfTrackChars(List<String> lines) {
        HashMap<Point, Character> hm = new HashMap<>();
        IntStream.range(0, lines.size()).forEach(i ->
        {
            String line = lines.get(i);
            IntStream.range(0, line.length()).forEach(j -> hm.put(new Point(j, i), line.charAt(j)));
        });
        return hm;
    }

    static boolean isCurvedPiece(char c) {
        return c == '\\' || c == '/';
    }

    Optional<TrackPiece> getTrackPieceForPoint(Point point) throws TrackPieceNotRecognisedException {
        Character character = hmPointToChar.get(point);
        Optional<TrackPiece> returnValue = Optional.empty();
        if (character == '|') {
            returnValue = Optional.of(new TrackPiece(Direction.UP, Direction.DOWN));
        } else if (character == '-') {
            returnValue = Optional.of(new TrackPiece(Direction.LEFT, Direction.RIGHT));
        } else if (character == '+') {
            returnValue = Optional.of(new TrackPiece(Direction.values()));
        } else if (isCurvedPiece(character)) {
            returnValue = Optional.of(getCurvedTrackPiece(point, character));
        } else if (isCart(character)) {
            if (isHorizontalCart(character)) {
                returnValue = Optional.of(new TrackPiece(Direction.LEFT, Direction.RIGHT));
            } else if (isVerticalCart(character)) {
                returnValue = Optional.of(new TrackPiece(Direction.UP, Direction.DOWN));
            }
        }
        return returnValue;
    }

    private static boolean isCart(Character character) {
        return isHorizontalCart(character) || isVerticalCart(character);
    }

    private static boolean isHorizontalCart(Character character) {
        return character == '>' || character == '<';
    }

    private static boolean isVerticalCart(Character character) {
        return character == '^' || character == 'v';
    }

    private TrackPiece getCurvedTrackPiece(Point point, Character character) throws TrackPieceNotRecognisedException {

        Character up = hmPointToChar.getOrDefault(Cart.mapDirectionToMovement(Direction.UP, point), ' ');
        Character right = hmPointToChar.getOrDefault(Cart.mapDirectionToMovement(Direction.RIGHT, point), ' ');
        Character down = hmPointToChar.getOrDefault(Cart.mapDirectionToMovement(Direction.DOWN, point), ' ');
        Character left = hmPointToChar.getOrDefault(Cart.mapDirectionToMovement(Direction.LEFT, point), ' ');

        if (character == '/') {
            if (connects(right, down)) {
                return new TrackPiece(Direction.RIGHT, Direction.DOWN);
            } else if (connects(left, up)) {
                return new TrackPiece(Direction.UP, Direction.LEFT);
            }
        } else if (character == '\\') {
            if (connects(left, down)) { //left and down
                return new TrackPiece(Direction.LEFT, Direction.DOWN);
            } else if (connects(right, up)) { //up and right
                return new TrackPiece(Direction.UP, Direction.RIGHT);
            }
        }
        throw new TrackPieceNotRecognisedException("Character: " + character + " Point: " + point.toString());
    }

    private boolean connects(Character leftOrRight, Character down) {
        return connectsOnEitherSide(leftOrRight) && connectsAboveOrBelow(down);
    }

    private static boolean connectsAboveOrBelow(Character character) {
        return character == '|'
                || character == '+'
                || character == 'v'
                || character == '^';
    }

    private static boolean connectsOnEitherSide(Character character) {
        Set<Character> sideCharacters = new HashSet<>(Arrays.asList('-', '+', '>', '<'));
        return sideCharacters.contains(character);
    }

    Track getTrack() {
        return track;
    }
}
