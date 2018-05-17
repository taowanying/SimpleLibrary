package onlineLibrarySystem;

import java.util.Map;

import onlineLibrarySystem.template.FreeMarker;
import io.netty.handler.codec.http.FullHttpResponse;

public class IndexControllerImpl  extends AbstractController{

	@Override
	public FullHttpResponse doCtr(Map<String, Object> params) {
		//渲染模板
		try {
			FreeMarker fm = new FreeMarker();
			byte[] content = fm.renderToByte("html/index.html", null);
			return ResponseUtil.responseOK(content);  
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.responseServerError("error");
		}  
	}



}
