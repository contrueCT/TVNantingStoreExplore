package com.contrue.mapper;

import com.contrue.entity.dto.SubscribeDTO;
import com.contrue.entity.po.Subscribe;

/**
 * @author confff
 */
public interface SubscribesMapper {
    int insertSubscribes(Subscribe subscribe);

    int deleteSubscribes(Subscribe subscribe);
}
