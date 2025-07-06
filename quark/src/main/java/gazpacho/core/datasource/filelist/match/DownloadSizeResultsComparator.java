package gazpacho.core.datasource.filelist.match;

import gazpacho.core.datasource.filelist.model.SearchResultEntry;
import gazpacho.core.model.SizeUnit;

import java.util.Comparator;

public class DownloadSizeResultsComparator implements Comparator<SearchResultEntry> {

    @Override
    public int compare(SearchResultEntry left, SearchResultEntry right) {
        if (getUnit(left).equals(getUnit(right))) {
            return (int) (left.downloadSize().size() - right.downloadSize().size());
        }

        if (SizeUnit.GB.equals(getUnit(left)) && !SizeUnit.GB.equals(getUnit(right))) {
            return -1;
        }

        if (SizeUnit.GB.equals(getUnit(right)) && !SizeUnit.GB.equals(getUnit(left))) {
            return 1;
        }
        return 0;
    }

    private SizeUnit getUnit(SearchResultEntry entry) {
        return entry.downloadSize().unit();
    }
}
