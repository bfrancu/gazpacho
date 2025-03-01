package gazpacho.core.datasource.filelist.navigate;

import gazpacho.core.datasource.filelist.model.*;
import gazpacho.core.model.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.Connection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MediaSearcherTest {

    private static final String STATIC_HTML_DOCUMENT = """
            div style='margin:10px 0px;' align='center'>
            <h2>Rezultatele căutării după "iron giant"</h2>
            </div>&nbsp;
            <div class='visitedlinks' style='margin-top:10px;'>
               <div class='colhead' align='center' style='float:left;border:none;width:35px;height:15px;'>Type</div>
               <div class='colhead' align='left' style='float:left;border:none;width:455px;height:15px;'>Name</div>
               <div class='colhead' align='center' style='float:left;border:none;width:20px;height:15px;'>&nbsp;</div>
               <div class='colhead' align='center' style='float:left;border:none;width:20px;height:15px;'>&nbsp;</div>
               <div class='colhead' align='center' style='float:left;border:none;width:25px;height:15px;'><img src='styles/images/comments.png' alt='Comments' title='Comments' /></div>
               <div class='colhead' align='center' style='float:left;border:none;width:65px;height:15px;'><a href='/browse.php?search=iron+giant&cat=0&searchin=1&sort=2&asc=1' style='color: inherit !important;'><img src='styles/images/date.png' alt='Added' title='Added' /> ⯆</a></div>
               <div class='colhead' align='center' style='float:left;border:none;width:45px;height:15px;'><a href='/browse.php?search=iron+giant&cat=0&searchin=1&sort=3'><img src='styles/images/size.png' alt='Size' title='Size' /></a></div>
               <!-- <div class='colhead' align='center'>Views</div>
                  <div class='colhead' align='center'>Hits</div> -->
               <div class='colhead' align='center' style='float:left;border:none;width:40px;height:15px;'><a href='/browse.php?search=iron+giant&cat=0&searchin=1&sort=4'><img src='styles/images/snatched.png' alt='Snatched' title='Snatched' /></a></div>
               <div class='colhead' align='center' style='float:left;border:none;width:35px;height:15px;'><a href='/browse.php?search=iron+giant&cat=0&searchin=1&sort=5'><img src='styles/images/arrowup.gif' alt='Seeders' title='Seeders' /></a></div>
               <div class='colhead' align='center' style='float:left;border:none;width:35px;height:15px;'><a href='/browse.php?search=iron+giant&cat=0&searchin=1&sort=6'><img src='styles/images/arrowdown.gif' alt='Leechers' title='Leechers' /></a></div>
               <div class='colhead' align='center' style='float:left;border:none;width:70px;height:15px;'><img src='styles/images/uppedby.png' alt='Upped by' title='Upped by' /></div>
               <div class='clearfix'></div>
               <div class='torrentrow'>
                  <div align='center' class='torrenttable'><span style='width:45px;height:47px;vertical-align:middle;display:table-cell;'><a href='browse.php?cat=19'><img border='0' src='styles/images/cat/hd-ro.png' alt='Filme HD-RO' /></a></span></div>
                  <div align='left' class='torrenttable'><span style='padding:0px 5px;width:455px;height:47px;vertical-align:middle;display:table-cell;'><span data-toggle='tooltip' data-placement='bottom' data-html='true' title="<img src='https://image.tmdb.org/t/p/w185//ct04FCFLPImNG5thcPLRnVsZlmS.jpg'>"><a href='details.php?id=741407' title='The.Iron.Giant.1999.Signature.Edition.720p.BluRay.DTS.x264-OmertaHD'><b>The.Iron.Giant.1999.Signature.Edition.720p.BluRay.DTS.x264...</b></a></span><br /><img src='styles/images/tags/freeleech.png' alt='FreeLeech' /> <font class='small'>[Animation, Action, Adventure]</font></span></div>
                  <div align='center' class='torrenttable'><span style='width:30px;height:47px;vertical-align:middle;display:table-cell;'><a href="snatchlist.php?action=usetoken&torrent=741407"><img src='styles/images/download_token.png' data-toggle='tooltip' data-placement='top' title='Use token' /></a></span></div>
                  <div align='center' class='torrenttable'><span style='width:30px;height:47px;vertical-align:middle;display:table-cell;'><a href="download.php?id=741407"><img src='styles/images/download.png' data-toggle='tooltip' data-placement='top' title='Download' /></a></span></div>
                  <div align='center' class='torrenttable'><span style='width:35px;height:47px;vertical-align:middle;display:table-cell;'><font class='small'>0</font></span></div>
                  <div align='center' class='torrenttable'>
                     <span style='width:75px;height:47px;vertical-align:middle;display:table-cell;'>
                        <nobr><font class='small'>19:35:04<br />04/06/2021</font></nobr>
                     </span>
                  </div>
                  <div align='center' class='torrenttable'><span style='width:55px;height:47px;vertical-align:middle;display:table-cell;'><font class='small'>6.10<br />GB</font></span></div>
                  <div align='center' class='torrenttable'><span style='width:50px;height:47px;vertical-align:middle;display:table-cell;'><font class='small'>595<br />times</font></span></div>
                  <div align='center' class='torrenttable'><span style='width:45px;height:47px;vertical-align:middle;display:table-cell;'><b><font color=#787878>7</font></b></span></div>
                  <div align='center' class='torrenttable'><span style='width:45px;height:47px;vertical-align:middle;display:table-cell;'>0</span></div>
                  <div align='center' class='torrenttable'><span style='word-break:break-all;width:80px;height:47px;vertical-align:middle;display:table-cell;'><font class='small'><a href=#><b>Anonymous</b></a></font></span></div>
                  <div class='clearfix'></div>
               </div>
               <div class='torrentrow'>
                  <div align='center' class='torrenttable'><span style='width:45px;height:47px;vertical-align:middle;display:table-cell;'><a href='browse.php?cat=19'><img border='0' src='styles/images/cat/hd-ro.png' alt='Filme HD-RO' /></a></span></div>
                  <div align='left' class='torrenttable'><span style='padding:0px 5px;width:455px;height:47px;vertical-align:middle;display:table-cell;'><span data-toggle='tooltip' data-placement='bottom' data-html='true' title="<img src='https://image.tmdb.org/t/p/w185//ct04FCFLPImNG5thcPLRnVsZlmS.jpg'>"><a href='details.php?id=539025' title='The.Iron.Giant.1999.1080p.BluRay.DTS.x264-VietHD'><b>The.Iron.Giant.1999.1080p.BluRay.DTS.x264-VietHD</b></a></span><br /><img src='styles/images/tags/freeleech.png' alt='FreeLeech' /> <font class='small'>[Animation, Action, Adventure]</font></span></div>
                  <div align='center' class='torrenttable'><span style='width:30px;height:47px;vertical-align:middle;display:table-cell;'><a href="snatchlist.php?action=usetoken&torrent=539025"><img src='styles/images/download_token.png' data-toggle='tooltip' data-placement='top' title='Use token' /></a></span></div>
                  <div align='center' class='torrenttable'><span style='width:30px;height:47px;vertical-align:middle;display:table-cell;'><a href="download.php?id=539025"><img src='styles/images/download.png' data-toggle='tooltip' data-placement='top' title='Download' /></a></span></div>
                  <div align='center' class='torrenttable'><span style='width:35px;height:47px;vertical-align:middle;display:table-cell;'><font class='small'><b><a href='details.php?id=539025&amp;hit=1#startcomments'>1</a></b></font></span></div>
                  <div align='center' class='torrenttable'>
                     <span style='width:75px;height:47px;vertical-align:middle;display:table-cell;'>
                        <nobr><font class='small'>15:58:55<br />22/12/2017</font></nobr>
                     </span>
                  </div>
                  <div align='center' class='torrenttable'><span style='width:55px;height:47px;vertical-align:middle;display:table-cell;'><font class='small'>7.44<br />GB</font></span></div>
                  <div align='center' class='torrenttable'><span style='width:50px;height:47px;vertical-align:middle;display:table-cell;'><font class='small'>2,261<br />times</font></span></div>
                  <div align='center' class='torrenttable'><span style='width:45px;height:47px;vertical-align:middle;display:table-cell;'><b><font color=#787878>51</font></b></span></div>
                  <div align='center' class='torrenttable'><span style='width:45px;height:47px;vertical-align:middle;display:table-cell;'>0</span></div>
                  <div align='center' class='torrenttable'><span style='word-break:break-all;width:80px;height:47px;vertical-align:middle;display:table-cell;'><font class='small'><a href=#><b>x1337x</b></a></font></span></div>
                  <div class='clearfix'></div>
               </div>
               <div class='torrentrow'>
                  <div align='center' class='torrenttable'><span style='width:45px;height:47px;vertical-align:middle;display:table-cell;'><a href='browse.php?cat=19'><img border='0' src='styles/images/cat/hd-ro.png' alt='Filme HD-RO' /></a></span></div>
                  <div align='left' class='torrenttable'><span style='padding:0px 5px;width:455px;height:47px;vertical-align:middle;display:table-cell;'><span data-toggle='tooltip' data-placement='bottom' data-html='true' title="<img src='https://image.tmdb.org/t/p/w185//ct04FCFLPImNG5thcPLRnVsZlmS.jpg'>"><a href='details.php?id=491895' title='The.Iron.Giant.1999.720p.BluRay.DD5.1.x264.RoSubbed-playHD'><b>The.Iron.Giant.1999.720p.BluRay.DD5.1.x264.RoSubbed-playHD</b></a></span><br /><img src='styles/images/tags/internal.png' alt='Internal' /> <img src='styles/images/tags/freeleech.png' alt='FreeLeech' /> <font class='small'>[Animation, Action, Adventure]</font></span></div>
                  <div align='center' class='torrenttable'><span style='width:30px;height:47px;vertical-align:middle;display:table-cell;'><a href="snatchlist.php?action=usetoken&torrent=491895"><img src='styles/images/download_token.png' data-toggle='tooltip' data-placement='top' title='Use token' /></a></span></div>
                  <div align='center' class='torrenttable'><span style='width:30px;height:47px;vertical-align:middle;display:table-cell;'><a href="download.php?id=491895"><img src='styles/images/download.png' data-toggle='tooltip' data-placement='top' title='Download' /></a></span></div>
                  <div align='center' class='torrenttable'><span style='width:35px;height:47px;vertical-align:middle;display:table-cell;'><font class='small'><b><a href='details.php?id=491895&amp;hit=1#startcomments'>3</a></b></font></span></div>
                  <div align='center' class='torrenttable'>
                     <span style='width:75px;height:47px;vertical-align:middle;display:table-cell;'>
                        <nobr><font class='small'>22:47:47<br />13/04/2017</font></nobr>
                     </span>
                  </div>
                  <div align='center' class='torrenttable'><span style='width:55px;height:47px;vertical-align:middle;display:table-cell;'><font class='small'>4.99<br />GB</font></span></div>
                  <div align='center' class='torrenttable'><span style='width:50px;height:47px;vertical-align:middle;display:table-cell;'><font class='small'>2,823<br />times</font></span></div>
                  <div align='center' class='torrenttable'><span style='width:45px;height:47px;vertical-align:middle;display:table-cell;'><b><font color=#787878>19</font></b></span></div>
                  <div align='center' class='torrenttable'><span style='width:45px;height:47px;vertical-align:middle;display:table-cell;'>0</span></div>
                  <div align='center' class='torrenttable'><span style='word-break:break-all;width:80px;height:47px;vertical-align:middle;display:table-cell;'><font class='small'><a href=#><b>vscorrpio</b></a></font></span></div>
                  <div class='clearfix'></div>
               </div>
               <div class='torrentrow'>
                  <div align='center' class='torrenttable'><span style='width:45px;height:47px;vertical-align:middle;display:table-cell;'><a href='browse.php?cat=20'><img border='0' src='styles/images/cat/bluray.png' alt='Filme Blu-Ray' /></a></span></div>
                  <div align='left' class='torrenttable'><span style='padding:0px 5px;width:455px;height:47px;vertical-align:middle;display:table-cell;'><span data-toggle='tooltip' data-placement='bottom' data-html='true' title="<img src='https://image.tmdb.org/t/p/w185//ct04FCFLPImNG5thcPLRnVsZlmS.jpg'>"><a href='details.php?id=443535' title='The.Iron.Giant.1999.2in1.1080p.Blu-ray.AVC.DTS-HD.MA.5.1.RoSubbed-HDBits'><b>The.Iron.Giant.1999.2in1.1080p.Blu-ray.AVC.DTS-HD.MA.5.1.R...</b></a></span><br /><img src='styles/images/tags/freeleech.png' alt='FreeLeech' /> <font class='small'>[Animation, Action, Adventure, RoSubbed]</font></span></div>
                  <div align='center' class='torrenttable'><span style='width:30px;height:47px;vertical-align:middle;display:table-cell;'><a href="snatchlist.php?action=usetoken&torrent=443535"><img src='styles/images/download_token.png' data-toggle='tooltip' data-placement='top' title='Use token' /></a></span></div>
                  <div align='center' class='torrenttable'><span style='width:30px;height:47px;vertical-align:middle;display:table-cell;'><a href="download.php?id=443535"><img src='styles/images/download.png' data-toggle='tooltip' data-placement='top' title='Download' /></a></span></div>
                  <div align='center' class='torrenttable'><span style='width:35px;height:47px;vertical-align:middle;display:table-cell;'><font class='small'>0</font></span></div>
                  <div align='center' class='torrenttable'>
                     <span style='width:75px;height:47px;vertical-align:middle;display:table-cell;'>
                        <nobr><font class='small'>08:59:50<br />08/09/2016</font></nobr>
                     </span>
                  </div>
                  <div align='center' class='torrenttable'><span style='width:55px;height:47px;vertical-align:middle;display:table-cell;'><font class='small'>41.13<br />GB</font></span></div>
                  <div align='center' class='torrenttable'><span style='width:50px;height:47px;vertical-align:middle;display:table-cell;'><font class='small'>91<br />times</font></span></div>
                  <div align='center' class='torrenttable'><span style='width:45px;height:47px;vertical-align:middle;display:table-cell;'><b><font color=#787878>3</font></b></span></div>
                  <div align='center' class='torrenttable'><span style='width:45px;height:47px;vertical-align:middle;display:table-cell;'>0</span></div>
                  <div align='center' class='torrenttable'><span style='word-break:break-all;width:80px;height:47px;vertical-align:middle;display:table-cell;'><font class='small'><a href=#><b>MDK</b></a></font></span></div>
                  <div class='clearfix'></div>
               </div>
               <div class='torrentrow'>
                  <div align='center' class='torrenttable'><span style='width:45px;height:47px;vertical-align:middle;display:table-cell;'><a href='browse.php?cat=4'><img border='0' src='styles/images/cat/hd.png' alt='Filme HD' /></a></span></div>
                  <div align='left' class='torrenttable'><span style='padding:0px 5px;width:455px;height:47px;vertical-align:middle;display:table-cell;'><span ><a href='details.php?id=118553' title='The.Iron.Giant.1999.720p.HDTV.x264-HDL'><b>The.Iron.Giant.1999.720p.HDTV.x264-HDL</b></a></span><br /><font class='small'>[Animation, Adventure, Family, Sci-Fi, ]</font></span></div>
                  <div align='center' class='torrenttable'><span style='width:30px;height:47px;vertical-align:middle;display:table-cell;'><a href="snatchlist.php?action=usetoken&torrent=118553"><img src='styles/images/download_token.png' data-toggle='tooltip' data-placement='top' title='Use token' /></a></span></div>
                  <div align='center' class='torrenttable'><span style='width:30px;height:47px;vertical-align:middle;display:table-cell;'><a href="download.php?id=118553"><img src='styles/images/download.png' data-toggle='tooltip' data-placement='top' title='Download' /></a></span></div>
                  <div align='center' class='torrenttable'><span style='width:35px;height:47px;vertical-align:middle;display:table-cell;'><font class='small'><b><a href='details.php?id=118553&amp;hit=1#startcomments'>10</a></b></font></span></div>
                  <div align='center' class='torrenttable'>
                     <span style='width:75px;height:47px;vertical-align:middle;display:table-cell;'>
                        <nobr><font class='small'>16:28:20<br />12/07/2010</font></nobr>
                     </span>
                  </div>
                  <div align='center' class='torrenttable'><span style='width:55px;height:47px;vertical-align:middle;display:table-cell;'><font class='small'>2.22<br />GB</font></span></div>
                  <div align='center' class='torrenttable'><span style='width:50px;height:47px;vertical-align:middle;display:table-cell;'><font class='small'>932<br />times</font></span></div>
                  <div align='center' class='torrenttable'><span style='width:45px;height:47px;vertical-align:middle;display:table-cell;'><b><font color=#787878>5</font></b></span></div>
                  <div align='center' class='torrenttable'><span style='width:45px;height:47px;vertical-align:middle;display:table-cell;'><b>1</b></span></div>
                  <div align='center' class='torrenttable'><span style='word-break:break-all;width:80px;height:47px;vertical-align:middle;display:table-cell;'><font class='small'><a href=#><b>SilverHybrid</b></a></font></span></div>
                  <div class='clearfix'></div>
               </div>
            </div>
            &nbsp;</div></div>
            """;

    private static final Document RESULTS_PAGE_DOC = Jsoup.parse(STATIC_HTML_DOCUMENT);
    private static final String TEST_URL = "https://testurl.io/browse.php";

    private static final VisualMedia MEDIA_ITEM = VisualMedia.builder()
            .metadata(MediaMetadata.builder()
                    .tmdbId(10L)
                    .mediaType(MediaType.MOVIE)
                    .popularity(100.0)
                    .title("The Iron Giant")
                    .language("EN")
                    .description("Iron giant")
                    .originCountry("US")
                    .firstAirDate(LocalDate.parse("1999-01-01"))
                    .build())
            .release(MediaRelease.builder()
                    .mediaReleaseType(MediaReleaseType.MOVIE)
                    .releaseDate(LocalDate.parse("1999-01-01"))
                    .build())
            .build();

    @Mock
    Connection primaryConnection;

    @Mock
    Connection searchConnection;

    @Mock
    QueryUrlResolver queryUrlResolver;

    @Mock
    Logger logger;

    @BeforeEach
    public void setUp() {
        when(queryUrlResolver.getQueryUrl(eq(MEDIA_ITEM))).thenReturn(TEST_URL);
        when(primaryConnection.newRequest(eq(TEST_URL))).thenReturn(searchConnection);
    }

    @Test
    public void searchItem_returnsResults() throws IOException, InterruptedException {
        when(searchConnection.get()).thenReturn(RESULTS_PAGE_DOC);
        List<SearchResultEntry> results = makeUnit().searchItem(MEDIA_ITEM, primaryConnection);
        SearchResultEntry expectedEntry = SearchResultEntry.builder()
                .category(CategoryDetails.builder()
                        .label("Filme HD-RO")
                        .category(MediaCategory.MOVIES_HD_RO)
                        .build())
                .title("The.Iron.Giant.1999.Signature.Edition.720p.BluRay.DTS.x264-OmertaHD")
                .detailsLink("")
                .downloadLink("")
                .downloadSize(DownloadSize.builder()
                        .size(6.1)
                        .unit(SizeUnit.GB)
                        .build())
                .videoQuality(VideoQuality.HD_720P)
                .seeders(7)
                .build();

        assertEquals(5, results.size());
        results.forEach(result -> assertTrue(result.title().startsWith("The.Iron.Giant.1999")));
        assertTrue(results.contains(expectedEntry));
    }

    @Test
    public void searchItem_throwsIOException_whenIOException() throws IOException {
        when(searchConnection.get()).thenThrow(IOException.class);
        assertThrows(IOException.class, () -> makeUnit().searchItem(MEDIA_ITEM, primaryConnection));
    }

    private MediaSearcher makeUnit() {
        return new MediaSearcher(queryUrlResolver, logger);
    }

}