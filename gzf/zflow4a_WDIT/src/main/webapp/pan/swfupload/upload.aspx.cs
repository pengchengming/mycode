using System;
using System.Data;
using System.Configuration;
using System.Collections;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Web.UI.HtmlControls;
using System.IO;
using System.Collections.Generic;
using Aliyun.OpenServices.OpenStorageService;

namespace Aliyun.swfupload
{
    public partial class upload : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            config c = new config();
            if (string.IsNullOrWhiteSpace(c.accessId))
                return;
            if (string.IsNullOrWhiteSpace(c.accessKey))
                return;
            if (string.IsNullOrWhiteSpace(c.bucketName)) 
                return;
            try
            {
                // Get the data
                HttpPostedFile upload = Request.Files["Filedata"];
                string firstName = DateTime.Now.ToString("yyyyMMddHHmmssfff");
                string lastName = Path.GetExtension(upload.FileName);
                string fullName = firstName + lastName;
                if (!string.IsNullOrEmpty(c.prefix))
                    fullName = c.prefix + "/" + fullName;
                ObjectMetadata metadata = new ObjectMetadata();
                // 可以设定自定义的metadata。
                metadata.ContentType = upload.ContentType;
                OssClient ossClient = new OssClient(c.accessId, c.accessKey);
                using (var fs = upload.InputStream)
                {
                    var ret = ossClient.PutObject(c.bucketName, fullName, fs, metadata);
                }
                Response.StatusCode = 200;
                Response.Write("http://" + c.bucketName + ".oss.aliyuncs.com/" + fullName);
            }
            catch
            {
                // If any kind of error occurs return a 500 Internal Server error
                Response.StatusCode = 500;
                Response.Write("An error occured");
                Response.End();
            }
            finally
            {
                // Clean up
                Response.End();
            }

        }
    }
}