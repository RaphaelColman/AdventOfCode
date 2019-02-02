import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.util.HashMap;

@Data
@RequiredArgsConstructor
public class Track {
    @NonNull HashMap<Point, TrackPiece> hmPointToTrackPiece;
}
