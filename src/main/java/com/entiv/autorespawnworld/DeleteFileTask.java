package com.entiv.autorespawnworld;

import com.entiv.autorespawnworld.scheduletask.ScheduleConfig;
import com.entiv.autorespawnworld.scheduletask.ScheduleTask;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.Period;

public class DeleteFileTask implements ScheduleTask {

    private final String name;
    private final ScheduleConfig scheduleConfig;
    private final ConfigurationSection section;

    public DeleteFileTask(String name) {
        this.name = name;

        String path = "自动删除文件." + name;

        section = Main.getInstance().getConfig().getConfigurationSection(path);
        scheduleConfig = new ScheduleConfig(path);
    }

    @Override
    public boolean isExpired() {
        return scheduleConfig.isExpired();
    }

    @Override
    public void runTask() {

        SimpleFileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                Instant fileTime = Files.readAttributes(file, BasicFileAttributes.class).lastAccessTime().toInstant();
                int day = section.getInt("过期天数");

                Instant expiredTime = fileTime.plus(Period.ofDays(day));
                boolean isExpiredFile = fileTime.isAfter(Instant.from(expiredTime));

                try {

                    if (isExpiredFile || day <= 0) {
                        Files.delete(file);
                        Message.sendConsole("&a文件 &e" + file.getFileName() + " &a已删除");
                    }

                } catch (FileSystemException e) {
                    Message.sendConsole("&a文件 &e" + Paths.get(e.getFile()).getFileName() + " &a无法删除, 已跳过");
                }
//
                return FileVisitResult.CONTINUE;
            }

        };

        Message.sendConsole("&a━━━━━━━━━━━━━━  &e正在删除" + name + "  &a━━━━━━━━━━━━━━");
        Message.sendConsole(" ");

        try {

            String serverPath = Bukkit.getServer().getWorldContainer().getCanonicalPath();

            for (String configPath : section.getStringList("文件路径")) {
                Path path = Paths.get(serverPath, configPath);
                Files.walkFileTree(path, visitor);
            }

        } catch (NoSuchFileException e) {
            Message.sendConsole("&9AutoSpawnWorld &6>> &c路径 " + e.getFile() + "不存在, 请检查配置文件");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Message.sendConsole(" ");
        Message.sendConsole("&a━━━━━━━━━━━━━━  &e" + name + "删除完毕  &a━━━━━━━━━━━━━━");

        scheduleConfig.setupNextScheduleTaskTime();
    }
}
