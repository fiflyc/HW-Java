package com.example.md5;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MD5Test {

    @Test
    void fileMD5_EqualFiles_EqualHashes() throws IOException {
        var in1 = new FileInputStream("./src/test/resources/file1.test1");
        var in2 = new FileInputStream("./src/test/resources/file2.test1");

        assertEquals(MD5.fileMD5(in1), MD5.fileMD5(in2));
    }

    @Test
    void dirMD5SingleThread_EqualDirs_EqualHashes() throws IOException {
        var dir1 = new File("./src/test/resources/dir1_test2/dir");
        var dir2 = new File("./src/test/resources/dir2_test2/dir");

        assertEquals(MD5.dirMD5SingleThread(dir1), MD5.dirMD5SingleThread(dir2));
    }

    @Test
    void dirMD5MultiThread_EqualDirs_EqualHashes() throws IOException {
        var dir1 = new File("./src/test/resources/dir1_test2/dir");
        var dir2 = new File("./src/test/resources/dir2_test2/dir");

        assertEquals(MD5.dirMD5MultiThread(dir1), MD5.dirMD5MultiThread(dir2));
    }
}