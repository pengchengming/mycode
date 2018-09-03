<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"  lang="zh-cn">
<head id="Head1">
<meta charset="utf-8">
<title>
	阿里云图片管理
</title>
    <script src="js/jquery-1.4.2.js" type="text/javascript"></script>
    <link href="css/keditor.css" rel="stylesheet" type="text/css" /><link href="css/AspNetPager.css" rel="stylesheet" type="text/css" />
    <script src="swfupload/swfupload.js" type="text/javascript"></script>
    <script src="swfupload/handlers.js" type="text/javascript"></script>
    <script type="text/javascript">
        var swfu;
        window.onload = function () {
            swfu = new SWFUpload({
                // Backend Settings
                upload_url: "/pan/formUpload",
                post_params: {
                    "ASPSESSID": new Date().getMilliseconds
                },

                // File Upload Settings
                file_size_limit: "20 MB",
                file_types: "*",
                file_types_description: "Files",
                file_upload_limit: "0",    // Zero means unlimited

                // Event Handler Settings - these functions as defined in Handlers.js
                //  The handlers are not part of SWFUpload but are part of my website and control how
                //  my website reacts to the SWFUpload events.
                file_queue_error_handler: fileQueueError,
                file_dialog_complete_handler: fileDialogComplete,
                upload_progress_handler: uploadProgress,
                upload_error_handler: uploadError,
                upload_success_handler: uploadSuccess,
                upload_complete_handler: uploadComplete,

                // Button settings
                button_image_url: "swfupload/XPButtonNoText_60x22.png",
                button_placeholder_id: "spanButtonPlaceholder",
                button_width: 60,
                button_height: 22,
                button_text: '<span class="button">批量上传<span class="buttonSmall"></span></span>',
                button_text_style: '.button { font-family: Helvetica, Arial, sans-serif; font-size: 12pt; } .buttonSmall { font-size: 10pt; }',
                button_text_top_padding: 1,
                button_text_left_padding: 5,

                // Flash Settings
                flash_url: "swfupload/swfupload.swf", // Relative to this file

                custom_settings: {
                    upload_target: "divFileProgressContainer"
                },

                // Debug Settings
                debug: false
            });
        }
    </script>
    <script type="text/javascript">
        function select(img) {
            $(".ke-photo").css("background-color", "#FFFFFF");
            $(img.parentNode).css("background-color", "#CCCCFF");
            $("#hDel").val($(img).attr("alt"));
        }
    </script>
</head>
<body>
 <form method="post" action="/pan/formUpload" id="form1" enctype="multipart/form-data">
 

    <div id="UpdatePanel1">
	
            <div>
                <input class="button1"  type="file" name="FileUpload2" id="FileUpload2" />
                <input class="button1"  type="submit" name="btnUp" value="文件上传" id="btnUp" />
            </div>
        
</div>
 
    </form>
</body>
</html>
