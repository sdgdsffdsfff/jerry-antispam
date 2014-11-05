package com.hehua.framework.antispam.segment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SimpleDartsSegment {

	private static final long serialVersionUID = -4815159987414538914L;
	private Set<Character> foreign; // 外来词，无意义
	private Set<Character> numbers; // 数量词
	private Darts darts;

	public SimpleDartsSegment(Darts dat) {
		this.darts = dat;
		try {
			this.load();
		} catch (IOException e) {
		}
	}

	public void load() throws IOException {
		foreign = new TreeSet<Character>();
		numbers = new TreeSet<Character>();
		loadset(numbers, "resources/numbers_u8.txt");
		loadset(foreign, "resources/foreign_u8.txt");
	}

	/**
	 * 取得字符串的的所有token
	 * 
	 * @param source
	 * @return
	 */
	public List<WordTerm> getToken(char[] source) {
		int pos = 0;
		int len = source.length;
		List<WordTerm> list = new LinkedList<WordTerm>();
		while (pos < source.length) {
			WordTerm w = darts.prefixSearchMax(source, pos, len);
			if (w == null) {
				w = new WordTerm();
				w.begin = pos;
				int c;
//				c = englishToken(source, pos);
//				if (c > 0) {
//					w.length = c;
//				} else {
					c = numberToken(source, pos);
					if (c > 0) {
						w.length = c;
					} else {
						c = foreignToken(source, pos);
						if (c > 0) {
							w.length = c;
						} else {
							w.length = 1;
						}
					}
//				}
			}
			list.add(w);
			len = source.length - (w.begin + w.length);
			pos = w.begin + w.length;
		}
		return list;
	}

	/**
	 * 切分英文单词
	 * 
	 * @param source
	 * @param pos
	 * @return
	 */
	private int englishToken(char[] source, int pos) {
		int count = 0;
		int len = source.length;
		while (pos + count < len && isEnglishChar(source[pos + count])) {
			++count;
		}
		return count;
	}

	/**
	 * 切分数量词
	 * 
	 * @param source
	 * @param pos
	 * @return
	 */
	private int numberToken(char[] source, int pos) {
		int count = 0;
		int len = source.length;
		while (pos + count < len && numbers.contains(source[pos + count])) {
			++count;
		}
		return count;
	}

	/**
	 * 切分外来语或者一些名字
	 * 
	 * @param source
	 * @param pos
	 * @return
	 */
	private int foreignToken(char[] source, int pos) {
		int count = 0;
		int len = source.length;
		while (pos + count < len && foreign.contains(source[pos + count])) {
			++count;
		}
		return count;
	}

	private boolean isEnglishChar(char c) {
		return ((c >= 97 && c <= 122) || (c >= 65 && c <= 90));
	}

	private void loadset(Set<Character> targetset, String sourcefile) {
		String dataline;
		BufferedReader in = null;
		try {
			int count = 0;
			InputStream setdata = getSegmentResource(sourcefile);
			in = new BufferedReader(new InputStreamReader(setdata, "UTF-8"));
			while ((dataline = in.readLine()) != null) {
				if ((dataline.indexOf("#") > -1) || (dataline.length() == 0)) {
					continue;
				}
				targetset.add(dataline.charAt(0));
				count++;
			}

		} catch (IOException e) {
		}
		try {
			if (in != null) {
				in.close();
			}
		} catch (IOException e) {
		}
	}

	private InputStream getSegmentResource(String sourcefile) {
		// String pckName = Darts.class.getPackage().getName();
		// sourcefile = "/" + pckName.replace('.', '/') + "/" + sourcefile;
		return Darts.class.getResourceAsStream(sourcefile);
	}

	public Darts getDarts() {
		return darts;
	}

	public void setDarts(Darts darts) {
		this.darts = darts;
	}
}

