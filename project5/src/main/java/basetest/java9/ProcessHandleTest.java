package basetest.java9;

import static java.lang.System.out;

public class ProcessHandleTest {

    public static void main(String...args) {
        ProcessHandle processHandle = ProcessHandle.current();
        out.println("Process ID: " + processHandle.pid());
        ProcessHandle.Info info = processHandle.info();
        info.command().ifPresent(cmd -> out.println("Command: " + cmd));
        info.arguments().ifPresent(argsArr -> out.println("Arguments: " + String.join("", argsArr)));
        info.startInstant().ifPresent(start -> out.println("Start Time: " + start));
        info.totalCpuDuration().ifPresent(duration -> out.println("CPU Duration: " + duration));
    }

}
