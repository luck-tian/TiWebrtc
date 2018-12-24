package com.hhtc.dialer.call.strategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PeerConnectionExecutor {

    private PeerConnectionStrategy strategy;

    private Map<String, PeerConnectionStrategy> strategys = new ConcurrentHashMap<>();


    public PeerConnectionExecutor addStrategy(PeerConnectionStrategy strategy) {
        strategys.put(strategy.name(), strategy);
        return this;
    }

    public PeerConnectionStrategy getStrategy(String strategy) {
        return strategys.get(strategy);
    }

    public void executorStrategy() {
        strategy.execute();
    }
}
