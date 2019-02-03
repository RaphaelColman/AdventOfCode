import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
class Track {
    @NonNull HashMap<Point, TrackPiece> hmPointToTrackPiece;

    public TrackPiece getTrackPieceForLocation(Point point) {
        return hmPointToTrackPiece.get(point);
    }
}
