import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

interface Logger {
    enum Level {
        DEBUG(0),
        INFO(1),
        WARNING(2),
        ERROR(3);

        public final int mValue;
        Level(int value) {
            mValue = value;
        }
    }

    // PRE: level != null && message != null
    void logString(Level level, String message);
    // POST: message is output to some channel if level >= getLevel

    Level getLevel();
    // POST: The minimum level the logger will output. Constant
}

class StdOutLogger implements Logger {
    // CLASS INVARIANT: mLevel != null
    private final Level mLevel;

    // PRE: level != null
    public StdOutLogger(Level level) {
        mLevel = level;
    }

    // PRE: level != null && message != null
    public final void logString(Level level, String message) {
        if (level.mValue >= mLevel.mValue) {
            System.out.println(message);
        }
    }
    // POST: message is written to the stdout channel if level >= getLevel

    public Level getLevel() {
        return mLevel;
    }
    // POST: The minimum level the logger will output. Constant
}

interface Reader {
    String readString();
    // POST: A message read from some channel if available, else null.
}

class StdInReader implements Reader {
    // CLASS INVARIANT: mReader != null
    private final BufferedReader mReader = new BufferedReader(new InputStreamReader(System.in));

    public final String readString() {
        try {
            return mReader.readLine();
        } catch (IOException ioExc) {
            return null;
        }
    }
}

final class ChannelBridge {
    // CLASS INVARIANT: mReader != null
    private final Reader mReader;
    // CLASS INVARIANT: mLogger != null
    private final Logger mLogger;
    // CLASS INVARIANT: mLevel != null
    private final Logger.Level mLevel;

    // PRE: reader != null, logger != null, level != null
    public ChannelBridge(Reader reader, Logger logger, Logger.Level level) {
        mReader = reader;
        mLogger = logger;
        mLevel = level;
    }

    public void tick() {
        String message = mReader.readString();
        if (message != null) {
            mLogger.logString(mLevel, message);
        }
    }
    // POST: If there is a message from the reader it gets logged in the Logger at level mLevel;
}

public class Main2 {
    public static void main(String[] args) {
        final Logger logger = new StdOutLogger(INFO);
        logger.logString(Logger.Level.DEBUG, "DEBUGGING");
        final Reader reader = new StdInReader();
        final ChannelBridge bridge = new ChannelBridge(reader, logger, Logger.Level.WARNING);
        bridge.tick();
        bridge.tick();
    }
}
