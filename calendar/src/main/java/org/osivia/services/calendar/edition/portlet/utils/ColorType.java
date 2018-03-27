package org.osivia.services.calendar.edition.portlet.utils;

public enum ColorType {

    RED("red","#D50000"),
    MANDARIN("mandarin","#F4511E"),
    LIGHTGREEN("lightGreen","#33B679"),
    LIGHTBLUE("lightBlue", "#039BE5"),
    LAVAND("lavand", "#7986CB"),
    GREY("grey", "#616161"),
    PINK("pink","#E67C73"),
    YELLOW("yellow", "#F6BF26"),
    GREEN("green", "#0B8043"),
    BLUE("blue", "#3F51B5"),
    PURPLE("purple", "#8E24AA"),
    ORANGE("orange", "#EF6C00");
	
	private final String colorName;
	
	private final String colorCode;


	private ColorType(String name, String code) {
		this.colorName = name;
		this.colorCode = code;
	}

	public String getColorName() {
		return colorName;
	}


	public String getColorCode() {
		return colorCode;
	}


}
