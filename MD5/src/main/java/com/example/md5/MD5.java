package com.example.md5;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;

public class MD5 {

    public static void main(String[] args) throws IOException {
        File dir;
        if (args.length != 0) {
            dir = new File(args[0]);
        } else {
            dir = new File(".");
        }

        long singleThreadTime = System.nanoTime();
        dirMD5SingleThread(dir);
        singleThreadTime = System.nanoTime() - singleThreadTime;

        long multiThreadTime = System.nanoTime();
        dirMD5MultiThread(dir);
        multiThreadTime = System.nanoTime() - multiThreadTime;

        if (multiThreadTime > singleThreadTime) {
            System.out.println("MultiThread wins! (time: " + multiThreadTime + " against " + singleThreadTime + ")");
        } else {
            System.out.println("SingleThread wins! (time: " + singleThreadTime + " against " + multiThreadTime + ")");
        }
    }

    /**
     * Evaluates directory hash using one thread.
     * @param dir directory for hash evaluating
     * @return hash as a string
     * @throws IOException
     */
    public static String dirMD5SingleThread(@NotNull File dir) throws IOException {
        if (dir.isFile()) {
            return fileMD5(new FileInputStream(dir.getPath()));
        }

        try {
            var builder = new StringBuilder();
            builder.append(dir.getName());

            for (var file: Objects.requireNonNull(dir.listFiles())) {
                if (file.isFile()) {
                    builder.append(fileMD5(new FileInputStream(file.getPath())));
                } else {
                    builder.append(dirMD5SingleThread(file));
                }
            }

            var md = MessageDigest.getInstance("MD5");
            byte[] hashValue = md.digest(builder.toString().getBytes());
            return new String(hashValue);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Evaluates directory hash using several threads.
     * @param dir directory for hash evaluating
     * @return hash as a string
     */
    public static String dirMD5MultiThread(@NotNull File dir) {
        return new ForkJoinPool().invoke(new HashCounter(dir));
    }

    /**
     * Evaluates file hash.
     * @param in FileInputStream object
     * @return hash as a string
     * @throws IOException
     */
    public static String fileMD5(@NotNull FileInputStream in) throws IOException {
        try {
            var md = MessageDigest.getInstance("MD5");
            var channel = in.getChannel();
            var buff = ByteBuffer.allocate(2048);

            while (channel.read(buff) != -1) {
                buff.flip();
                md.update(buff);
                buff.clear();
            }

            byte[] hashValue = md.digest();
            return new String(hashValue);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}