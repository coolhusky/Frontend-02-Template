package com.github.coolhusky.gateway.router;

import java.util.List;
import java.util.Random;

public class RandomHttpEndpointRouter implements HttpEndpointRouter {
    @Override
    public String route(List<String> svcUrls) {
        int size = svcUrls.size();
        Random rd = new Random(System.currentTimeMillis());
        return svcUrls.get(rd.nextInt(size));
    }
}
