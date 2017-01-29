package jp.igapyon.diary.v3.plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import jp.igapyon.diary.v3.IgDiaryProcessor;

/**
 * mvn jp.igapyon.diary.v3.plugin:igdiary-maven-plugin:1.0:touch
 * 
 */
@Mojo(name = "touch", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class MyMojo extends AbstractMojo {
	/**
	 * Location of the file.
	 */
	@Parameter(defaultValue = "${project.build.directory}", property = "outputDir", required = true)
	private File outputDirectory;

	public void execute() throws MojoExecutionException {

		if (true) {
			try {
				// カレントディレクトリを取得のうえ正規化します。
				final File rootdir = new File(".").getCanonicalFile();
				if (rootdir.getName().equals("diary") == false) {
					System.err.println(
							"安全装置：処理停止。期待とは違うディレクトリで実行されました。このプログラムは diary ディレクトリでの実行を前提とします。:" + rootdir.getName());
					return;
				}

				// 基本処理。
				new IgDiaryProcessor().process(rootdir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		File f = outputDirectory;

		if (!f.exists()) {
			f.mkdirs();
		}

		File touch = new File(f, "touch.txt");

		FileWriter w = null;
		try {
			w = new FileWriter(touch);

			w.write("touch.txt");
		} catch (IOException e) {
			throw new MojoExecutionException("Error creating file " + touch, e);
		} finally {
			if (w != null) {
				try {
					w.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}
}
