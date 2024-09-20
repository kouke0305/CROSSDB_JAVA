package com.open.crossdb.jni.core;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class Environment {
    private static String OS = System.getProperty("os.name").toLowerCase(Locale.getDefault());
    private static String ARCH = System.getProperty("os.arch").toLowerCase(Locale.getDefault());
    private static String MUSL_ENVIRONMENT = System.getenv("ROCKSDB_MUSL_LIBC");
    private static final String LIBC_MUSL_PREFIX = "libc.musl";
    private static final String SPARCV9 = "sparcv9";
    private static Boolean MUSL_LIBC = null;

    public Environment() {
    }

    public static boolean isAarch64() {
        return ARCH.contains("aarch64");
    }

    public static boolean isPowerPC() {
        return ARCH.contains("ppc");
    }

    public static boolean isS390x() {
        return ARCH.contains("s390x");
    }

    public static boolean isRiscv64() {
        return ARCH.contains("riscv64");
    }

    public static boolean isWindows() {
        return OS.contains("win");
    }

    public static boolean isFreeBSD() {
        return OS.contains("freebsd");
    }

    public static boolean isMac() {
        return OS.contains("mac");
    }

    public static boolean isAix() {
        return OS.contains("aix");
    }

    public static boolean isUnix() {
        return OS.contains("nix") || OS.contains("nux");
    }

    public static boolean isMuslLibc() {
        if (MUSL_LIBC == null) {
            MUSL_LIBC = initIsMuslLibc();
        }

        return MUSL_LIBC;
    }

    static boolean initIsMuslLibc() {
        if ("true".equalsIgnoreCase(MUSL_ENVIRONMENT)) {
            return true;
        } else if ("false".equalsIgnoreCase(MUSL_ENVIRONMENT)) {
            return false;
        } else {
            try {
                Process var0 = (new ProcessBuilder(new String[]{"/usr/bin/env", "sh", "-c", "ldd /usr/bin/env | grep -q musl"})).start();
                if (var0.waitFor() == 0) {
                    return true;
                }
            } catch (InterruptedException | IOException var8) {
            }

            File var9 = new File("/lib");
            if (var9.exists() && var9.isDirectory() && var9.canRead()) {
                String var1;
                if (isPowerPC()) {
                    var1 = "libc.musl-ppc64le.so.1";
                } else if (isAarch64()) {
                    var1 = "libc.musl-aarch64.so.1";
                } else if (isS390x()) {
                    var1 = "libc.musl-s390x.so.1";
                } else {
                    var1 = "libc.musl-x86_64.so.1";
                }

                File var2 = new File(var9, var1);
                if (var2.exists() && var2.canRead()) {
                    return true;
                }

                File[] var3 = var9.listFiles();
                if (var3 == null) {
                    return false;
                }

                File[] var4 = var3;
                int var5 = var3.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    File var7 = var4[var6];
                    if (var7.getName().startsWith("libc.musl")) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public static boolean isSolaris() {
        return OS.contains("sunos");
    }

    public static boolean isOpenBSD() {
        return OS.contains("openbsd");
    }

    public static boolean is64Bit() {
        if (ARCH.contains("sparcv9")) {
            return true;
        } else {
            return ARCH.indexOf("64") > 0;
        }
    }

    public static String getSharedLibraryName(String var0) {
        return var0 + "jni";
    }

    public static String getSharedLibraryFileName(String var0) {
        return appendLibOsSuffix("lib" + getSharedLibraryName(var0), true);
    }

    public static String getLibcName() {
        return isMuslLibc() ? "musl" : null;
    }

    private static String getLibcPostfix() {
        String var0 = getLibcName();
        return var0 == null ? "" : "-" + var0;
    }

    public static String getJniLibraryName(String var0) {
        String var1;
        if (isUnix()) {
            var1 = is64Bit() ? "64" : "32";
            if (!isPowerPC() && !isAarch64() && !isRiscv64()) {
                return isS390x() ? String.format("%sjni-linux-%s", var0, ARCH) : String.format("%sjni-linux%s%s", var0, var1, getLibcPostfix());
            } else {
                return String.format("%sjni-linux-%s%s", var0, ARCH, getLibcPostfix());
            }
        } else if (isMac()) {
            if (is64Bit()) {
                if (isAarch64()) {
                    var1 = "arm64";
                } else {
                    var1 = "x86_64";
                }

                return String.format("%sjni-osx-%s", var0, var1);
            } else {
                return String.format("%sjni-osx", var0);
            }
        } else if (isFreeBSD()) {
            return String.format("%sjni-freebsd%s", var0, is64Bit() ? "64" : "32");
        } else if (isAix() && is64Bit()) {
            return String.format("%sjni-aix64", var0);
        } else if (isSolaris()) {
            var1 = is64Bit() ? "64" : "32";
            return String.format("%sjni-solaris%s", var0, var1);
        } else if (isWindows() && is64Bit()) {
            return String.format("%sjni-win64", var0);
        } else if (isOpenBSD()) {
            return String.format("%sjni-openbsd%s", var0, is64Bit() ? "64" : "32");
        } else {
            throw new UnsupportedOperationException(String.format("Cannot determine JNI library name for ARCH='%s' OS='%s' name='%s'", ARCH, OS, var0));
        }
    }

    public static String getFallbackJniLibraryName(String var0) {
        return isMac() && is64Bit() ? String.format("%sjni-osx", var0) : null;
    }

    public static String getJniLibraryFileName(String var0) {
        return appendLibOsSuffix("lib" + getJniLibraryName(var0), false);
    }

    public static String getFallbackJniLibraryFileName(String var0) {
        String var1 = getFallbackJniLibraryName(var0);
        return var1 == null ? null : appendLibOsSuffix("lib" + var1, false);
    }

    private static String appendLibOsSuffix(String var0, boolean var1) {
        if (!isUnix() && !isAix() && !isSolaris() && !isFreeBSD() && !isOpenBSD()) {
            if (isMac()) {
                return var0 + (var1 ? ".dylib" : ".jnilib");
            } else if (isWindows()) {
                return var0 + ".dll";
            } else {
                throw new UnsupportedOperationException();
            }
        } else {
            return var0 + ".so";
        }
    }

    public static String getJniLibraryExtension() {
        if (isWindows()) {
            return ".dll";
        } else {
            return isMac() ? ".jnilib" : ".so";
        }
    }

}
