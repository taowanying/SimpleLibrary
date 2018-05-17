package onlineLibrarySystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.StringUtils;

import io.netty.handler.codec.http.FullHttpResponse;
import onlineLibrarySystem.template.FreeMarker;
import resultset.Result;

public class SearchControllerImpl extends AbstractController {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/library?characterEncoding=utf8&useSSL=true";
	static final String USER = "root";
	static final String PASS = "twy97620";

	@Override
	public FullHttpResponse doCtr(Map<String, Object> params) {

		try {
			Connection conn = null;
			PreparedStatement pstmt;
			pstmt = null;
			// String sql = "SELECT ISBN,BookName,Author,BookCategoryID FROM
			// bookinfo where 1=1 AND ? ? ? ?";
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT ISBN,BookName,Author,BookCategoryID FROM bookinfo where 1=1 ");
			// PreparedStatement pstmt = conn.prepareStatement(sql);
			if (params.containsKey("SISBN") && !StringUtils.isNullOrEmpty(params.get("SISBN").toString())) {
				sb.append(" And ISBN").append("=").append("'").append(params.get("SISBN")).append("'");
				// pstmt.setString(1, "ISBN="+(String) params.get("SISBN"));
			}

			if (params.containsKey("Sbookname") && !StringUtils.isNullOrEmpty(params.get("Sbookname").toString())) {
				sb.append(" And BookName").append(" like ").append("'%").append(params.get("Sbookname")).append("%")
						.append("'");
				// pstmt.setString(2, "BookName="+(String)
				// params.get("Sbookname"));
			}

			if (params.containsKey("Sbookauthor") && !StringUtils.isNullOrEmpty(params.get("Sbookauthor").toString())) {
				sb.append(" And Author").append("= ").append("'").append(params.get("Sbookauthor")).append("'");
				// pstmt.setString(3, (String) params.get("Sbookauthor"));
			}

			if (params.containsKey("SbookcategoryID")
					&& !StringUtils.isNullOrEmpty(params.get("SbookcategoryID").toString())) {
				sb.append(" And BookCategoryID").append("=").append(params.get("SbookcategoryID"));
				// pstmt.setString(4, (String) params.get("SbookcategoryID"));
			}

			System.out.println(String.format("execute sql is %s", sb.toString()));
			List list = new ArrayList();
			try {

				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				System.out.println("Connecting to database...");
				System.out.println("Creating statement...");
				pstmt = (PreparedStatement) conn.prepareStatement(sb.toString());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					Result result = new Result();
					result.setISBN(rs.getString("ISBN"));
					result.setbookname(rs.getString("Bookname"));
					result.setbookauthor(rs.getString("Author"));
					result.setbookcategoryID(rs.getString("BookCategoryID"));
					list.add(result);
					System.out.println("搜索成功！");
				}
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} finally {
				// finally block used to close resources
				try {
					if (pstmt != null)
						pstmt.close();
				} catch (SQLException se2) {
				} // nothing we can do
				try {
					if (conn != null)
						conn.close();
				} catch (SQLException se) {
					se.printStackTrace();
				} // end finally try
			}
			Map root = new HashMap();
			root.put("list", list);
			FreeMarker fm = new FreeMarker();
			byte[] content = fm.renderToByte("html/result.html", root);
			return ResponseUtil.responseOK(content);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.responseServerError("服务器异常：" + e.getMessage());
		}

	}

}
