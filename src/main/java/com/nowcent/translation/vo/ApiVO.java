package com.nowcent.translation.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author orangeboy
 * @version 1.0
 * @date 2020/8/5 21:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiVO<T> {
    private Integer code;
    private String message;
    private T data;
}
