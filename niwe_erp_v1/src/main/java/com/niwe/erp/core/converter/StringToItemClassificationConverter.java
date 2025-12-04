package com.niwe.erp.core.converter;
import java.util.UUID;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.niwe.erp.core.domain.CoreItemClassification;
import com.niwe.erp.core.service.CoreItemClassificationService;

@Component
public class StringToItemClassificationConverter implements Converter<String, CoreItemClassification> {

    private final CoreItemClassificationService coreItemClassificationService;

    public StringToItemClassificationConverter(CoreItemClassificationService coreItemClassificationService) {
        this.coreItemClassificationService = coreItemClassificationService;
    }

    @Override
    public CoreItemClassification convert(String id) {
    	if (id == null || id.trim().isEmpty()) {
            return null; // handle empty <option value="">
        }
        UUID uuid = UUID.fromString(id);
        return coreItemClassificationService.findById(uuid);
    }
}
