package com.news.smartnewsaggregator.domain.model

enum class SortOrder {
    DATE_DESC,
    DATE_ASC,
    SOURCE_ASC,
    SOURCE_DESC
}

enum class Category(val value: String) {
    ALL("All"),
    TECHNOLOGY("Technology"),
    SPORTS("Sports"),
    CULTURE("Culture")
}
