package com.nowcent.translation.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author orangeboy
 * @version 1.0
 * @date 2020/8/5 21:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecretPojo {
    private String appId = "20200803000532380";
    private String secretKey = "VQoeTSBUg1YkptcUNeLr";
    private Date time = new Date();
}
