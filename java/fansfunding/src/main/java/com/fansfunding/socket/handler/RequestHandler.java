package com.fansfunding.socket.handler;

import com.fansfunding.socket.entity.SocketRequest;

public abstract class RequestHandler implements Runnable,IRequestHandler{
	private SocketRequest request;
	public RequestHandler(SocketRequest request){
		this.request=request;
	}
	@Override
	public void handle(){
		switch(request.getType()){
		case 1:
			handle1(request);
			break;
		case 2:
			handle2(request);
			break;
		case 3:
			handle3(request);
			break;
		default:
			break;
		}
	}
	private void handle1(SocketRequest request){
	}
	private void handle2(SocketRequest request){
	}
	private void handle3(SocketRequest request){
	}
	@Override
	public void run() {
		before();
		handle();
		after();
	}
}
