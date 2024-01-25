package cn.scheduled;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 任务线程池
 *
 * @author nackily
 * @since 1.0.0
 */
public class TaskScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {

    private static final TaskScheduledThreadPoolExecutor INSTANCE = new TaskScheduledThreadPoolExecutor();
    public static TaskScheduledThreadPoolExecutor getInstance() {
        return INSTANCE;
    }

    protected TaskScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
    }

    protected TaskScheduledThreadPoolExecutor() {
        this(2, new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("Task-thread-" + threadNumber.getAndIncrement());
                return thread;
            }
        });
        // 任务取消时从队列中移除
        setRemoveOnCancelPolicy(true);
    }

}
