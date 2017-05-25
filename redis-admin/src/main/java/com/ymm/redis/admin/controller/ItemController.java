package com.ymm.redis.admin.controller;


import com.ymm.redis.admin.entity.Item;
import com.ymm.redis.admin.response.BizResponse;
import com.ymm.redis.admin.service.ItemService;
import com.ymm.redis.admin.utils.CodeConstants;
import com.ymm.redis.admin.vo.ItemVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Resource
    private ItemService itemService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BizResponse getList(@RequestParam String bizName) {
        bizName = StringUtils.trimToNull(bizName);
        Assert.notNull(bizName, "业务线名字不能为空");

        List<Item> items = itemService.getAvailableItemsByBizName(bizName);

        BizResponse response = new BizResponse();
        if (items != null && items.size() > 0) {
            List<ItemVo> itemVos = new ArrayList<>();
            ItemVo itemVo;
            for (Item item : items) {
                itemVo = new ItemVo();
                BeanUtils.copyProperties(item, itemVo);
                itemVos.add(itemVo);
            }
            response.setData(itemVos);
        }
        return response;
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public BizResponse getDetail(@RequestParam String name) {
        name = StringUtils.trimToNull(name);
        Assert.notNull(name, "Item名字不能为空");

        Item item = itemService.getAvailableItemByName(name);

        BizResponse response = new BizResponse();
        if (item != null) {
            ItemVo itemVo = new ItemVo();
            BeanUtils.copyProperties(item, itemVo);
            response.setData(itemVo);
        }
        return response;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public BizResponse delItem(@RequestParam String name) {
        name = StringUtils.trimToNull(name);
        Assert.notNull(name, "Item名字不能为空");

        boolean result = itemService.deleteItem(name);

        BizResponse response = new BizResponse();

        if (!result) {
            response.setCode(CodeConstants.DEL_ITEM_ERROR_CODE);
            response.setMsg(CodeConstants.DEL_ITEM_ERROR_MSG);
        }
        return response;

    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BizResponse addItem(
            @RequestParam String bizName,
            @RequestParam String name,
            @RequestParam String value,
            String desc) {
        bizName = StringUtils.trimToNull(bizName);
        Assert.notNull(bizName, "业务线名字不能为空");

        name = StringUtils.trimToNull(name);
        Assert.notNull(name, "Item名字不能为空");

        value = StringUtils.trimToNull(value);
        Assert.notNull(value, "Item值不能为空");

        boolean result = itemService.addItem(bizName, name, value, desc);

        BizResponse response = new BizResponse();
        if (!result) {
            response.setCode(CodeConstants.ADD_ITEM_ERROR_CODE);
            response.setMsg(CodeConstants.ADD_ITEM_ERROR_MSG);
        }
        return response;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BizResponse updateItem(
            @RequestParam String bizName,
            @RequestParam String name,
            @RequestParam String value, String desc) {
        bizName = StringUtils.trimToNull(bizName);
        Assert.notNull(bizName, "业务线名字不能为空");

        name = StringUtils.trimToNull(name);
        Assert.notNull(name, "Item名字不能为空");

        value = StringUtils.trimToNull(value);
        Assert.notNull(value, "Item值不能为空");

        desc = StringUtils.trimToNull(desc);

        boolean result = itemService.updateItem(bizName, name, value, desc);

        BizResponse response = new BizResponse();
        if (!result) {
            response.setCode(CodeConstants.UPDATE_ITEM_ERROR_CODE);
            response.setMsg(CodeConstants.UPDATE_ITEM_ERROR_MSG);
        }
        return response;
    }

}
