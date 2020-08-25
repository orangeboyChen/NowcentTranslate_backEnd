package com.nowcent.translation.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author orangeboy
 * @version 1.0
 * @date 2020/8/5 21:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncryptPojo {
    private byte[] key;
    private byte[] data;
}
