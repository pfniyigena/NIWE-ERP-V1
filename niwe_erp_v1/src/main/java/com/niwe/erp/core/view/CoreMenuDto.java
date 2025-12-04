package com.niwe.erp.core.view;
 

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Data;
@Data
public class CoreMenuDto {
    private UUID id;
    private String title;
    private String url;
    private String icon;
    private List<CoreMenuDto> children = new ArrayList<>();

    public CoreMenuDto(UUID id, String title, String url, String icon) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.icon = icon;
    }
}

