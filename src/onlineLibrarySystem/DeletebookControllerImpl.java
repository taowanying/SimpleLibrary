package onlineLibrarySystem;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.StringUtils;

import onlineLibrarySystem.ResponseUtil;
import onlineLibrarySystem.template.FreeMarker;
import io.netty.handler.codec.http.FullHttpResponse;

public class DeletebookControllerImpl extends AbstractController {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/library?characterEncoding=utf8&useSSL=true";
	static final String USER = "root";
	static final String PASS = "twy97620";

	@Override
	public FullHttpResponse doCtr(Map<String, Object> params) {

		try {
			String deleteresult = null;
			if (params.containsKey("DISBN") && !StringUtils.isNullOrEmpty(params.get("DISBN").toString())) 
			{
				Connection conn = null;
				PreparedStatement pstmt = null;
				Class.forName("com.mysql.jdbc.Driver");
				conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
				System.out.println("Connecting to database...");
				System.out.println("Creating statement...");
				String sql = "SELECT ISBN FROM bookinfo WHERE ISBN=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, params.get("DISBN").toString());

				ResultSet rs = pstmt.executeQuery();
				if (!rs.wasNull()) {

					pstmt.close();
					PreparedStatement pstmt1 = null;
					System.out.println("Creating statement1...");
					sql = "UPDATE bookinfo SET TotalNum=TotalNum-1 WHERE ISBN=? AND TotalNum>0";

					pstmt1 = conn.prepareStatement(sql);
					System.out.println("0 was ok");
					pstmt1.setString(1, params.get("DISBN").toString());
					System.out.println("1 was ok");
					int rows = pstmt1.executeUpdate();
					rs.close();
					pstmt.close();
					pstmt1.close();
					System.out.println("3 was ok");					
					System.out.println("Rows impacted : " + rows);
					if (rows > 0) {
						deleteresult = "OK";
					} else {
						deleteresult = "amounterror";
					}

				} else {
					
					rs.close();
					pstmt.close();
					deleteresult = "ISBNerror";
					System.out.println("不存在该ISBN！请输入正确的待注销书籍ISBN!");
				}
				conn.close();

			} // end if
			else {
				deleteresult = "error";
				System.out.println("请输入完整的注销信息...");
			}
			Map root = new HashMap();
			root.put("deleteresult", deleteresult);
			FreeMarker fm = new FreeMarker();
			byte[] content = fm.renderToByte("html/deletebook.html", root);
			return ResponseUtil.responseOK(content);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.responseServerError("服务器异常：" + e.getMessage());
		}

	}

}
