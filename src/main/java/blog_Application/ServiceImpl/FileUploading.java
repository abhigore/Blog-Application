package blog_Application.ServiceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.websocket.server.ServerEndpoint;


public class FileUploading {
	
	Logger logger =LoggerFactory.getLogger(FileUploading.class);
	
	public final String UPLOAD_DIR ;//=new ClassPathResource("image/").getFile().getAbsolutePath();
	
	public FileUploading()throws IOException
	{
		  ClassPathResource resource = new ClassPathResource("static/image/");
		logger.info("UPLOAD_Dir is = " + resource.getFile().getAbsolutePath());
		UPLOAD_DIR =resource.getFile().getAbsolutePath();
		
		
		
	}
	public String FileUpload(MultipartFile file) throws IOException
	{
		
		String name =file.getOriginalFilename();
	    
		String randomid =UUID.randomUUID().toString();
		String filename ="";
		
		int lastindex =name.lastIndexOf(".");
		
		int lastCharIndex =name.length()-1;
		
		if(lastindex != -1)
		{
		  filename =randomid.concat(name.substring(lastindex));
		}
		else
		{
			System.out.println("Your name not content dot");
			
			filename =randomid.concat(name.substring(lastCharIndex));
			logger.info("Your name is = " +filename);
		}
	
		
	   Files.copy(file.getInputStream(),Paths.get(UPLOAD_DIR+File.separator+filename),StandardCopyOption.REPLACE_EXISTING);
	   
	    logger.info("uploaded file name is :  "+filename);
		
		return filename;
	
	}

}
