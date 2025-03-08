package com.hhm.api.config.application;

import com.hhm.api.service.RoleService;
import com.hhm.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SeedDatabaseRunner implements CommandLineRunner {

    private final RoleService roleService;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        log.warn("Starting initialize seed database...");

        roleService.init();
        userService.init();

        log.info("Finish initialize seed database!!!");
    }
}
