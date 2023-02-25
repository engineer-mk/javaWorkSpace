package xmg.xmg.jvm;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

// -XX:+HeapDumpOnOutOfMemoryError -Xmx20m  -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/Users/mk/Downloads/
public class OomTest {

    public static void main(String[] args) {
        final List<Onem> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            list.add(new Onem());
        }
        LockSupport.park();
    }

    static class Onem {
        private final byte[] bytes;

        public Onem() {
            this.bytes = new byte[1024 * 100];
        }
    }
}

