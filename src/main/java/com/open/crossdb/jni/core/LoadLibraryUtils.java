package com.open.crossdb.jni.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicReference;

public class LoadLibraryUtils {

    private static final AtomicReference<LibraryState> libraryLoaded = new AtomicReference(LibraryState.NOT_LOADED);
    private static final String tempFileSuffix = Environment.getJniLibraryExtension();

    public static synchronized void loadLibrary(String libraryName) {
            try {
                System.loadLibrary(libraryName);
            } catch (Throwable var5) {
                try {
                    System.load(loadLibraryFromJarToTemp(libraryName).getAbsolutePath());
                } catch (Throwable e) {
                    libraryLoaded.set(LibraryState.NOT_LOADED);
                    throw new RuntimeException("Unable to load the library", e);
                }
                libraryLoaded.set(LibraryState.LOADED);
            }
    }

    private static File loadLibraryFromJarToTemp(String libraryName) throws Throwable {

        String jniLibraryFileName = Environment.getJniLibraryFileName(libraryName);
        System.out.println("-----------------------jniLibraryFileName:" +jniLibraryFileName);

        InputStream jniLibStream = LoadLibraryUtils.class.getClassLoader().getResourceAsStream(jniLibraryFileName);
        Throwable throwable = null;

        File file;
        try {
            if (jniLibStream != null) {
                file = createTemp(jniLibraryFileName);
                Files.copy(jniLibStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                return file;
            }else {
                throwable = new Throwable("jar miss lib");
                throw throwable;
            }
        } catch (Throwable var33) {
            throwable = var33;
            throw var33;
        } finally {
            if (jniLibStream != null) {
                if (throwable != null) {
                    try {
                        jniLibStream.close();
                    } catch (Throwable var30) {
                        throwable.addSuppressed(var30);
                    }
                } else {
                    jniLibStream.close();
                }
            }

        }

    }


    private static File createTemp(String name) throws IOException {
        File var3 = File.createTempFile(name.replace(".dll", ""), tempFileSuffix);

        if (var3.exists()) {
            var3.deleteOnExit();
            return var3;
        } else {
            throw new RuntimeException("File " + var3.getAbsolutePath() + " does not exist.");
        }
    }

    private static enum LibraryState {
        NOT_LOADED,
        LOADING,
        LOADED;

        private LibraryState() {
        }
    }
}
