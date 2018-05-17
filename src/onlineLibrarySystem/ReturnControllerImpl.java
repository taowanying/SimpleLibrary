package onlineLibrarySystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

import com.mysql.jdbc.StringUtils;

import onlineLibrarySystem.ResponseUtil;
import onlineLibrarySystem.template.FreeMarker;
import io.netty.handler.codec.http.FullHttpResponse;

public class ReturnControllerImpl extends AbstractController {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/library?characterEncoding=utf8&useSSL=true";
	static final String USER = "root";
	static final String PASS = "twy97620";

	@Override
	public FullHttpResponse doCtr(Map<String, Object> params) {
		
		try {
			String returnresult = null;
			if (params.containsKey("RISBN") && !StringUtils.isNullOrEmpty(params.get("RISBN").toString())) {
				Connection conn = null;
				PreparedStatement pstmt = null;
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				System.out.println("Connecting to database...");
				System.out.println("Creating statement...");
				String sql = "UPDATE bookinfo SET StockNum=StockNum+1 WHERE ISBN=? AND StockNum<TotalNum";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, params.get("RISBN").toString());

				int rows = pstmt.executeUpdate();
				System.out.println("Rows impacted : " + rows);
				pstmt.close();
				conn.close();
				if (rows > 0)
					returnresult = "OK";
				else
					returnresult = "amounterror";
			} // end if
			else {
				System.out.println("未输入ISBN，归还失败...");
				returnresult = "ISBNerror";
			}

			// 渲染模板
			Map root = new HashMap();
			root.put("returnresult", returnresult);
			FreeMarker fm = new FreeMarker();
			byte[] content = fm.renderToByte("html/return.html", root);
			return ResponseUtil.responseOK(content);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.responseServerError("服务器异常：" + e.getMessage());
		}
	}
}
