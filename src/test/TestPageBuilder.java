import org.junit.Test;

import prs.CategoryPage;
import prs.CategoryPageBuilder;
import prs.JsoupCategoryPageBuilder;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.charset.Charset;

import com.google.common.io.Files;

/**
 * Blahblahblah
 */
public class TestPageBuilder {

  @Test
  public void testPageBuild() throws Exception {
    String html = Files.toString(new File("/home/serge/Projects/GEO/flats/src/test/resources/mirkv-page.html"), Charset.defaultCharset());
    CategoryPageBuilder builder = new JsoupCategoryPageBuilder();
    CategoryPage page = builder.build(html, "");
    assertEquals(page.getItems().size(), 24);
  }
}
