package com.example.springboot3demo.utils;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import lombok.Synchronized;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by Administrator on 2018/6/26 0026.
 * 系统工具类
 */
public class SystemUtils {


    /**
     * 获取process的pid
     *
     * @param process process
     * @return
     */
    @Synchronized
    public static Long getPidOfProcess(Process process) {
        long pid = -1;
        try {
            //依赖JDK的不同，Process的实现类可能是java.lang.UNIXProcess或java.lang.ProcessImpl
            if (process.getClass().getName().equals("java.lang.UNIXProcess") || process.getClass().getName().equals("java.lang.ProcessImpl")) {
                Field field = process.getClass().getDeclaredField("pid");
                field.setAccessible(true);
                pid = field.getLong(process);
                field.setAccessible(false);
            }
        } catch (Exception e) {
            pid = -1;
        }
        return pid;
    }


    /**
     * 杀死指定进程数，即包括process进程的所有子进程
     *
     * @param process
     */
    public static void killProcessTree(Process process) throws IOException {
        if (SystemUtils.isLinux()) {//如果是linux系统
            Long pid = getPidOfProcess(process);
            String[] cmd = new String[]{"sh", "-c", "kill " + pid};
            Process exec = Runtime.getRuntime().exec(cmd);
            try {
                exec.waitFor();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                exec.destroyForcibly();
            }
        } else {
            try {
                if (process != null && process.isAlive()) {
                    Field f = process.getClass().getDeclaredField("handle");
                    f.setAccessible(true);
                    long handl = f.getLong(process);
                    Kernel32 kernel = Kernel32.INSTANCE;
                    WinNT.HANDLE handle = new WinNT.HANDLE();
                    handle.setPointer(Pointer.createConstant(handl));
                    int ret = kernel.GetProcessId(handle);
                    Long PID = (long) ret;
                    String cmd = "cmd /c taskkill /PID " + PID + " /F /T ";
                    Runtime rt = Runtime.getRuntime();
                    Process killPrcess = rt.exec(cmd);
                    killPrcess.waitFor();
                    killPrcess.destroyForcibly();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isWindows() {
        String osName = getOsName();

        return osName != null && osName.startsWith("Windows");
    }

    public static boolean isLinux() {
        String osName = getOsName();

        return (osName != null && osName.startsWith("Linux")) || (!isWindows() && !isMacOs());
    }

    /**
     * 判断操作系统是否是 MacOS
     *
     * @return true：操作系统是 MacOS
     * false：其它操作系统
     */
    public static boolean isMacOs() {
        String osName = getOsName();

        return osName != null && osName.startsWith("Mac");
    }

    /**
     * 获取操作系统名称
     *
     * @return os.name 属性值
     */
    public static String getOsName() {
        return System.getProperty("os.name");
    }
}