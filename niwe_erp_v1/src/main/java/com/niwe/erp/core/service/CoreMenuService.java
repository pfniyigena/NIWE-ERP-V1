package com.niwe.erp.core.service;

import org.springframework.stereotype.Service;

import com.niwe.erp.core.domain.CoreMenu;
import com.niwe.erp.core.repository.CoreMenuRepository;
import com.niwe.erp.core.view.CoreMenuDto;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CoreMenuService {

    private final CoreMenuRepository coreMenuRepository;

    public CoreMenuService(CoreMenuRepository coreMenuRepository) {
        this.coreMenuRepository = coreMenuRepository;
    }

    /**
     * Return menus visible to a user based on their authorities.
     */
    public List<CoreMenuDto> getVisibleMenus(Set<String> authorities) {
        List<CoreMenu> allMenus = coreMenuRepository.findAllByActiveTrueOrderBySortOrderAsc();

        // Filter menus where permission is null (public) or user has that permission
        List<CoreMenu> visibleMenus = allMenus.stream()
            .filter(m -> m.getRequiredPermission() == null
                      || authorities.contains(m.getRequiredPermission())
                      || authorities.contains("ROLE_ADMIN"))
            .toList();

        // Build tree structure
        Map<UUID, CoreMenuDto> dtoMap = visibleMenus.stream()
            .collect(Collectors.toMap(CoreMenu::getId,
                m -> new CoreMenuDto(m.getId(), m.getTitle(), m.getUrl(), m.getIcon())));

        List<CoreMenuDto> rootMenus = new ArrayList<>();

        for (CoreMenu m : visibleMenus) {
            if (m.getParent() != null && dtoMap.containsKey(m.getParent().getId())) {
                dtoMap.get(m.getParent().getId()).getChildren().add(dtoMap.get(m.getId()));
            } else {
                rootMenus.add(dtoMap.get(m.getId()));
            }
        }

        // Sort children recursively
        sortMenuTree(rootMenus);
        return rootMenus;
    }

    private void sortMenuTree(List<CoreMenuDto> menus) {
        menus.sort(Comparator.comparing(CoreMenuDto::getTitle));
        for (CoreMenuDto child : menus) {
            if (!child.getChildren().isEmpty()) {
                sortMenuTree(child.getChildren());
            }
        }
    }
}
