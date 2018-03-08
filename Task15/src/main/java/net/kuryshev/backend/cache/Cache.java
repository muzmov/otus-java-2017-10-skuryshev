package net.kuryshev.backend.cache;

import org.springframework.stereotype.Component;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Sergey.Kuryshev on 26.01.2018
 */

@Component
public class Cache<K, V> {
    private static final int CLEAN_PERIOD = 1000;

    private Map<K, SoftReference<Element<K, V>>> storage = new HashMap<>();
    private int hits = 0;
    private int misses = 0;

    public Cache() {
        Timer timer = new Timer();
        timer.schedule(getCleanEmptyElementsTask(), CLEAN_PERIOD);
    }

    private TimerTask getCleanEmptyElementsTask() {
        return new TimerTask() {
            @Override
            public void run() {
                List<K> unusedKeys = storage.entrySet().stream()
                        .filter(e -> e.getValue().get() == null)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());
                unusedKeys.forEach(k -> storage.remove(k));
            }
        };
    }

    public void put(K key, V value) {
        if (!Objects.equals(getValue(key), value)) {
            Element<K, V> element = new Element<>(key, value);
            storage.put(key, new SoftReference<>(element));
        }
    }

    public V get(K key) {
        V value = getValue(key);
        if (value != null) hits++;
        else misses++;
        return value;
    }

    private V getValue(K key) {
        if (storage.containsKey(key)) {
            Element<K, V> element = storage.get(key).get();
            if (element != null) return element.getValue();
        }
        return null;
    }

    public int getHits() {
        return hits;
    }

    public int getMisses() {
        return misses;
    }

    public void printStatistics() {
        System.out.println(getStatistics());
   }

    public String getStatistics() {
        return String.format("Hits: %d. Misses: %d. Total: %d.", hits, misses, hits + misses);
    }
}
