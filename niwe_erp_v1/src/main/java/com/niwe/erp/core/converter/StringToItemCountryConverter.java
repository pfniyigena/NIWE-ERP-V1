package com.niwe.erp.core.converter;
import java.util.UUID;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.niwe.erp.core.domain.CoreCountry;
import com.niwe.erp.core.service.CoreCountryService;

@Component
public class StringToItemCountryConverter implements Converter<String, CoreCountry> {

    private final CoreCountryService coreCountryService;

    public StringToItemCountryConverter(CoreCountryService coreCountryService) {
        this.coreCountryService = coreCountryService;
    }

    @Override
    public CoreCountry convert(String id) {
    	if (id == null || id.trim().isEmpty()) {
            return null; // handle empty <option value="">
        }
        UUID uuid = UUID.fromString(id);
        return coreCountryService.findById(uuid);
    }
}
