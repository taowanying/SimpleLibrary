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

public class BorrowControllerImpl extends AbstractController {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/library?characterEncoding=utf8&useSSL=true";
	static final String USER = "root";
	static final String PASS = "twy97620";

	@Override
	public FullHttpResponse doCtr(Map<String, Object> params) {
		
		try {
			String borrowresult = null;
			if (params.containsKey("BISBN") && !StringUtils.isNullOrEmpty(params.get("BISBN").toString())) {
				Connection conn = null;
				PreparedStatement pstmt = null;
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				System.out.println("Connecting to database...");
				System.out.println("Creating statement...");
				String sql = "UPDATE bookinfo SET StockNum=StockNum-1 WHERE ISBN=? AND StockNum>0";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, params.get("BISBN").toString());

				int rows = pstmt.executeUpdate();
				System.out.println("Rows impacted : " + rows);
				pstmt.close();
				conn.close();
				if (rows > 0)
					borrowresult = "OK";
				else
					borrowresult = "amounterror";
			} // end if
			else {
				System.out.println("未输入ISBN，借阅失败...");
				borrowresult = "ISBNerror";
			}

			// 渲染模板
			Map root = new HashMap();
			root.put("borrowresult", borrowresult);
			FreeMarker fm = new FreeMarker();
			byte[] content = fm.renderToByte("html/borrow.html", root);
			return ResponseUtil.responseOK(content);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.responseServerError("服务器异常：" + e.getMessage());
		}
	}
}
