package com.niwe.erp.core.web.controller;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.niwe.erp.core.domain.CoreMenu;
import com.niwe.erp.core.repository.CoreMenuRepository;
import com.niwe.erp.core.repository.CorePermissionRepository;

@Controller
@RequestMapping("/admin/menus")
@PreAuthorize("hasAuthority('USER_MANAGE') or hasRole('ADMIN')")
public class CoreAdminMenuController {

    private final CoreMenuRepository coreMenuRepository;
    private final CorePermissionRepository corePermissionRepository;

    public CoreAdminMenuController(CoreMenuRepository coreMenuRepository,CorePermissionRepository corePermissionRepository) {
        this.coreMenuRepository = coreMenuRepository;
        this.corePermissionRepository = corePermissionRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("menus", coreMenuRepository.findAllByActiveTrueOrderBySortOrderAsc());
        return "admin/menus/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("menu", new CoreMenu());
        model.addAttribute("allPermissions", corePermissionRepository.findAll());
        model.addAttribute("allMenus", coreMenuRepository.findAllByActiveTrueOrderBySortOrderAsc());
        return "admin/menus/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute CoreMenu menu, @RequestParam(required = false) String parentId) {
        if (parentId != null) {
            menu.setParent(coreMenuRepository.getReferenceById(UUID.fromString(parentId)));
        }
        coreMenuRepository.save(menu);
        return "redirect:/admin/menus";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        model.addAttribute("menu", coreMenuRepository.getReferenceById(UUID.fromString(id)));
        model.addAttribute("allPermissions", corePermissionRepository.findAll());
        model.addAttribute("allMenus", coreMenuRepository.findAllByActiveTrueOrderBySortOrderAsc());
        return "admin/menus/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
    	coreMenuRepository.deleteById(UUID.fromString(id));
        return "redirect:/admin/menus";
    }
}

