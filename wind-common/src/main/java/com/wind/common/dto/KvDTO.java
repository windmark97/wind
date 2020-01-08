package com.wind.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2020/1/8 13:21
 **/
@Data
public class KvDTO<K, V> implements Serializable {
    private static final long serialVersionUID = -3452872597525404926L;

    private K key;
    private V value;

    private List<V> values;

    private List<KvDTO> children;

    private String user;
    private Date date;

    public KvDTO() {
        super();
    }

    public KvDTO(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public KvDTO(K key, List<V> values) {
        this.key = key;
        this.values = values;
    }

    public KvDTO(K key) {
        this.key = key;
    }

    public void addChildren(KvDTO<K, V> dto) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(dto);
    }

    public void addChildrens(List<KvDTO> dtos) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.addAll(dtos);
    }
}

