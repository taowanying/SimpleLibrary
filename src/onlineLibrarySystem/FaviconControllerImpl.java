package onlineLibrarySystem;

import java.util.Map;


import io.netty.handler.codec.http.FullHttpResponse;

public class FaviconControllerImpl  extends AbstractController{

	@Override
	public FullHttpResponse doCtr(Map<String, Object> params) {
		
		return ResponseUtil.responseOK("Ok");
	}


}
