package site.code4fun.constant;

import java.util.Arrays;
import java.util.List;

public interface AppConstants {
    String TABLE_PREFIX = "";
    String TOKEN_COOKIE_NAME = "Token";
    int DEFAULT_PAGE_SIZE = 20;
    String DEFAULT_SORT_COLUMN = "id";
    List<String> SORT_LIST = Arrays.asList("ASC", "DESC");
}
