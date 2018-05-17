package onlineLibrarySystem;

import java.util.Map;
import onlineLibrarySystem.ResponseUtil;
import onlineLibrarySystem.template.FreeMarker;
import io.netty.handler.codec.http.FullHttpResponse;

public class UpdateControllerImpl extends AbstractController {

	@Override
	public FullHttpResponse doCtr(Map<String, Object> params) {
		
	
		try{

				
			//渲染模板
			FreeMarker fm = new FreeMarker();

			byte[] content = fm.renderToByte("html/updatebook.html",null);
			
			return ResponseUtil.responseOK(content); 
		}catch(Exception e){
			e.printStackTrace();
			return ResponseUtil.responseServerError("服务器异常："+e.getMessage());
		}
	}
}
