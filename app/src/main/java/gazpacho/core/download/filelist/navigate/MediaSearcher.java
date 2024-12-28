package gazpacho.core.download.filelist.navigate;

import gazpacho.core.download.filelist.model.*;
import gazpacho.core.model.VideoQuality;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import gazpacho.core.model.MediaItem;
import lombok.NonNull;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class MediaSearcher {
    private static final long WAIT_TIME_MS = 500;
    private static final int MAX_RESULT_PAGES = 10;
    private static final int SEARCH_ENTRY_COL_CATEGORY_IDX = 0;
    private static final int SEARCH_ENTRY_COL_TITLE_IDX = 1;
    private static final int SEARCH_ENTRY_COL_DOWNLOAD_IDX = 3;
    private static final int SEARCH_ENTRY_COL_SIZE_IDX = 6;
    private static final int SEARCH_ENTRY_COL_SEEDERS_IDX = 8;
    private static final String PAGES_SELECTOR = ".pager";
    private static final String A_HREF_SELECTOR = "a[href]";
    private static final String HREF_ATTRIBUTE_KEY = "href";
    private static final String RESULTS_TABLE_SELECTOR = "[class=torrentrow]";
    private static final String RESULTS_COL_SELECTOR = "[class=torrenttable]";
    private static final String CATEGORY_SELECTOR = "a";
    private static final String CATEGORY_DESCRIPTION_ATTRIBUTE_KEY = "alt";

    private static final String CATEGORY_ID_REGEX = "(.*)(\\?)(.*)(cat=)(\\d*)";
    private static final Pattern CATEGORY_PATTERN = Pattern.compile(CATEGORY_ID_REGEX);
    private static final int CATEGORY_REGEX_GROUP_IDX = 5;

    private static final String VIDEO_QUALITY_REGEX = String.format(
            "(.*)((%s)|(%s)|(%s))(.*)",
            VideoQuality.HD_720P.value(),
            VideoQuality.HD_1080P.value(),
            VideoQuality.UHD_2160P.value());

    private static final Pattern VIDEO_QUALITY_PATTERN = Pattern.compile(VIDEO_QUALITY_REGEX);
    private static final int VIDEO_QUALITY_REGEX_GROUP_ID = 2;

    private static final String TITLE_SELECTOR = "a[title]";
    private static final String TITLE_ATTRIBUTE_KEY = "title";
    private static final String SIZE_SELECTOR = "font";
    private static final String SEEDERS_SELECTOR = "b";

    private final QueryUrlResolver queryUrlResolver;
    private final Logger logger;

    public MediaSearcher(@NonNull QueryUrlResolver queryUrlResolver, @NonNull Logger logger) {
        this.queryUrlResolver = queryUrlResolver;
        this.logger = logger;
    }

    public List<SearchResultEntry> searchItem(@NonNull MediaItem mediaItem,
                                              @NonNull Connection connection) throws IOException, InterruptedException {
        Set<String> searchPageUrls = getResultPageUrls(mediaItem, connection);
        logger.info("Search urls {}", searchPageUrls);
        return aggregateResults(connection, searchPageUrls);
    }

    private List<SearchResultEntry> aggregateResults(Connection connection,
                                                     Set<String> searchPageUrls) throws IOException, InterruptedException {
        List<SearchResultEntry> searchResultEntries = new ArrayList<>();
        for (String searchPageUrl : searchPageUrls) {
            Document page = getDocument(connection, searchPageUrl);
            searchResultEntries.addAll(getResults(page));
        }
        return searchResultEntries;
    }

    private List<SearchResultEntry> getResults(Document resultsPage) {
        List<SearchResultEntry> pageResults = new ArrayList<>();
        Elements table = resultsPage.select(RESULTS_TABLE_SELECTOR);
        for (Element row : table) {
            Elements columns = row.select(RESULTS_COL_SELECTOR);
            var searchEntryBuilder = SearchResultEntry.builder();
            for (int i = 0; i < columns.size(); ++i) {
                Element column = columns.get(i);
                switch (i) {
                    case (SEARCH_ENTRY_COL_CATEGORY_IDX): {
                        searchEntryBuilder = addCategory(searchEntryBuilder, column);
                    } break;
                    case (SEARCH_ENTRY_COL_TITLE_IDX): {
                        searchEntryBuilder = addTitle(searchEntryBuilder, column);
                    } break;
                    case (SEARCH_ENTRY_COL_DOWNLOAD_IDX): {
                        searchEntryBuilder = addDownloadLink(searchEntryBuilder, column);
                    } break;
                    case (SEARCH_ENTRY_COL_SIZE_IDX): {
                        searchEntryBuilder = addSize(searchEntryBuilder, column);
                    } break;
                    case (SEARCH_ENTRY_COL_SEEDERS_IDX): {
                       searchEntryBuilder = addSeeders(searchEntryBuilder, column);
                    } break;
                }
            }

            pageResults.add(searchEntryBuilder.build());
        }

        return pageResults;
    }

    private SearchResultEntry.SearchResultEntryBuilder addCategory(SearchResultEntry.SearchResultEntryBuilder searchEntryBuilder,
                                                                   Element categoryColumn) {
        var categoryBuilder = CategoryDetails.builder();
        Element categoryElement = categoryColumn.selectFirst(CATEGORY_SELECTOR);
        String categoryHref = categoryElement.attr(HREF_ATTRIBUTE_KEY);
        categoryBuilder.category(MediaCategory.from(getCategory(categoryHref)));

        String description = categoryElement.child(0).attr(CATEGORY_DESCRIPTION_ATTRIBUTE_KEY);
        categoryBuilder.label(description);
        searchEntryBuilder.category(categoryBuilder.build());

        return searchEntryBuilder;
    }

    private SearchResultEntry.SearchResultEntryBuilder addTitle(SearchResultEntry.SearchResultEntryBuilder searchEntryBuilder,
                                                                   Element titleColumn) {
        Element titleElement = titleColumn.selectFirst(TITLE_SELECTOR);
        String title = titleElement.attr(TITLE_ATTRIBUTE_KEY);
        searchEntryBuilder.title(title);
        searchEntryBuilder.videoQuality(VideoQuality.from(getVideoQuality(title)));
        searchEntryBuilder.detailsLink(titleElement.absUrl(HREF_ATTRIBUTE_KEY));
        return searchEntryBuilder;
    }

    private SearchResultEntry.SearchResultEntryBuilder addDownloadLink(SearchResultEntry.SearchResultEntryBuilder searchEntryBuilder,
                                                           Element downloadColumn) {
        Element downloadElement = downloadColumn.selectFirst(A_HREF_SELECTOR);
        searchEntryBuilder.downloadLink(downloadElement.absUrl(HREF_ATTRIBUTE_KEY));
        return searchEntryBuilder;
    }

    private SearchResultEntry.SearchResultEntryBuilder addSize(SearchResultEntry.SearchResultEntryBuilder searchEntryBuilder,
                                                           Element sizeColumn) {
        var sizeBuilder = DownloadSize.builder();
        Element sizeElement = sizeColumn.selectFirst(SIZE_SELECTOR);
        List<Node> childNodes = sizeElement.childNodes();
        if (!CollectionUtils.isEmpty(childNodes)) {
            Node valueNode = childNodes.get(0);
            if (valueNode instanceof TextNode) {
                TextNode valueTextNode = (TextNode) valueNode;
                sizeBuilder.size(Double.parseDouble(valueTextNode.getWholeText()));
            }
            Node unitNode = childNodes.get(2);
            if (unitNode instanceof TextNode) {
                TextNode unitTextNode = (TextNode) unitNode;
                sizeBuilder.unit(SizeUnit.valueOf(unitTextNode.getWholeText()));
            }
            searchEntryBuilder.downloadSize(sizeBuilder.build());
        }
        return searchEntryBuilder;
    }

    private SearchResultEntry.SearchResultEntryBuilder addSeeders(SearchResultEntry.SearchResultEntryBuilder searchEntryBuilder,
                                                               Element seedersColumn) {
        Element seedersElement = seedersColumn.selectFirst(SEEDERS_SELECTOR);
        Integer seeders = -1;
        if (null != seedersElement) {
            List<Node> childNodes = seedersElement.childNodes();
            if (!CollectionUtils.isEmpty(childNodes)) {
                Node firstChild = childNodes.getFirst();
                List<Node> grandchildNodes = firstChild.childNodes();
                if (!CollectionUtils.isEmpty(grandchildNodes)) {
                    String seedersTextValue = grandchildNodes.getFirst().toString();
                    seeders = Integer.parseInt(seedersTextValue);
                }
            }
        }

        searchEntryBuilder.seeders(seeders);
        return searchEntryBuilder;
    }

    private Set<String> getResultPageUrls(MediaItem mediaItem, Connection connection) throws IOException, InterruptedException {
        String firstResultsUrl = queryUrlResolver.getQueryUrl(mediaItem);
        Document firstResultsPage = getDocument(connection, firstResultsUrl);
        Element pager = firstResultsPage.selectFirst(PAGES_SELECTOR);
        if (null == pager) {
            return Set.of(firstResultsUrl);
        }

        Elements pages = pager.children();
        Set<String> searchPagesUrls = new HashSet<>();
        for (Element page : pages) {
            Element url = page.selectFirst(A_HREF_SELECTOR);
            if (null != url) {
                searchPagesUrls.add(url.absUrl(HREF_ATTRIBUTE_KEY));
                if (MAX_RESULT_PAGES == searchPagesUrls.size()) {
                    break;
                }
            }
        }
        return searchPagesUrls;
    }


    private Document getDocument(Connection connection, String url)
            throws IOException, InterruptedException {
        bePolite();

        logger.info("Accessing url {}", url);
        return connection.newRequest(url).get();
    }

    private void bePolite() throws InterruptedException {
        Thread.sleep(WAIT_TIME_MS);
    }

    private Integer getCategory(String categoryHref) {
        logger.info("Get category {}", categoryHref);
        Matcher matcher = CATEGORY_PATTERN.matcher(categoryHref);
        if (matcher.find() && matcher.groupCount() >= CATEGORY_REGEX_GROUP_IDX) {
            return Integer.parseInt(matcher.group(CATEGORY_REGEX_GROUP_IDX));
        }
        return -1;
    }

    private String getVideoQuality(String title) {
        Matcher matcher = VIDEO_QUALITY_PATTERN.matcher(title);
        if (matcher.find() && matcher.groupCount() >= VIDEO_QUALITY_REGEX_GROUP_ID) {
            return matcher.group(VIDEO_QUALITY_REGEX_GROUP_ID);
        }
        return "";
    }
}
