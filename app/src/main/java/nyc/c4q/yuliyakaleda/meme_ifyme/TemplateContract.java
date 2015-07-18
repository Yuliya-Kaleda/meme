package nyc.c4q.yuliyakaleda.meme_ifyme;

import android.provider.BaseColumns;

public final class TemplateContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public TemplateContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "TEMPLATES";
        public static final String COLUMN_NAME_ID = "_ID";
        public static final String COLUMN_NAME_NAME = "_NAME";
        public static final String COLUMN_NAME_IMAGE = "_IMAGE";

    }
}
