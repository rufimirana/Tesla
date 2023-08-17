package chrono;

import java.util.Timer;
import java.util.*;

public class Chrono extends Observable {
    long seconds = 0;
    long totalSecondes = 0;
    boolean launched = false;
    Thread currentThread;
    boolean pause = false;

    public void start() {
        launched = true;
        currentThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (launched) {
                    if (!pause) {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        seconds++;
                        totalSecondes++;
                        setChanged();
                        notifyObservers(seconds);
                    }
                    System.out.print("");
                }

            }
        });
        currentThread.start();
    }

    public long getTotalSecondes() {
        return totalSecondes;
    }

    public void toggle() {
        pause = !pause;
    }

    public void restart() {
        reset();
        start();
    }

    public void reset() {
        stop();
        seconds = 0;
    }

    public void stop() {
        launched = false;
        if (currentThread != null)
            currentThread.stop();
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
        stop();
    }

    public long getSeconds() {
        return seconds;
    }

    public boolean isLaunched() {
        return launched;
    }
}
