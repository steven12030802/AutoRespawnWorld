package com.entiv.autoresetworld.task;

import com.entiv.autoresetworld.Main;
import com.entiv.autoresetworld.utils.Message;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.Period;

public class DeleteFileTask extends ScheduleTask {

    public DeleteFileTask(String name) {
        super("自动删除文件." + name, name);
    }

    public String getName() {
        return name;
    }

    @Override
    public void runTask() {

        SimpleFileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                Instant fileTime = Files.readAttributes(file, BasicFileAttributes.class).lastModifiedTime().toInstant();

                int day = config.getInt("保留天数", 0);

                Instant expiredTime = fileTime.plus(Period.ofDays(day));
                boolean isExpiredFile = Instant.now().isAfter(expiredTime);

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

            for (String configPath : config.getStringList("文件路径")) {
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
    }
}
