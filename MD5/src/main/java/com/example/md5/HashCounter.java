package com.example.md5;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.RecursiveTask;

/** A recursive task evaluates a directory hash. */
class HashCounter extends RecursiveTask<String> {

    @NotNull private final File dir;

    HashCounter(@NotNull File dir) {
        this.dir = dir;
    }

    @Override
    protected String compute() {
        if (dir.isFile()) {
            try {
                return MD5.fileMD5(new FileInputStream(dir.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ArrayList<HashCounter> subTasks = new ArrayList<>();
        var builder = new StringBuilder();
        builder.append(dir.getName());

        for (var file: Objects.requireNonNull(dir.listFiles())) {
            HashCounter task = new HashCounter(file);
            task.fork();
            subTasks.add(task);
        }

        for (var task: subTasks) {
            builder.append(task.join());
        }

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hashValue = md.digest(builder.toString().getBytes());
        return new String(hashValue);
    }
}
