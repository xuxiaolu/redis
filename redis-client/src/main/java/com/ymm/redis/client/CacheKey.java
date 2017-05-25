package com.ymm.redis.client;

import java.io.Serializable;
import java.util.Objects;

public class CacheKey implements Serializable {

    private static final long serialVersionUID = 771968874205193318L;

    private String item;

    private String key;

    public CacheKey(String item, String key) {
        this.item = item;
        this.key = key;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheKey key1 = (CacheKey) o;
        return Objects.equals(item, key1.item) &&
                Objects.equals(key, key1.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, key);
    }

    @Override
    public String toString() {
        return "CacheKey{" +
                "item='" + item + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

}
