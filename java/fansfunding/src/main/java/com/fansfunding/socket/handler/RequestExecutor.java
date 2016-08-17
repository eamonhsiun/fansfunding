package com.fansfunding.socket.handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestExecutor {
	private static ExecutorService exec=Executors.newCachedThreadPool();
	private RequestExecutor(){
		throw new IllegalArgumentException();
	}
	public static void execute(RequestHandler handler){
		exec.execute(handler);
	}
}
