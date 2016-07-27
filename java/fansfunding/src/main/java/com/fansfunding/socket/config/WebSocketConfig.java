package com.fansfunding.socket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.fansfunding.socket.handler.TestHandler;
import com.fansfunding.socket.interceptor.WebSocketHandshakeInterceptor;


/**
 * websocket配置
 * 因为跨域请求的接口，需要setAllowedOrigins("*")
 * 两种方式，原生websocket和SockJS
 * @author w_angler
 *
 */
@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(userHandler(),"/websocket/test").setAllowedOrigins("*")
        		.addInterceptors(new WebSocketHandshakeInterceptor());
        registry.addHandler(userHandler(), "/sockjs/test").setAllowedOrigins("*")
        		.addInterceptors(new WebSocketHandshakeInterceptor()).withSockJS();
    }

    @Bean
    public WebSocketHandler userHandler(){
        return new TestHandler();
    }

}