package jp.igapyon.diary.v3.plugin;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import jp.igapyon.diary.v3.IgDiaryProcessor;

/**
 * mvn jp.igapyon.diary.v3.plugin:igdiary-maven-plugin:1.0:igdiary
 */
@Mojo(name = "igdiary", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class MyMojo extends AbstractMojo {
	public void execute() throws MojoExecutionException {
		try {
			// カレントディレクトリを取得のうえ正規化します。
			final File rootdir = new File(".").getCanonicalFile();

			// 基本処理。
			new IgDiaryProcessor().process(rootdir);
		} catch (IOException e) {
			e.printStackTrace();
			throw new MojoExecutionException("Error processing: ", e);
		}
	}
}
