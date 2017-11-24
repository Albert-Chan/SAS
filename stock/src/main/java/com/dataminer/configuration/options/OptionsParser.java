package com.dataminer.configuration.options;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import scala.Tuple2;

/**
 * The option parser. The option definition is a comma separated string in the
 * format of <i>opt, longOpt, hasArg, isRequired, defaultValue, valueParser,
 * description</i>
 * 
 */
public class OptionsParser {
	private Map<String, OptionDef> optionDefMap;

	public OptionsParser(String argsFile) throws IOException, OptionsParserBuildException {
		try (Stream<String> stream = Files.lines(Paths.get(argsFile))) {
			buildOptionMap(stream);
		}
	}

	public OptionsParser(List<String> optDefs) throws OptionsParserBuildException {
		buildOptionMap(optDefs.stream());
	}

	private void buildOptionMap(Stream<String> stream) throws OptionsParserBuildException {
		try {
			optionDefMap = stream.filter(line -> line.length() > 0).map(line -> {
				String[] props = line.split(",");
				String opt = props[0].trim();
				String longOpt = props[1].trim();
				boolean hasArg = OptionDef.HAS_ARG.equals(props[2].trim());
				String description = props[6].trim();
				Option option = new Option(opt, longOpt, hasArg, description);
				boolean required = OptionDef.REQUIRED.equals(props[3].trim());
				option.setRequired(required);
				String defaultValue = props[4].trim();
				String valueParser = props[5].trim();
				OptionDef optDef = new OptionDef(option, valueParser);
				optDef.setDefaultValue(defaultValue);
				return new Tuple2<String, OptionDef>(longOpt, optDef);
			}).collect(Collectors.toMap(t -> t._1, t -> t._2));
		} catch (Exception e) {
			throw new OptionsParserBuildException(e);
		}
	}

	public ParsedOptions parse(String[] args) throws OptionsParseException {
		CommandLineParser parser = new BasicParser();
		CommandLine cmd;
		Options options = new Options();
		for (OptionDef optDef : optionDefMap.values()) {
			options.addOption(optDef.getOption());
		}

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			throw new OptionsParseException("Error occours while parsing the arguments. Caused by: " + e.getMessage());
		}

		ParsedOptions parsed = new ParsedOptions(optionDefMap);
		for (Object o : options.getOptions()) {
			Option opt = (Option) o;
			String longOpt = opt.getLongOpt();
			if (cmd.hasOption(longOpt)) {
				parsed.setRawValue(longOpt, cmd.getOptionValue(longOpt));
			} else {
				if (opt.isRequired() && Objects.isNull(optionDefMap.get(longOpt).getDefaultValue())) {
					throw new OptionsParseException(
							"The required argument: '" + longOpt + "' have not been specified a value.");
				}
			}
		}
		return parsed;
	}

	public class OptionsParserBuildException extends Exception {
		private static final long serialVersionUID = -269910143665096194L;

		public OptionsParserBuildException(Throwable cause) {
			super(cause);
		}
	}

	public class OptionsParseException extends Exception {
		private static final long serialVersionUID = -801298633764626975L;

		public OptionsParseException(String msg) {
			super(msg);
		}
	}
}
