/**
 * 本例程演示了通过Web上传文件过程中的进度显示。您可以对本例程进行任何修改和使用。
 * 如果需要转载本例程，请您注明作者。
 *
 * 作者： 刘作晨
 * EMail:liuzuochen@gmail.com
 */

package com.bizduo.zflow.servlet.upload;

/**
 * <p>Title: 后台服务</p>
 *
 * <p>Description: 为客户端提供上传及文件传输状态查询服务</p>
 *
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;

import com.aliyun.openservices.oss.model.AccessControlList;
import com.aliyun.openservices.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.bizduo.zflow.domain.sys.Employee;
import com.bizduo.zflow.domain.sys.Global;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.service.sys.IEmployeeService;
import com.bizduo.zflow.service.sys.IGlobalService;
import com.bizduo.zflow.service.sys.impl.EmployeeService;
import com.bizduo.zflow.util.UserUtil;


@SuppressWarnings("serial")
public class BackGroundService extends javax.servlet.http.HttpServlet implements
        javax.servlet.Servlet {
	
	
	@Autowired
	private IEmployeeService employeeService;	

	@Autowired
	private IGlobalService globalService;

    public static final String UPLOAD_DIR = "/upload";
    public static final String DEFAULT_UPLOAD_FAILURE_URL = "./result.jsp";

    public BackGroundService() {
        super();
    }


    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException,
            IOException {
        doPost(request, response);
    }

    /**
     * 从文件路径中取出文件名
     */
    private String takeOutFileName(String filePath) {
        int pos = filePath.lastIndexOf(File.separator);
        if (pos > 0) {
            return filePath.substring(pos + 1);
        } else {
            return filePath;
        }
    }

    /**
     * 从request中取出FileUploadStatus Bean
     */
    public static FileUploadStatus getStatusBean(
            HttpServletRequest request) {
        BeanControler beanCtrl = BeanControler.getInstance();
        return beanCtrl.getUploadStatus(request.getRemoteAddr());
    }

    /**
     * 把FileUploadStatus Bean保存到类控制器BeanControler
     */
    public static void saveStatusBean(
            HttpServletRequest request,
            FileUploadStatus statusBean) {
        statusBean.setUploadAddr(request.getRemoteAddr());
        BeanControler beanCtrl = BeanControler.getInstance();
        beanCtrl.setUploadStatus(statusBean);
    }

    /**
     * 删除已经上传的文件
     */
    @SuppressWarnings("deprecation")
	private void deleteUploadedFile(HttpServletRequest request) {
        FileUploadStatus satusBean = getStatusBean(request);
        for (int i = 0; i < satusBean.getUploadFileUrlList().size(); i++) {
            File uploadedFile = new File(request.getRealPath(UPLOAD_DIR) +
                                         File.separator +
                                         satusBean.getUploadFileUrlList().
                                         get(i));
            uploadedFile.delete();
        }
        satusBean.getUploadFileUrlList().clear();
        satusBean.setStatus("删除已上传的文件");
        satusBean.setStatus("4");
        saveStatusBean(request, satusBean);
    }

    /**
     * 上传过程中出错处理
     */
    private void uploadExceptionHandle(
            HttpServletRequest request,
            String errMsg) throws ServletException, IOException {
        //首先删除已经上传的文件
        deleteUploadedFile(request);
        FileUploadStatus satusBean = getStatusBean(request);
        satusBean.setStatus(errMsg);
        satusBean.setStatus("-1");
        saveStatusBean(request, satusBean);
    }

    /**
     * 初始化文件上传状态Bean
     */
    private FileUploadStatus initStatusBean(HttpServletRequest
            request) {
        FileUploadStatus satusBean = new FileUploadStatus();
        satusBean.setStatusCode("0");
        satusBean.setStatus("正在准备处理");
        satusBean.setUploadTotalSize(request.getContentLength());
        satusBean.setProcessStartTime(System.currentTimeMillis());
        satusBean.setBaseDir(request.getContextPath() + UPLOAD_DIR);
        return satusBean;
    }

    /**
     * 处理文件上传
     * @throws Exception 
     */
    @SuppressWarnings({ "deprecation", "unused", "rawtypes", "unchecked" })
	private String processFileUpload(HttpServletRequest request,
                                   HttpServletResponse response) throws
            Exception {
    	String returnstr="";
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //设置内存缓冲区，超过后写入临时文件
        factory.setSizeThreshold(10240000);
        //设置临时文件存储位置
        factory.setRepository(new File(request.getRealPath("/upload/temp")));
        ServletFileUpload upload = new ServletFileUpload(factory);
        //设置单个文件的最大上传值
        upload.setFileSizeMax(102400000);
        //设置整个request的最大值
        upload.setSizeMax(102400000);
        upload.setProgressListener(new FileUploadListener(request));
        //保存初始化后的FileUploadStatus Bean
        saveStatusBean(request, initStatusBean(request));

        URL url1= new URL("http://baidu.com");
        File file=null;
        String forwardURL = "";
        try {
            List items = upload.parseRequest(request);
            //获得返回url
            for (int i = 0; i < items.size(); i++) {
                FileItem item = (FileItem) items.get(i);
                if (item.isFormField()) {
                    forwardURL = item.getString();
                    break;
                }
            }
            //处理文件上传
            for (int i = 0; i < items.size(); i++) {
                FileItem item = (FileItem) items.get(i);

                //取消上传
                if (getStatusBean(request).getCancel()) {
                    deleteUploadedFile(request);
                    break;
                }
                //保存文件
                else if (!item.isFormField() && item.getName().length() > 0) {
                    String fileName = takeOutFileName(item.getName());
                    file = new File(request.getRealPath(UPLOAD_DIR) +
                                                 File.separator + fileName);
                    item.write(file);
                    //更新上传文件列表
                    FileUploadStatus satusBean =
                            getStatusBean(request);
                    satusBean.getUploadFileUrlList().add(fileName);
                    saveStatusBean(request, satusBean);
                    Thread.sleep(500);
                }
            }

        } catch (FileUploadException e) {
            uploadExceptionHandle(request, "上传文件时发生错误:" + e.getMessage());
        } catch (Exception e) {
            uploadExceptionHandle(request, "保存上传文件时发生错误:" + e.getMessage());
        }
        if (forwardURL.length() == 0) {
            forwardURL = DEFAULT_UPLOAD_FAILURE_URL;
        }
        
		User currentUser= UserUtil.getUser();
		employeeService = new EmployeeService();
		Employee currentemployee = employeeService.getByUserame(currentUser.getUsername());
		
		Global global02002 = globalService.getByCode("02002");
		Double globalsinglefilesize = Double.parseDouble( global02002.getVal());
		

		com.aliyun.openservices.oss.OSSClient ossClient;
		com.aliyun.openservices.oss.model.PutObjectResult  result;
		//URL url1=null;
		
		//FileUtil.saveFile(request.getSession().getServletContext().getRealPath("/WEB-INF"), file, "");
		 if (file != null)
         {
//			 if(file.getSize()>globalsinglefilesize*1024*1024 )
//			 {
//					results.put("code", 0);
//					results.put("message", "文件大小超过系统限制!");
//			 }
//			 else
			 {
             String accessid = "GpJqxQDmKAa7pouT";          // AccessID
             String accesskey = "8ccWp2nkm5qW8m15d7pIPMI23JLsRS";     // AccessKey
             String bucketName ="jahwa";
             ossClient = new com.aliyun.openservices.oss.OSSClient(accessid, accesskey); //当然这里可以封装下
             ObjectMetadata meta = new ObjectMetadata();
             meta.setContentLength(file.length());
//             meta.setContentType(file.getContentType()); 
             String key = currentemployee.getOpenId()+"/" + file.getName();
             
              
             result= ossClient.putObject(bucketName, key, new FileInputStream(file), meta);//上传图片
             AccessControlList accs = ossClient.getBucketAcl(bucketName);
             String imgurl;//String.Empty;
             if (accs.getGrants().size()>0)//判断是否有读取权限
             {
            	//生成一个签名的Uri 有效期24小时
            	 
            	 Date expires = new Date (new Date().getTime() + 1000 * 60*60*24);
            	 java.net.URL url = ossClient.generatePresignedUrl(bucketName, accesskey, expires);
            	 try {
					imgurl		 = url.toURI().toString();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	 
                 GeneratePresignedUrlRequest generatePresignedUrlRequest1 =
                 new GeneratePresignedUrlRequest(bucketName, key);
                 generatePresignedUrlRequest1.setExpiration(expires);
                 url1= ossClient.generatePresignedUrl(generatePresignedUrlRequest1);            	 
             }
             else
             {

                 imgurl =String.format("http://{0}.oss.aliyuncs.com/{1}", bucketName, key);
             } 
             
             Double employeeusedsize =0.00;
             employeeusedsize =  currentemployee.getUsedDiskSize();
             employeeusedsize += +file.length()/(1024*1024);
             currentemployee.setUsedDiskSize(employeeusedsize);
             employeeService.update(currentemployee);
             
              
             Global global = globalService.getByCode("09001");
             Double totaldiskusedsize =0.00;
             totaldiskusedsize =Double.parseDouble(global.getVal())+file.length()/(1024*1024);
             global.setVal(totaldiskusedsize.toString());
             globalService.update(global);
             
             
//             results.put("totalsize",currentemployee.getDiskSize());
//             results.put("usedsize",currentemployee.getUsedDiskSize());
             
//     		response.setContentType("text/plain; charset=utf-8");
//    		response.getWriter().print(url1.toString());
    		
//            mav = new ModelAndView("pan/uploadsuccess"); 
//    		mav.addObject("url", url1.toString());
//    		mav.addObject("filename",file.getOriginalFilename());
//    		results.put("code", 1);
//			results.put("batchNo", springJdbcService.getGenerateCode("gen_batch_number"));
//			results.put("message", "文件上传成功 !");
			 }
         }        
		 returnstr ="{\"message\":\"文件上传成功 !\",\"usedsize\":10.835,\"totalsize\":200,\"code\":1}";
		 return returnstr;
        //request.getRequestDispatcher(forwardURL).forward(request, response);
    }

    /**
     * 回应上传状态查询
     */
    private void responseStatusQuery(HttpServletRequest request,
                                              HttpServletResponse response) throws
            IOException {
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        FileUploadStatus satusBean = getStatusBean(request);
        response.getWriter().write(satusBean.toJSon());
    }

    /**
     * 处理取消文件上传
     */
    private void processCancelFileUpload(HttpServletRequest request,
                                         HttpServletResponse response) throws
            IOException {
        FileUploadStatus satusBean = getStatusBean(request);
        satusBean.setCancel(true);
        saveStatusBean(request, satusBean);
        responseStatusQuery(request, response);
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException,
            IOException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
        	String retString="";
			try {
				retString = processFileUpload(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
     		response.setContentType("text/plain; charset=utf-8");
     		response.getWriter().print(retString);
     		response.flushBuffer();
        
        } else {
            request.setCharacterEncoding("UTF-8");

            if (request.getParameter("uploadStatus") != null) {
                responseStatusQuery(request, response);
            }
            if (request.getParameter("cancelUpload") != null) {
                processCancelFileUpload(request, response);
            }

        }
    }
}
