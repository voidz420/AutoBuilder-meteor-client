package com.voidz.autobuilder;

import com.voidz.autobuilder.modules.AutoBuilder;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class AutoBuilderAddon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("AutoBuilder");

    @Override
    public void onInitialize() {
        LOG.info("Initializing AutoBuilder Addon");
        Modules.get().add(new AutoBuilder());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.voidz.autobuilder";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("voidz420", "AutoBuilder-meteor-client");
    }
}
