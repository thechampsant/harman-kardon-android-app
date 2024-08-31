package app.core.model;

public class PageModel {

	public String PageSize;
	public String PageIndex;
	public String TotalPageCount;
	public String TotalRecords;
	public String LastUpdatedOn;
	public String UserName;

	public PageModel setPageSize(int PazeSize) {
		this.PageSize = String.valueOf(PazeSize);
		return this;
	}

	public PageModel setPageIndex(int PageIndex) {
		this.PageIndex = String.valueOf(PageIndex);
		return this;
	}

	public PageModel setUser(String UserName) {
		this.UserName = UserName;
		return this;
	}

	public PageModel autoIncrement() {
		this.PageIndex = String.valueOf(Integer.parseInt(this.PageIndex) + 1);
		return this;
	}

	public PageModel() {
		this.PageIndex = "0";
		this.PageSize = "500";
		this.TotalPageCount = "1000";
	}

	public int getTotalPages() {
		return Integer.parseInt(this.TotalPageCount);
	}
	public int getPageIndex()
	{
		return Integer.parseInt(this.PageIndex);
	}
	public int getPageNumber()
	{
		return Integer.parseInt(this.PageIndex)+1;
	}
}
