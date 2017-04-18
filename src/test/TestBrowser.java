import org.hamcrest.CoreMatchers;
import org.junit.Test;
import static org.junit.Assert.*;

import brws.Browser;
import brws.HttpBrowser;

/**
 * Blahblahblah
 */
public class TestBrowser {

  @Test
  public void testBrowse() {
    Browser browser = new HttpBrowser();
    String result = browser.browse("https://mirkvartir.ua/offers/list?oper=ar&type=kv&city=5250&colCom=1|2|3|4|5");
    assertThat(result, CoreMatchers.containsString("Киев, Дмитриевская, 46"));
  }
}
