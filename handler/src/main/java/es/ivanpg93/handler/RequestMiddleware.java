package es.ivanpg93.handler;

import okhttp3.Request;

/**
 * Created by Guillem on 2019-07-17.
 * app2U SL
 */
public interface RequestMiddleware {
    Request processRequest(Request request);
}
