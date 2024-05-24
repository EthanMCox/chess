package results;

import model.ListGamesData;
import java.util.Collection;

public record ListGamesResult(Collection<ListGamesData> games) {
}
