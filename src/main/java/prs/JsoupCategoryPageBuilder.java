package prs;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;


/**
 * Blahblahblah
 */
public class JsoupCategoryPageBuilder implements CategoryPageBuilder {
  @Override
  public CategoryPage build(String html, String url) {
    Document doc = Jsoup.parse(html);
    CategoryPage page = new CategoryPage();
    page.setHtml(html);
    page.setUrl(url);

    page.setItems(getItems(doc));
    page.setNextPageUrl(getNextPageUrl(doc));

    return page;
  }

  private String getNextPageUrl(Document html) {
    return null;
  }

  private List<String> getItems(Document doc) {
    List<String> result = Lists.newArrayList();
    Elements elements = doc.select("div.col-xs-12 [class=classbox]");

    elements.forEach(element ->
        result.add(element.toString())
    );
    return result;
  }
}
