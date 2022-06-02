package site.code4fun.constant;

import java.util.Arrays;
import java.util.List;

public interface AppConstants {
    String TOKEN_COOKIE_NAME = "Token";
    int DEFAULT_PAGE_SIZE = 20;
    String DEFAULT_SORT_COLUMN = "id";
    String DEFAULT_SORT_DIRECTION = "ASC";
    List<String> SORT_LIST = Arrays.asList("ASC", "DESC");
}
