package com.ymm.redis.admin.service;


import com.ymm.redis.admin.entity.Item;

import java.util.List;

public interface ItemService {

    boolean addItem(String bizName, String name, String value, String desc);

    boolean deleteItem(String key);

    boolean updateItem(String bizName, String name, String value, String desc);

    List<Item> getAvailableItemsByBizName(String bizName);

    List<Item> getUnAvailableItemsByBizName(String bizName);

    List<Item> getAllItemsByBizName(String bizName);

    Item getAvailableItemByName(String name);

    Item getUnAvailableItemByName(String name);

    Item getItemNoMatterIdDeleteByName(String name);


}
