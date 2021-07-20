package com.entiv.autoresetworld;

import com.entiv.autoresetworld.scheduletask.ScheduleConfig;
import com.entiv.autoresetworld.scheduletask.ScheduleTask;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
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
                int day = section.getInt("保留天数", 0);

                Instant expiredTime = fileTime.plus(Period.ofDays(day));
                boolean isExpiredFile = fileTime.isAfter(Instant.from(expiredTime));

                try {
                    if (isExpiredFile || day <= 0) {
                        Files.delete(file);
                        Message.sendConsole("&a文件 &e" + file.getFileName() + " &a已删除");
                    }

                } catch (FileSystemException e) {
                    Message.sendConsole("&c文件 &e" + Paths.get(e.getFile()).getFileName() + " &c无法删除, 已跳过");
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                return FileVisitResult.CONTINUE;
            }

        };

        Message.sendConsole("&a━━━━━━━━━━━━━━  &e正在删除" + name + "  &a━━━━━━━━━━━━━━");
        Message.sendConsole(" ");

        try {

            File dataFolder = Main.getInstance().getDataFolder();
            String dir = dataFolder.getAbsoluteFile().getParentFile().getParentFile().toString();

            for (String configPath : section.getStringList("文件路径")) {
                Path path = Paths.get(dir, configPath);
                if (Files.exists(path)) {
                    Files.walkFileTree(path, visitor);
                } else {
                    Message.sendConsole("&c路径 &e" + path + " &c不存在或没有文件, 已跳过");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Message.sendConsole(" ");
        Message.sendConsole("&a━━━━━━━━━━━━━━  &e" + name + "删除完毕  &a━━━━━━━━━━━━━━");

        scheduleConfig.setupNextScheduleTaskTime();
    }
}
