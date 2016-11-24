package example;

import org.vertx.java.core.logging.impl.LogDelegate;

/**
 * Created by nihe01 on 14.12.2015.
 */
public class SysOutLoggerDelegate implements LogDelegate {
    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public void fatal(Object o) {
        System.out.println(o);
    }

    @Override
    public void fatal(Object o, Throwable throwable) {
        System.out.println(o);
    }

    @Override
    public void error(Object o) {
        System.out.println(o);
    }

    @Override
    public void error(Object o, Throwable throwable) {
        System.out.println(o);
    }

    @Override
    public void warn(Object o) {
        System.out.println(o);
    }

    @Override
    public void warn(Object o, Throwable throwable) {
        System.out.println(o);
    }

    @Override
    public void info(Object o) {
        System.out.println(o);
    }

    @Override
    public void info(Object o, Throwable throwable) {
        System.out.println(o);
    }

    @Override
    public void debug(Object o) {
        System.out.println(o);
    }

    @Override
    public void debug(Object o, Throwable throwable) {
        System.out.println(o);
    }

    @Override
    public void trace(Object o) {
        System.out.println(o);
    }

    @Override
    public void trace(Object o, Throwable throwable) {

    }
}
