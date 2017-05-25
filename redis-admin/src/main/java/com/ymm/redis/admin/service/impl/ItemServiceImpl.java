package com.ymm.redis.admin.service.impl;


import com.ymm.redis.admin.entity.BusinessLine;
import com.ymm.redis.admin.entity.Item;
import com.ymm.redis.admin.entity.ItemCriteria;
import com.ymm.redis.admin.exception.BizException;
import com.ymm.redis.admin.mapper.ItemMapper;
import com.ymm.redis.admin.service.BusinessLineService;
import com.ymm.redis.admin.service.ItemService;
import com.ymm.redis.admin.utils.CodeConstants;
import com.ymm.redis.admin.zookeeper.ZkClient;
import com.ymm.redis.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private BusinessLineService businessLineService;

    @Override
    public boolean addItem(String bizName, String name, String value, String desc) {
        BusinessLine businessLine = businessLineService.getBusinessLineByName(bizName);
        if (businessLine == null) {
            throw new BizException(CodeConstants.BUSINESS_LINE_NOT_EXISTS_ERROR_CODE, String.format("%s业务线不存在", bizName));
        }

        Item itemByKey = this.getItemNoMatterIdDeleteByName(name);
        if (itemByKey != null) {
            throw new BizException(CodeConstants.ITEM_EXISTS_ERROR_CODE, String.format("%s已存在", name));
        }

        Date now = new Date();

        Item item = new Item();
        item.setBusinessName(bizName);
        item.setName(name);
        item.setValue(value);
        item.setDescription(desc);
        item.setCreateTime(now);
        item.setModifyTime(now);
        item.setIsDelete(0);

        boolean result = 1 == itemMapper.insertSelective(item);

        if (result) {
            String itemPath = Constants.getRealItemPath(businessLine.getClusterName(), bizName, name);
            ZkClient.getInstance().create(itemPath, value, false);
        }
        return result;
    }

    @Override
    public boolean deleteItem(String name) {
        Item item = this.getAvailableItemByName(name);
        if (item == null) {
            throw new BizException(CodeConstants.ITEM_NOT_EXISTS_ERROR_CODE, String.format("%s不存在", name));
        }

        BusinessLine businessLine = businessLineService.getBusinessLineByName(item.getBusinessName());
        if (businessLine == null) {
            throw new BizException(CodeConstants.BUSINESS_LINE_NOT_EXISTS_ERROR_CODE, String.format("%s业务线不存在", item.getBusinessName()));
        }

        item.setIsDelete(1);
        item.setModifyTime(new Date());

        boolean result = 1 == itemMapper.updateByPrimaryKeySelective(item);

        if (result) {
            String itemPath = Constants.getRealItemPath(businessLine.getClusterName(), item.getBusinessName(), name);
            ZkClient.getInstance().delete(itemPath);
        }

        return result;
    }

    @Override
    public boolean updateItem(String bizName, String name, String value, String desc) {

        BusinessLine businessLine = businessLineService.getBusinessLineByName(bizName);

        if (businessLine == null) {
            throw new BizException(CodeConstants.BUSINESS_LINE_NOT_EXISTS_ERROR_CODE, String.format("%s业务线不存在", bizName));
        }

        Item item = this.getAvailableItemByName(name);

        if (item == null) {
            throw new BizException(CodeConstants.ITEM_NOT_EXISTS_ERROR_CODE, String.format("%s不存在", name));
        }

        item.setValue(value);
        item.setDescription(desc);
        item.setModifyTime(new Date());

        boolean result = 1 == itemMapper.updateByPrimaryKeySelective(item);

        if (result) {
            String itemPath = Constants.getRealItemPath(businessLine.getClusterName(), bizName, name);
            ZkClient.getInstance().setData(itemPath, value);
        }
        return result;
    }


    @Override
    public List<Item> getAvailableItemsByBizName(String bizName) {
        ItemCriteria itemCriteria = new ItemCriteria();
        itemCriteria.createCriteria().andBusinessNameEqualTo(bizName).andIsDeleteEqualTo(0);
        return itemMapper.selectByExample(itemCriteria);
    }

    @Override
    public List<Item> getUnAvailableItemsByBizName(String bizName) {
        ItemCriteria itemCriteria = new ItemCriteria();
        itemCriteria.createCriteria().andBusinessNameEqualTo(bizName).andIsDeleteEqualTo(1);
        return itemMapper.selectByExample(itemCriteria);
    }

    @Override
    public List<Item> getAllItemsByBizName(String bizName) {
        ItemCriteria itemCriteria = new ItemCriteria();
        itemCriteria.createCriteria().andBusinessNameEqualTo(bizName);
        return itemMapper.selectByExample(itemCriteria);
    }

    @Override
    public Item getAvailableItemByName(String name) {
        ItemCriteria itemCriteria = new ItemCriteria();
        itemCriteria.createCriteria().andNameEqualTo(name).andIsDeleteEqualTo(0);
        List<Item> items = itemMapper.selectByExample(itemCriteria);
        return items == null || items.isEmpty() ? null : items.get(0);
    }

    @Override
    public Item getUnAvailableItemByName(String name) {
        ItemCriteria itemCriteria = new ItemCriteria();
        itemCriteria.createCriteria().andNameEqualTo(name).andIsDeleteEqualTo(1);
        List<Item> items = itemMapper.selectByExample(itemCriteria);
        return items == null || items.isEmpty() ? null : items.get(0);
    }

    @Override
    public Item getItemNoMatterIdDeleteByName(String name) {
        ItemCriteria itemCriteria = new ItemCriteria();
        itemCriteria.createCriteria().andNameEqualTo(name);
        List<Item> items = itemMapper.selectByExample(itemCriteria);
        return items == null || items.isEmpty() ? null : items.get(0);
    }
}
