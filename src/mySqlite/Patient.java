package mySqlite;

public class Patient {
	private String pid;
	private String pname;
	private String pbirthday;
	private String pgender;
	private String pdepartment;
	private String pstate;
	private String datein;
	private String dateout;
	private String pjob;
	private String pmail;
	private String pphone;
	private String pstreet;
	private String paddress;
	private String pcity;
	private String pcode;
	private String ppassword;
	private String pbool;
	private String did;
	public Patient(String pid, String pname, String pbirthday, String pgender,
			String pdepartment, String pstate, String datein, String dateout,
			String pjob, String pmail, String pphone, String pstreet,
			String paddress, String pcity, String pcode, String ppassword,
			String pbool,String did) {
		super();
		this.pid = pid;
		this.pname = pname;
		this.pbirthday = pbirthday;
		this.pgender = pgender;
		this.pdepartment = pdepartment;
		this.pstate = pstate;
		this.datein = datein;
		this.dateout = dateout;
		this.pjob = pjob;
		this.pmail = pmail;
		this.pphone = pphone;
		this.pstreet = pstreet;
		this.paddress = paddress;
		this.pcity = pcity;
		this.pcode = pcode;
		this.ppassword = ppassword;
		this.pbool = pbool;
		this.did = did;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPbirthday() {
		return pbirthday;
	}
	public void setPbirthday(String pbirthday) {
		this.pbirthday = pbirthday;
	}
	public String getPgender() {
		return pgender;
	}
	public void setPgender(String pgender) {
		this.pgender = pgender;
	}
	public String getPdepartment() {
		return pdepartment;
	}
	public void setPdepartment(String pdepartment) {
		this.pdepartment = pdepartment;
	}
	public String getPstate() {
		return pstate;
	}
	public void setPstate(String pstate) {
		this.pstate = pstate;
	}
	public String getDatein() {
		return datein;
	}
	public void setDatein(String datein) {
		this.datein = datein;
	}
	public String getDateout() {
		return dateout;
	}
	public void setDateout(String dateout) {
		this.dateout = dateout;
	}
	public String getPjob() {
		return pjob;
	}
	public void setPjob(String pjob) {
		this.pjob = pjob;
	}
	public String getPmail() {
		return pmail;
	}
	public void setPmail(String pmail) {
		this.pmail = pmail;
	}
	public String getPphone() {
		return pphone;
	}
	public void setPphone(String pphone) {
		this.pphone = pphone;
	}
	public String getPstreet() {
		return pstreet;
	}
	public void setPstreet(String pstreet) {
		this.pstreet = pstreet;
	}
	public String getPaddress() {
		return paddress;
	}
	public void setPaddress(String paddress) {
		this.paddress = paddress;
	}
	public String getPcity() {
		return pcity;
	}
	public void setPcity(String pcity) {
		this.pcity = pcity;
	}
	public String getPcode() {
		return pcode;
	}
	public void setPcode(String pcode) {
		this.pcode = pcode;
	}
	public String getPpassword() {
		return ppassword;
	}
	public void setPpassword(String ppassword) {
		this.ppassword = ppassword;
	}
	public String getPbool() {
		return pbool;
	}
	public void setPbool(String pbool) {
		this.pbool = pbool;
	}
	public String getdid() {
		return did;
	}
	public void setdid(String did) {
		this.did = did;
	}
	
}
