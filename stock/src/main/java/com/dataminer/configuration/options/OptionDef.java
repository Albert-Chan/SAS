package com.dataminer.configuration.options;

import org.apache.commons.cli.Option;

public class OptionDef {
	static final String HAS_ARG = "hasArg";
	static final String REQUIRED = "required";
	private Option opt;
	private String parseFunc;
	private String defaultValue;

	public OptionDef(Option opt, String parseFunc) {
		this.opt = opt;
		this.parseFunc = parseFunc;
	}

	public Option getOption() {
		return opt;
	}

	public String getParseFunc() {
		return parseFunc;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}