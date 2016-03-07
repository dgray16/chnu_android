package com.chnu.browser_lab2.app.dummy;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class Bookmark {

    /**
     * An array of sample (dummy) items.
     */
    public static List<WebPage> ITEMS = new ArrayList<WebPage>();


    static {
        // Add sample item.
        addItem(new WebPage("Empty now"));
    }

    public static void addItem(WebPage item) {
            ITEMS.add(item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class WebPage {
        public String id;
        public String content;

        public WebPage(String content) {
            this.id = "" + (ITEMS.size() + 1);
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }

}
