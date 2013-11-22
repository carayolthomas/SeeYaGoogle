package org.tartarus.snowball;

import org.tartarus.snowball.util.StemmerException;

/**
 * @author cmtrim
 *
 */
public class FrenchSnowballStemmerFactory {

	private static final String CLASS_NAME = "org.tartarus.snowball.ext.frenchStemmer";

	private static FrenchSnowballStemmerFactory instance;

	/**
	 * @return {@link argType}
	 */
	public static FrenchSnowballStemmerFactory getInstance() {
		if (null == instance) {
			instance = new FrenchSnowballStemmerFactory();
		}

		return instance;
	}

	private SnowballStemmer stemmer;

	/**
	 * Constructor
	 */
	private FrenchSnowballStemmerFactory() {
	}

	/**
	 * @return {@link SnowballStemmer}
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private SnowballStemmer getStemmer() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (null == stemmer) {
			Class<?> stemClass = Class.forName(CLASS_NAME);
			setStemmer((SnowballStemmer) stemClass.newInstance());
		}

		return stemmer;
	}

	/**
	 * @param token
	 * @return {@link String}
	 * @throws StemmerException
	 */
	public String process(String token) throws StemmerException {
		try {

			getStemmer().setCurrent(token);
			if (!getStemmer().stem()) {
				return token;
			}

			return getStemmer().getCurrent();

		} catch (ClassNotFoundException e) {
			throw new StemmerException(e, String.format("Class not found (class = %s)", CLASS_NAME));
		} catch (InstantiationException e) {
			throw new StemmerException(e, String.format("Unable to instantiate French Stemmer (class = %s)", CLASS_NAME));
		} catch (IllegalAccessException e) {
			throw new StemmerException(e);
		}
	}

	/**
	 * @param stemmer the stemmer to set
	 */
	private void setStemmer(SnowballStemmer stemmer) {
		this.stemmer = stemmer;
	}
}
