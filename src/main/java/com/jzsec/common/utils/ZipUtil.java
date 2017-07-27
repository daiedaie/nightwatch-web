/**
 * 
 */
package com.jzsec.common.utils;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

/**
 * @author 劉 焱
 * @date 2017-5-31
 */
public class ZipUtil {
	private static final Project DEFAULT_PROJECT = new Project();
	
	public static void unZip(File orgin, File dest) {
		Expand expand = new Expand();
		expand.setProject(DEFAULT_PROJECT);
		expand.setSrc(orgin);
		expand.setDest(dest);
		expand.execute();
	}
	
	public static void zip(File orgin, File dest) {
		Zip zip = new Zip();
		zip.setProject(DEFAULT_PROJECT);
		zip.setDestFile(dest);
		
		FileSet fs = new FileSet();
		fs.setProject(DEFAULT_PROJECT);
		fs.setDir(orgin);
        //fileSet.setIncludes("**/*.java"); 包括哪些文件或文件夹 eg:zip.setIncludes("*.java");  
        //fileSet.setExcludes(...); 排除哪些文件或文件夹  
		
		zip.addFileset(fs);
		zip.execute();
	}

}
