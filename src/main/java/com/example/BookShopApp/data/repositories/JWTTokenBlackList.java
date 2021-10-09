package com.example.BookShopApp.data.repositories;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.stereotype.Service;

@Service
public class JWTTokenBlackList {

    public static final String TOKENS = "tokens";
    private final HazelcastInstance hazelcastInstance
            = Hazelcast.newHazelcastInstance(createConfig());

    public void put(String accessTokenId, String jwtToken) {
        IMap<String, String> map = hazelcastInstance.getMap(TOKENS);
        map.put(accessTokenId, jwtToken);
    }

    public String get(String accessTokenId) {
        IMap<String, String> map = hazelcastInstance.getMap(TOKENS);
        return map.get(accessTokenId);
    }

    public Config createConfig() {
        Config config = new Config();
        config.addMapConfig(mapConfig());
        return config;
    }

    private MapConfig mapConfig() {
        MapConfig mapConfig = new MapConfig(TOKENS);
        mapConfig.setTimeToLiveSeconds(3600);
        return mapConfig;
    }
}
