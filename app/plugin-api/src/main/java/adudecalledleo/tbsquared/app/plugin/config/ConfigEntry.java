package adudecalledleo.tbsquared.app.plugin.config;

import adudecalledleo.tbsquared.data.DataKey;

public record ConfigEntry<T>(ConfigEntryType<T> type, DataKey<T> key, String category, String name, String description) { }
