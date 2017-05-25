package com.xuxl.redis.client.listener;

import com.xuxl.redis.client.data.ConfigManager;

import java.util.Map;

public class ItemChangeListener {

    private ItemChangeListener() {
    }

    public static void handleItems(Map<String, String> map) {
        ConfigManager.getInstance().setItems(map);
    }
}
