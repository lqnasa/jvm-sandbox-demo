package com.lee;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.kohsuke.MetaInfServices;
import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.http.Http;
import com.alibaba.jvm.sandbox.api.http.printer.ConcurrentLinkedQueuePrinter;
import com.alibaba.jvm.sandbox.api.http.printer.Printer;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatcher;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@MetaInfServices(Module.class)
@Information(id = "debug-watch", version = "0.0.1", author = "liqiao")
public class DebugWatchRCDModule implements Module {

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    private static Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    @Http("/watch")
    public void watch(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final String cnPattern = req.getParameter("class");
        final String mnPattern = req.getParameter("method");
        final List<Trigger> triggers = Arrays.asList(Trigger.BEFORE, Trigger.RETURN, Trigger.THROWS);
        final Printer printer = new ConcurrentLinkedQueuePrinter(resp.getWriter());
        debugWatch(cnPattern, mnPattern, triggers, printer);
    }

    private void debugWatch(final String cnPattern, final String mnPattern, final List<Trigger> triggers,
            final Printer printer) {
        EventWatcher watcher = null;
        try {
            watcher = new EventWatchBuilder(moduleEventWatcher).onClass(cnPattern).includeSubClasses()
                    .onBehavior(mnPattern).onWatching().withProgress(new ProgressPrinter(printer))
                    .onWatch(new AdviceListener() {

                        @Override
                        public void before(final Advice advice) {
                            printlnByExpress(binding(advice));
                        }

                        @Override
                        public void afterReturning(final Advice advice) {
                            String returnObj = gson.toJson(advice.getReturnObj());
                            printlnByExpress(binding(advice).bind("return", returnObj));
                        }

                        @Override
                        public void afterThrowing(final Advice advice) {
                            printlnByExpress(binding(advice).bind("throws", advice.getThrowable()));
                        }

                        private Bind binding(Advice advice) {
                            String parameterArray = gson.toJson(advice.getParameterArray());
                            return new Bind().bind("class", advice.getBehavior().getDeclaringClass())
                                    .bind("method", advice.getBehavior()).bind("params", parameterArray)
                                    .bind("target", advice.getTarget());
                        }

                        private void printlnByExpress(final Bind bind) {
                            String watchObjectStr = bind.entrySet().stream()
                                    .map(obj -> obj.getKey() + ":" + obj.getValue()).collect(Collectors.joining(" "));
                            printer.println(watchObjectStr);
                        }

                    });

            printer.println(String.format("watching on [%s#%s], at %s.\nPress CTRL_C abort it!", cnPattern, mnPattern,
                    triggers));
            printer.waitingForBroken();
        } finally {
            watcher.onUnWatched();
        }
    }

    /**
     * 观察触点
     */
    enum Trigger {
        BEFORE, RETURN, THROWS
    }

    class Bind extends HashMap<String, Object> {
        /**
         * 
         */
        private static final long serialVersionUID = 2455316406956962948L;

        Bind bind(final String name, final Object value) {
            put(name, value);
            return this;
        }
    }

}
