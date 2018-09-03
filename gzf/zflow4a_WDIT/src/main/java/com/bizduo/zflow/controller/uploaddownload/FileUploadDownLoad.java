package com.bizduo.zflow.controller.uploaddownload;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bizduo.zflow.domain.bizType.BizValue;
import com.bizduo.zflow.service.bizType.IBizTypeService;
import com.bizduo.zflow.util.TimeUitl;
import com.bizduo.zflow.util.UserUtil;
import com.bizduo.zflow.util.ccm.UploadFileStatus;
import com.bizduo.zflow.util.ccm.uploadFile.UploadFileUtil;

@Controller
@RequestMapping(value = "/fileUploadDownLoad")
public class FileUploadDownLoad {

	@Autowired
	private IBizTypeService  bizTypeService;
  
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	@ResponseBody
    public void formUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException{
		Map<String, String> map = new HashMap<String, String>();
		try {
			List<BizValue> list = bizTypeService.getBizValuesByCode(UploadFileStatus.FILE_PATH);
			String path = null;
			File newfile = null;
			if (list != null && list.size() > 0) {
				path = list.get(0).getDisplayValue();
				String formatStr = "yyyyMMdd";
				String savePth = path + "/"
						+ TimeUitl.getDatePart(new Date(), formatStr) + "/"
						+ UserUtil.getUser().getId();
				path = request.getSession().getServletContext()
						.getRealPath("/")
						+ savePth;
				newfile = new File(path);
				if (!newfile.exists() && !newfile.isDirectory()) {
					newfile.mkdirs();
				}
				Long fileNameLong = new Date().getTime();
				// 文件名
				String showName = file.getOriginalFilename();
				String fileName = "";
				int i = showName.lastIndexOf(".");
				if (i > -1 && i < showName.length()) {
					String extention = showName.substring(i); // --扩展名
					fileName = fileNameLong + "" + extention;
				}
				// 保存
				UploadFileUtil.copyFile(file.getInputStream(), path + "/"
						+ fileName);
				String returnPath = savePth + "/" + fileName;
				// String
				// returnJson="{'code':1,'fileName':'"+showName+"','path':'"+returnPath+"'}";

				map.put("code", "1");
				map.put("fileName", showName);
				map.put("path", returnPath);
				JSONObject returnJson = new JSONObject();
				returnJson.put("code", "1");
				returnJson.put("fileName", showName);
				returnJson.put("path", returnPath);
				response.setContentType("text/plain; charset=utf-8");
				response.getWriter().write(returnJson.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			String returnJson = "{'code':0}";
			map.put("code", "0");
			response.getWriter().write(returnJson.toString());
		}
    }  
 
    @RequestMapping("/downloadFile")    
    public void downloadFile(@RequestParam(value = "fileName", required = true) String fileName,
    		@RequestParam(value = "filePath", required = true) String filePath, 
    		HttpServletRequest request, HttpServletResponse response){    
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        try {  
			response.setHeader("Content-Disposition", "attachment;fileName=" + fileName.substring(fileName.lastIndexOf("/") + 1));
            File file = new File(request.getSession().getServletContext().getRealPath("/") + filePath);        
            InputStream inputStream = new FileInputStream(file);    
            OutputStream os = response.getOutputStream();    
            byte[] b = new byte[1024];    
            int length;    
            while((length = inputStream.read(b))>0)    
                os.write(b, 0, length);    
            inputStream.close();    
        } catch (FileNotFoundException e) {    
            e.printStackTrace();    
        } catch (IOException e) {    
            e.printStackTrace();    
        }    
    } 
    
    
    @RequestMapping(value = "/uploadImgeFile", method = RequestMethod.POST)
	@ResponseBody
    public void uploadImgeFile(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException{
		Map<String, String> map = new HashMap<String, String>();
		try {
			List<BizValue> list = bizTypeService.getBizValuesByCode(UploadFileStatus.FILE_PATH);
			String path = null;
			File newfile = null;
			if (list != null && list.size() > 0) {
				path = list.get(0).getDisplayValue();
				String formatStr = "yyyyMMdd";
				String savePth = path + "/"
						+ TimeUitl.getDatePart(new Date(), formatStr) + "/"
						+ UserUtil.getUser().getId();
				path = request.getSession().getServletContext()
						.getRealPath("/")
						+ savePth;
				newfile = new File(path);
				if (!newfile.exists() && !newfile.isDirectory()) {
					newfile.mkdirs();
				}
				Long fileNameLong = new Date().getTime();
				// 文件名
				String showName = file.getOriginalFilename();
				String fileName = "";
				int i = showName.lastIndexOf(".");
				if (i > -1 && i < showName.length()) {
					String extention = showName.substring(i); // --扩展名
					fileName = fileNameLong + "" + extention;
				}
				// 保存
				UploadFileUtil.copyFile(file.getInputStream(), path + "/"
						+ fileName);
				String returnPath = savePth + "/" + fileName;
				// String
				// returnJson="{'code':1,'fileName':'"+showName+"','path':'"+returnPath+"'}";
				
				//返回json
				JSONObject returnJson = new JSONObject();
				//缩略图
				String force= request.getParameter("force");
				if(force!=null&&force.equals("1")){
					String w= request.getParameter("w");
					String h= request.getParameter("h");
					if(w!=null&&!w.trim().equals("")
							&&h!=null&&!h.trim().equals("")){
						/*CommonsMultipartFile cf= (CommonsMultipartFile)file; 
				        DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 
				        File f = fi.getStoreLocation();*/
						File f = new File(path+ "/"+fileName);
						thumbnailImage(f,  Integer.parseInt(w), Integer.parseInt(h), path, fileName, 1);
						returnJson.put("smallImagePath", savePth + "/small/" + fileName);
					}
				}
				/*map.put("code", "1");
				map.put("fileName", showName);
				map.put("path", returnPath);
				*/
				returnJson.put("code", "1");
				returnJson.put("imageName", showName);
				returnJson.put("imagePath", returnPath);
				response.setContentType("text/plain; charset=utf-8");
				response.getWriter().write(returnJson.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			String returnJson = "{'code':0}";
			map.put("code", "0");
			response.getWriter().write(returnJson.toString());
		}
    }  
    
    public static void saveImage(BufferedImage image, String format,  
            String filePath) {  
        try {  
            ImageIO.write(image, format, new File(filePath));  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
    /**
     * 缩略图
     * @param imgFile 图片的File
     * @param w  夸
     * @param h  长
     * @param savePth  保存的地址
     * @param fileName 保存的文件名称
     * @param force  是否要缩放图片
     * File imgFile,
     */
	public void thumbnailImage(File imgFile, int w, int h, String savePth,String fileName, int force)throws IOException{
    	/* 
        FileInputStream fis = null ;  
        ImageInputStream iis =null ;  
        try{     
            //读取图片文件   
        	//File f = new File(savePth + "/"+ fileName);
            String suffix="";
            int i = fileName.lastIndexOf(".");
			if (i > -1 && i < fileName.length()) {
				suffix = fileName.substring(i+1); // --扩展名 
			}
			
            fis = new FileInputStream(imgFile);   
            //String types = Arrays.toString(ImageIO.getReaderFormatNames());
            Iterator it = ImageIO.getImageReadersByFormatName(suffix);
            ImageReader reader = (ImageReader) it.next();   
            //获取图片流    
            iis = ImageIO.createImageInputStream(fis);    
            reader.setInput(iis,true) ;  
            ImageReadParam param = reader.getDefaultReadParam();   
            //定义一个矩形   
            Rectangle rect = new Rectangle(0, 0, w, h);   
            //提供一个 BufferedImage，将其用作解码像素数据的目标。    
            param.setSourceRegion(rect);  
            BufferedImage bi = reader.read(0,param);                  
            //保存新图片    
            File newfile = new File(savePth+ "/small");
            if (!newfile.exists() && !newfile.isDirectory()) {
				newfile.mkdirs();
			}
        
            ImageIO.write(bi, suffix, new File(savePth+ "/small/"+fileName));       
        } catch (Exception e) {
           e.printStackTrace();
        }finally{  
            if(fis!=null)  
                fis.close();         
            if(iis!=null)  
               iis.close();   
        }   */
        
        
        if(imgFile.exists()){
          try {
        	// ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
              String types = Arrays.toString(ImageIO.getReaderFormatNames());
              String suffix = null;
              // 获取图片后缀
              if(imgFile.getName().indexOf(".") > -1) {
                  suffix = imgFile.getName().substring(imgFile.getName().lastIndexOf(".") + 1);
              }// 类型和图片后缀全部小写，然后判断后缀是否合法
              if(suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()) < 0){ 
                  return ;
              } 
              Image img = ImageIO.read(imgFile);
              
                  int width = img.getWidth(null);
                  int height = img.getHeight(null);
                  if((width*1.0)/w < (height*1.0)/h){
                      if(width > w){
                          h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w/(width*1.0)));
                      }
                  } else {
                      if(height > h){
                          w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h/(height*1.0)));
                      }
                  }
              
              BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
              Graphics g = bi.getGraphics();
              g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
              g.dispose();
                // 将图片保存在原目录并加上前缀
                File newfile = null;
                newfile = new File(savePth+ "/small");
                if (!newfile.exists() && !newfile.isDirectory()) {
					newfile.mkdirs();
				}      
                ImageIO.write(bi, suffix, new File(savePth+ "/small/"+fileName ));
                
            } catch (IOException e) {
               //log.error("generate thumbnail image failed.",e);
            }
        }else{
            //log.warn("the image is not exist.");
        }
        
    }
    
    
}
