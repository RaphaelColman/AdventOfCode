import lombok.Data;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.IntStream;

@Data
public class TrackBuilder {
    HashMap<Point, Character> hmPointToChar;
    Track track;

    public TrackBuilder(List<String> lines) {
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
            if(isCart(character)) {
                track.addCart(new Cart(getDirectionForCartCharacter(character), new Point(point)));
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

    static HashMap<Point, Character> buildHashMapOfTrackChars(List<String> lines) {
        HashMap<Point, Character> hm = new HashMap<>();
        IntStream.range(0, lines.size()).forEach(i ->
        {
            String line = lines.get(i);
            IntStream.range(0, line.length()).forEach(j -> hm.put(new Point(j,i), line.charAt(j)));
        });
        return hm;
    }

    protected static boolean isCurvedPiece(char c) {
        return c == '\\' || c == '/';
    }

    public Optional<TrackPiece> getTrackPieceForPoint(Point point) throws TrackPieceNotRecognisedException {
        Character character = hmPointToChar.get(point);
        Optional<TrackPiece> returnValue = Optional.empty();
        if (character.charValue() == '|') {
            returnValue = Optional.of(new TrackPiece(Border.TOP, Border.BOTTOM));
        }
        else if (character.charValue() == '-') {
            returnValue = Optional.of(new TrackPiece(Border.LEFT, Border.RIGHT));
        }
        else if (character.charValue() == '+') {
            returnValue = Optional.of(new TrackPiece(Border.values()));
        }
        else if (isCurvedPiece(character))
        {
            returnValue = Optional.of(getCurvedTrackPiece(point, character));
        }
        else if (isCart(character))
        {
            if (isHorizontalCart(character))
            {
                returnValue = Optional.of(new TrackPiece(Border.LEFT, Border.RIGHT));
            }
            else if (isVerticalCart(character))
            {
                returnValue = Optional.of(new TrackPiece(Border.TOP, Border.BOTTOM));
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
        Character up = hmPointToChar.getOrDefault(new Point((int) point.getX(), (int) point.getY() - 1), ' ');
        Character right = hmPointToChar.getOrDefault(new Point((int) point.getX() + 1, (int) point.getY()), ' ');
        Character down = hmPointToChar.getOrDefault(new Point((int) point.getX(), (int) point.getY() + 1), ' ');
        Character left = hmPointToChar.getOrDefault(new Point((int) point.getX() - 1, (int) point.getY()), ' ');

        if (character == '/') {
            if (connectsOnEitherSide(right) && connectsAboveOrBelow(down)) {
                return new TrackPiece(Border.RIGHT, Border.BOTTOM);
            }
            else if (connectsOnEitherSide(left) && connectsAboveOrBelow(up)) {
                return new TrackPiece(Border.TOP, Border.LEFT);
            }
        } else if (character == '\\') {
            if (connectsOnEitherSide(left) && connectsAboveOrBelow(down)) { //left and down
                return new TrackPiece(Border.LEFT, Border.BOTTOM);
            }
            else if (connectsOnEitherSide(right) && connectsAboveOrBelow(up)) { //up and right
                return new TrackPiece(Border.TOP, Border.RIGHT);
            }
        }
        throw new TrackPieceNotRecognisedException("Character: " + character + " Point: " + point.toString());
    }

    private static boolean connectsAboveOrBelow(Character character) {
        return character == '|'
                || character == '+'
                || character == 'v'
                || character == '^';
    }

    private static boolean connectsOnEitherSide(Character character)
    {
        Set<Character> sideCharacters = new HashSet<>(Arrays.asList('-', '+', '>', '<'));
        return sideCharacters.contains(character);
    }

    public Track getTrack() {
        return track;
    }
}
