/*
 *  Igapyon Diary system v3 (IgapyonV3).
 *  Copyright (C) 2015-2017  Toshiki Iga
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 *  Copyright 2015-2017 Toshiki Iga
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package jp.igapyon.diary.igapyonv3.plugin;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import jp.igapyon.diary.igapyonv3.inittemplate.IgInittemplateDiaryDir;

/**
 * mvn jp.igapyon.diary.igapyonv3.plugin:igapyonv3-maven-plugin:inittemplate
 */
@Mojo(name = "inittemplate")
public class IgInittemplateMojo extends AbstractMojo {
	@Parameter(property = "inittemplate.basedir", defaultValue = "${project.basedir}")
	private File basedir;

	public void execute() throws MojoExecutionException {
		try {
			System.err.println("igapyonv3-maven-plugin: inittemplate: basedir: " + basedir.getAbsolutePath());
			if (basedir == null) {
				basedir = new File(".");
			}
			// do normalize
			final File rootdir = basedir.getCanonicalFile();

			System.err.println("inittemplate called.: basedir=" + basedir);

			new IgInittemplateDiaryDir(rootdir).process();
		} catch (IOException e) {
			e.printStackTrace();
			throw new MojoExecutionException("Error processing: ", e);
		}
	}
}
