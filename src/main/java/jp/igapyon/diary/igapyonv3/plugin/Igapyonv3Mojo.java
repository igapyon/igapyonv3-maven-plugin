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

import jp.igapyon.diary.igapyonv3.IgDiaryProcessor;

/**
 * mvn jp.igapyon.diary.igapyonv3.plugin:igapyonv3-maven-plugin:1.0:igdiary
 */
@Mojo(name = "igapyonv3")
public class Igapyonv3Mojo extends AbstractMojo {
	@Parameter(property = "igapyonv3.basedir", defaultValue = ".")
	private String basedir;

	public void execute() throws MojoExecutionException {
		try {
			if (basedir == null) {
				basedir = ".";
			}
			// カレントディレクトリを取得のうえ正規化します。
			final File rootdir = new File(basedir).getCanonicalFile();

			// 基本処理。
			new IgDiaryProcessor().process(rootdir);
		} catch (IOException e) {
			e.printStackTrace();
			throw new MojoExecutionException("Error processing: ", e);
		}
	}
}
