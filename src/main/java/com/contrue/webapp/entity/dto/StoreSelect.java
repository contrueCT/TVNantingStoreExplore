package com.contrue.webapp.entity.dto;

import com.contrue.webapp.entity.po.Store;
import com.contrue.webapp.entity.po.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author confff
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreSelect {
    Store store;
    User user;

}
