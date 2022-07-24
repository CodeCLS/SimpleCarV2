package cls.simplecar.tools;


public class UpdaterConnection {
    private final Runnable task;

    public UpdaterConnection(Runnable task) {
        this.task = task;
    }

    public void update() {
        task.run();
    }
}
