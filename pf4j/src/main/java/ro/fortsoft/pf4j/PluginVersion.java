/*
 * Copyright 2012 Decebal Suiu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with
 * the License. You may obtain a copy of the License in the LICENSE file, or at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package ro.fortsoft.pf4j;

import ro.fortsoft.pf4j.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the version of a Plugin and allows versions to be compared.
 * Version following semantic defined by <a href="http://semver.org/">Semantic Versioning</a> document.
 * Version identifiers have four components.
 *
 *  1. Major version. A non-negative integer.
 *  2. Minor version. A non-negative integer.
 *  3. Patch version. A non-negative integer.
 *  4. Qualifier. A text string.
 *
 * This class is immutable.
 *
 * @author Decebal Suiu
 */
public class PluginVersion implements Comparable<PluginVersion> {

    private static final String FORMAT = "(\\d+)\\.(\\d+)(?:\\.)?(\\d*)(\\.|-|\\+)?([0-9A-Za-z-.]*)?";
    private static final Pattern PATTERN = Pattern.compile(FORMAT);

	private int major;
	private int minor;
	private int patch;
    private String separator;
	private String qualifier;

	public PluginVersion(int major, int minor, int patch) {
		this.major = major;
		this.minor = minor;
		this.patch = patch;
	}

	public PluginVersion(int major, int minor, int patch, String separator, String qualifier) {
		this.major = major;
		this.minor = minor;
		this.patch = patch;
        this.separator = separator;
		this.qualifier = qualifier;
	}

	public static PluginVersion createVersion(String version) {
        Matcher matcher = PATTERN.matcher(version);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("'" + version + "' does not match format '" + FORMAT + "'");
        }



        int major = Integer.valueOf(matcher.group(1));
        int minor = Integer.valueOf(matcher.group(2));
        int patch;
        String patchMatch = matcher.group(3);
        if (StringUtils.isNotEmpty(patchMatch)) {
            patch = Integer.valueOf(patchMatch);
        } else {
            patch = 0;
        }
        String separator = matcher.group(4);
        String qualifier = matcher.group(5);

        return new PluginVersion(major, minor, patch, separator, "".equals(qualifier) ? null : qualifier);
    }

	public int getMajor() {
		return this.major;
	}

	public int getMinor() {
		return this.minor;
	}

	public int getPatch() {
		return this.patch;
	}

    public String getQualifier() {
		return qualifier;
	}

	public String toString() {
        StringBuffer sb = new StringBuffer(50);
        sb.append(major);
        sb.append('.');
        sb.append(minor);
        sb.append('.');
        sb.append(patch);
        if (separator != null) {
            sb.append(separator);
        }
        if (qualifier != null) {
        	sb.append(qualifier);
        }

        return sb.toString();
    }

    @Override
    public int compareTo(PluginVersion version) {
        if (version.major > major) {
            return 1;
        } else if (version.major < major) {
            return -1;
        }

        if (version.minor > minor) {
            return 1;
        } else if (version.minor < minor) {
            return -1;
        }

        if (version.patch > patch) {
            return 1;
        } else if (version.patch < patch) {
            return -1;
        }

        return 0;
    }

    // for test only
    public static void main(String[] args) {
        PluginVersion v = PluginVersion.createVersion("1.2.3-SNAPSHOT");
        System.out.println(v.toString());
        PluginVersion v1 = PluginVersion.createVersion("4.1.0");
        System.out.println(v1.toString());
        PluginVersion v2  = PluginVersion.createVersion("4.0.32");
        System.out.println(v2.toString());
        System.out.println(v1.compareTo(v2));
    }

}
