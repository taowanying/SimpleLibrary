package onlineLibrarySystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.mysql.jdbc.StringUtils;

import onlineLibrarySystem.ResponseUtil;
import onlineLibrarySystem.template.FreeMarker;
import io.netty.handler.codec.http.FullHttpResponse;

public class AddbookControllerImpl extends AbstractController {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/library?characterEncoding=utf8&useSSL=true";
	static final String USER = "root";
	static final String PASS = "twy97620";

	@Override
	public FullHttpResponse doCtr(Map<String, Object> params) {

		try {
			String addresult = null;
			if (params.containsKey("AISBN") && !StringUtils.isNullOrEmpty(params.get("AISBN").toString())
					&& params.containsKey("Abookname") && !StringUtils.isNullOrEmpty(params.get("Abookname").toString())
					&& params.containsKey("Abookauthor")
					&& !StringUtils.isNullOrEmpty(params.get("Abookauthor").toString())
					&& params.containsKey("AbookcategoryID")
					&& !StringUtils.isNullOrEmpty(params.get("AbookcategoryID").toString())
					&& params.containsKey("Aamount") && !StringUtils.isNullOrEmpty(params.get("Aamount").toString())) {
				Connection conn = null;
				PreparedStatement pstmt = null;
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				System.out.println("Connecting to database...");
				System.out.println("Creating statement...");
				String sql = "SELECT ISBN FROM bookinfo WHERE ISBN=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, params.get("AISBN").toString());

				ResultSet rs = pstmt.executeQuery();
				if (rs.wasNull()) {
					pstmt.close();
					System.out.println("该ISBN已存在，新增书籍失败 ！ ");
					addresult = "ISBNerror";
				} else {
					pstmt.close();
					sql = "SELECT BookCategoryID FROM bookcategory WHERE BookCategoryID=?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, params.get("AbookcategoryID").toString());

					ResultSet rs1 = pstmt.executeQuery();
					if (rs1.wasNull()) {
						PreparedStatement pstmt1 = null;
						sql = "INSERT INTO bookcategory VALUES(?,'null',0,0)";
						System.out.println("Creating add statement1...");
						pstmt1 = conn.prepareStatement(sql);
						pstmt1.setString(1, params.get("AbookcategoryID").toString());
						int rows = pstmt1.executeUpdate();
						pstmt1.close();
						System.out.println("Rows impacted : " + rows);
					}
					rs1.close();
					PreparedStatement pstmt2 = null;
					sql = "INSERT INTO bookinfo VALUES(?,?,'001',?,'null',0,?,?,?)";
					System.out.println("Creating add statement2...");
					pstmt2 = conn.prepareStatement(sql);
					pstmt2.setString(1, params.get("AISBN").toString());
					pstmt2.setString(2, params.get("Abookname").toString());
					pstmt2.setString(3, params.get("Abookauthor").toString());
					pstmt2.setString(4, params.get("Aamount").toString());
					pstmt2.setString(5, params.get("Aamount").toString());
					pstmt2.setString(6, params.get("AbookcategoryID").toString());
					int rows = pstmt2.executeUpdate();
					System.out.println("Rows impacted : " + rows);

					pstmt2.close();
					addresult = "OK";
				}
				// rs.close();
				conn.close();

			} // end if
			else {
				System.out.println("新增书籍失败，请输入完整的书籍信息！");
				addresult = "error";
			}

			// 渲染模板
			Map root = new HashMap();
			root.put("addresult", addresult);
			FreeMarker fm = new FreeMarker();
			byte[] content = fm.renderToByte("html/addbook.html", root);
			return ResponseUtil.responseOK(content);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.responseServerError("服务器异常：" + e.getMessage());
		}
	}
}
