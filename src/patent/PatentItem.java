package patent;
public class PatentItem {
	private String url;
	private String appNo;
	private String date;
	private String patentName;
	private String pubNo;
	private String pubDate;
	private String category;
	private String originNo;
	private String classNo;
	private String issueDate;
	private String priority;
	private String applier;
	private String address;
	private String authors;
	private String intPatent;
	private String intPub;
	private String intDate;
	private String agency;
	private String agent;
	private String summary;

	public PatentItem() {
		url = "";
		appNo = "";
		date = "";
		patentName = "";
		pubNo = "";
		pubDate = "";
		category = "";
		originNo = "";
		classNo = "";
		issueDate = "";
		priority = "";
		applier = "";
		address = "";
		authors = "";
		intPatent = "";
		intPub = "";
		intDate = "";
		agency = "";
		agent = "";
		summary = "";
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAppNo() {
		return appNo;
	}

	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPatentName() {
		return patentName;
	}

	public void setPatentName(String patentName) {
		this.patentName = patentName;
	}

	public String getPubNo() {
		return pubNo;
	}

	public void setPubNo(String pubNo) {
		this.pubNo = pubNo;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category.replace(";", "");
	}

	public String getOriginNo() {
		return originNo;
	}

	public void setOriginNo(String originNo) {
		this.originNo = originNo;
	}

	public String getClassNo() {
		return classNo;
	}

	public void setClassNo(String classNo) {
		this.classNo = classNo.replace("&nbsp;", "");
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		// 当写入到csv文件中的时候
		// priority = priority.replace(",", "，");
		this.priority = priority;
	}

	public String getApplier() {
		return applier;
	}

	public void setApplier(String applier) {
		this.applier = applier;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getIntPatent() {
		return intPatent;
	}

	public void setIntPatent(String intPatent) {
		this.intPatent = intPatent;
	}

	public String getIntPub() {
		return intPub;
	}

	public void setIntPub(String intPub) {
		this.intPub = intPub;
	}

	public String getIntDate() {
		return intDate;
	}

	public void setIntDate(String intDate) {
		this.intDate = intDate;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary.replace("'", "\"");
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(url + ",");
		sb.append(appNo + ",");
		sb.append(date + ",");
		sb.append(patentName + ",");
		sb.append(pubNo + ",");
		sb.append(pubDate + ",");
		sb.append(category + ",");
		sb.append(originNo + ",");
		sb.append(classNo + ",");
		sb.append(issueDate + ",");
		sb.append(priority + ",");
		sb.append(applier + ",");
		sb.append(address + ",");
		sb.append(authors + ",");
		sb.append(intPatent + ",");
		sb.append(intPub + ",");
		sb.append(intDate + ",");
		sb.append(agency + ",");
		sb.append(agent + ",");
		sb.append(summary);
		return sb.toString();
	}
}
