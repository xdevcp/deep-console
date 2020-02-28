package cc.devcp.project.config.server.manager;

/**
 * task processor
 *
 * @author deep
 */
public interface TaskProcessor {
    /**
     * process task
     *
     * @param taskType task type
     * @param task     task
     * @return process task result
     */
    boolean process(String taskType, AbstractTask task);
}
