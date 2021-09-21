package com.spts.lms.web.helper;

/**
 * Help configure the elements to be displayed in the jsp view
 * @author Indra
 *
 */
public class WebPage {
	
	private String name;
	
	private String title;
	
	private boolean js;
	
	private boolean css;
	
	private boolean header;
	
	private boolean footer;
	
	private boolean leftNav;
	
	/**
	 * Setup the page configuration.
	 * Based on the values selected the corresponding files are included/excluded from the main jsp view.
	 * Navbar and footer are included by default.
	 * Left Nav is disabled
	 * @param name		Name to use for including the js and css files
	 * @param title		Html Title that should be displayed
	 * @param js		Include page specific js file (<name>.js)
	 * @param css		Include page specific css file (<name>.css)
	 */
	public WebPage(String name, String title, boolean js, boolean css) {
		this(name, title, js, css, true, true, false);
	}
	
	/**
	 * Setup the page configuration.
	 * Based on the values selected the corresponding files are included/excluded from the main jsp view.
	 * @param name		Name to use for including the js and css files
	 * @param title		Html Title that should be displayed
	 * @param js		Include page specific js file (<name>.js)
	 * @param css		Include page specific css file (<name>.css)
	 * @param header	Include the navbar in the page
	 * @param footer	Include the footer in the page
	 * @param leftNav 	Include the left Navigation bar
	 */
	public WebPage(String name, String title, boolean js, boolean css, boolean header, boolean footer, boolean leftNav) {
		super();
		this.name = name;
		this.title = title;
		this.js = js;
		this.css = css;
		this.header = header;
		this.footer = footer;
		this.setLeftNav(leftNav);
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isJs() {
		return js;
	}

	public void setJs(boolean js) {
		this.js = js;
	}

	public boolean isCss() {
		return css;
	}

	public void setCss(boolean css) {
		this.css = css;
	}

	public boolean isHeader() {
		return header;
	}

	public void setHeader(boolean header) {
		this.header = header;
	}

	public boolean isFooter() {
		return footer;
	}

	public void setFooter(boolean footer) {
		this.footer = footer;
	}

	public boolean isLeftNav() {
		return leftNav;
	}

	public void setLeftNav(boolean leftNav) {
		this.leftNav = leftNav;
	}
	
	

}
