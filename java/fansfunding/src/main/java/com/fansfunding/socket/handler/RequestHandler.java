package com.fansfunding.socket.handler;

import com.fansfunding.socket.entity.SocketRequest;

public abstract class RequestHandler implements Runnable{
	private SocketRequest request;
	public RequestHandler(SocketRequest request){
		this.request=request;
	}
	public abstract void handle(SocketRequest request);
	@Override
	public void run() {
		handle(this.request);
	}
}
