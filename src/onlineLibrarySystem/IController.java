package onlineLibrarySystem;

import java.io.File;
import java.util.Map;

import io.netty.handler.codec.http.FullHttpResponse;

public interface IController {

	//old
	public FullHttpResponse doCtr(Map<String, Object> requestParams);
	
	public FullHttpResponse doCtr(Map<String, Object> params,String method);
	
	//public FullHttpResponse doCtr(Map<String,File> files,Map<String, String> params,String method);

	
}
