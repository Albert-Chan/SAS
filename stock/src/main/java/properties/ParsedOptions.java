package properties;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.cli.Option;

public class ParsedOptions {

	private Map<String, OptionWrapper> optionMap = new HashMap<>();

	public ParsedOptions(Map<String, OptionDef> optionDef) {
		optionDef.forEach((key, value) -> optionMap.put(key, new OptionWrapper(value)));
	}

	/**
	 * Gets the calculated value. If the value from array args is failed to
	 * convert, throws an exception.
	 * 
	 * @param optionName
	 *            the long name of the Option
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String optionName) {
		if (!optionMap.containsKey(optionName)) {
			throw new NonExistentOptionException(optionName + " is not a valid option name.");
		}
		OptionWrapper opt = optionMap.get(optionName);
		String parseFunc = opt.getParseFunc();
		String rawValue = opt.getRawValue();
		String defaultValue = opt.getDefaultValue();

		if (Objects.nonNull(rawValue)) {
			// The user has specified the value.
			// If the value is invalid, throws a runtime exception.
			T result = (T) convertValue(parseFunc, rawValue).orElse(null);
			if (Objects.isNull(result)) {
				throw new InvalidOptionValueException(
						"The value of " + optionName + " in args array cannot be converted.");
			}
			return result;
		} else {
			// use the default value passed from option definition.
			return (T) convertValue(parseFunc, defaultValue).orElse(null);
		}
	}

	void setRawValue(String longOpt, String rawValue) {
		optionMap.get(longOpt).setRawValue(rawValue);
	}

	@SuppressWarnings("unchecked")
	private <T> Optional<T> convertValue(String parseFunc, String rawValue) {
		try {
			Method m = ParserUtil.class.getDeclaredMethod(parseFunc, String.class);
			return (Optional<T>) m.invoke(null, rawValue);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	public class OptionWrapper {

		private OptionDef optDef;
		private String rawValue;

		public OptionWrapper(OptionDef optDef) {
			this.optDef = optDef;
		}

		public Option getOption() {
			return optDef.getOption();
		}

		public String getParseFunc() {
			return optDef.getParseFunc();
		}

		public String getDefaultValue() {
			return optDef.getDefaultValue();
		}

		public String getRawValue() {
			return rawValue;
		}

		public void setRawValue(String rawValue) {
			this.rawValue = rawValue;
		}

	}

	public class InvalidOptionValueException extends RuntimeException {
		private static final long serialVersionUID = -3372020901775594046L;

		public InvalidOptionValueException(String msg) {
			super(msg);
		}
	}

	public class NonExistentOptionException extends RuntimeException {
		private static final long serialVersionUID = -3372020901775594046L;

		public NonExistentOptionException(String msg) {
			super(msg);
		}
	}
}
