import lombok.Data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Data
class TrackPiece {
    Set<Border> exits;

    public TrackPiece(Border... exits)
    {
        this.exits = new HashSet<>(Arrays.asList(exits));
    }
}
