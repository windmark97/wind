package com.wind.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2020/1/8 13:19
 **/
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class VtDTO {

    private int value;
    private String text;
    private Boolean status;

    public VtDTO(int value, String text) {
        this.value = value;
        this.text = text;
        this.status = true;
    }

}
