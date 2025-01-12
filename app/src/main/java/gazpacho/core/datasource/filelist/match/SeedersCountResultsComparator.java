package gazpacho.core.datasource.filelist.match;

import gazpacho.core.datasource.filelist.model.SearchResultEntry;

import java.util.Comparator;

public class SeedersCountResultsComparator implements Comparator<SearchResultEntry> {
    @Override
    public int compare(SearchResultEntry left, SearchResultEntry right) {
        return right.seeders() - left.seeders();
    }
}