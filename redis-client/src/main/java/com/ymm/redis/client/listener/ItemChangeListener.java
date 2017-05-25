package com.ymm.redis.client.listener;

import com.ymm.redis.client.data.ConfigManager;

import java.util.Map;

public class ItemChangeListener {

    private ItemChangeListener() {
    }

    public static void handleItems(Map<String, String> map) {
        ConfigManager.getInstance().setItems(map);
    }
}
