package resultset;

public class Result {
	private String ISBN;
	private String bookname;
	private String bookauthor;
	private String bookcategoryID;
	
	public String getISBN() {
		return ISBN;
	}
	public void setISBN(String ISBN) {
		this.ISBN = ISBN;
	}
	public String getbookname() {
		return bookname;
	}
	public void setbookname(String bookname) {
		this.bookname = bookname;
	}
	public String getbookauthor() {
		return bookauthor;
	}
	public void setbookauthor(String bookauthor) {
		this.bookauthor = bookauthor;
	}
	public String getbookcategoryID() {
		return bookcategoryID;
	}
	public void setbookcategoryID(String bookcategoryID) {
		this.bookcategoryID = bookcategoryID;
	}
}
