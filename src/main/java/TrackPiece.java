import lombok.Data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class TrackPiece {
    Set<Border> exits;

    public TrackPiece(Border... exits)
    {
        this.exits = new HashSet<>(Arrays.asList(exits));
    }
}
