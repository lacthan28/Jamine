package jamine.event;

import jamine.Server;
import jamine.level.Level;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.StrictMath.round;

/**
 * Created by lacthan28 on 6/6/2017 - 7:40 PM.
 */
public class TimingsHandler {
    public static final int MAX = 50;
    /**
     * @var TimingsHandler[]
     */
    static TimingsHandler[] HANDLERS = new TimingsHandler[MAX];

    private String name;
    /**
     * @var TimingsHandler
     */
    private TimingsHandler parent = null;

    private int count = 0;
    private int curCount = 0;
    private int start = 0;
    private int timingDepth = 0;
    private int totalTime = 0;
    private int curTickTotal = 0;
    private int violations = 0;

    /**
     * @param name   string
     * @param parent TimingsHandler
     */
    public TimingsHandler(String name, TimingsHandler parent) {
        this.name = name;
        if (parent != null) {
            this.parent = parent;
        }
        HANDLERS[System.identityHashCode(this)] = this;
    }

    public static void printTimings(String fp) throws IOException {
        File f = new File(fp);
        FileWriter fwrite = new FileWriter(f);
        fwrite.write("Minecraft \n");

        for (TimingsHandler timings : HANDLERS) {
            int time = timings.totalTime;
            int count = timings.count;
            if (count == 0) {
                continue;
            }

            double avg = time / count;

            fwrite.write("    " + timings.name + " Time: " + round(time * 1000000000) + " Count: " + count + " Avg: " + round(avg * 1000000000) + " Violations: " + timings.violations + "\n");
        }

        fwrite.write("# Version " + Server.getInstance().getVersion() + "\n");
        fwrite.write("# " + Server.getInstance().getName() + " " + Server.getInstance().getPocketMineVersion() + "\n");

        int entities = 0;
        int livingEntities = 0;
        for (Level level : Server.getInstance().getLevels()) {
            entities += (level.getEntities()).length();
            for (Entity e : level.getEntities()) {
                if (e instanceof Living) {
                    ++livingEntities;
                }
            }
        }

        fwrite.write("# Entities " + entities + "\n");
        fwrite.write("# LivingEntities " + livingEntities + "\n");
        fwrite.flush();
        fwrite.close();

    }

    public static void reload() {
        if (Server.getInstance().getPluginManager().useTimings()) {
            for(TimingsHandler timings:TimingsHandler.HANDLERS) {
                timings.reset();
            }
            TimingsCommand.timingStart = microtime(true);
        }
    }

    public static void tick(measure =true) {
        if (PluginManager.useTimings) {
            if (measure) {
                for(self.HANDLERS as timings) {
                    if (timings.curTickTotal > 0.05) {
                        timings.violations += round(timings.curTickTotal / 0.05);
                    }
                    timings.curTickTotal = 0;
                    timings.curCount = 0;
                    timings.timingDepth = 0;
                }
            } else {
                for(self.HANDLERS as timings) {
                    timings.totalTime -= timings.curTickTotal;
                    timings.count -= timings.curCount;

                    timings.curTickTotal = 0;
                    timings.curCount = 0;
                    timings.timingDepth = 0;
                }
            }
        }
    }

    public void startTiming() {
        if (PluginManager.useTimings and++ this.timingDepth == = 1){
            this.start = microtime(true);
            if (this.parent != = null and++ this.parent.timingDepth == = 1){
                this.parent.start = this.start;
            }
        }
    }

    public void stopTiming() {
        if (PluginManager.useTimings) {
            if (--this.timingDepth != = 0 or this.start == = 0){
                return;
            }

            diff = microtime(true) - this.start;
            this.totalTime += diff;
            this.curTickTotal += diff;
            ++this.curCount;
            ++this.count;
            this.start = 0;
            if (this.parent != = null) {
                this.parent.stopTiming();
            }
        }
    }

    public void reset() {
        this.count = 0;
        this.curCount = 0;
        this.violations = 0;
        this.curTickTotal = 0;
        this.totalTime = 0;
        this.start = 0;
        this.timingDepth = 0;
    }

    public void remove() {
        unset(self.HANDLERS[spl_object_hash(this)]);
    }
}
