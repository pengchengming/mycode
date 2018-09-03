<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"  lang="zh-cn">
<head id="Head1">
<meta charset="utf-8">
<title>
	阿里云云盘管理
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
                accessId：<input name="accessId" type="text" id="accessId" style="width:109px;" value="GpJqxQDmKAa7pouT" />
                accessKey：<input name="accessKey" type="text" id="accessKey"  value=" 8ccWp2nkm5qW8m15d7pIPMI23JLsRS"/>
                bucketName：<input name="bucketName" type="text" id="bucketName"  value="jahwa"/>
                <br />

                前缀（类似目录，可为空）<input name="txtPrefix" type="text" id="txtPrefix" value="test" />
<form method="post" action="/zflow/pan/formUpload" id="form1" enctype="multipart/form-data">
 

    <div id="UpdatePanel1">
	
            <div>

                <input type="submit" name="btnGet" value="获取" id="btnGet" />
                <input type="file" name="FileUpload2" id="FileUpload2" /><input type="submit" name="btnUp" value="单文件上传" id="btnUp" />
            </div>
        
</div>
    <div class="ke-plugin-filemanager-header">
        <div class="ke-left">
            <input type="hidden" name="hDel" id="hDel" />
            <input type="submit" name="btnDel" value="删除" id="btnDel" />
        </div>
        <div class="ke-right">
            <span id="spanButtonPlaceholder"></span>
        </div>
        <div class="ke-clearfix">
        </div>
    </div>
    <div id="upp2">
	
            <div id="thumbnails" class="ke-plugin-filemanager-body">
<table>
<tr><td></td><td>文件名</td><td>大小</td><td>类型</td><td>创建时间</td><td>失效时间</td></tr>
<tr><td></td><td>. . /(返回上一级)</td><td> </td><td></td><td></td><td></td></tr>
<tr><td><input id="object-item-id-0" type="checkbox" /></td></t></d><td>Data.xlsx</td><td>46.057KB</td><td>xlsx</td><td>2014-12-29 13:01:04</td><td>2015-12-29 13:01:04</td></tr>
<tr><td><input id="object-item-id-1" type="checkbox" /></td><td>MySQL.rar</td><td>44.119MB</td><td>xlsx</td><td>2014-12-29 13:01:04</td><td>2015-12-29 13:01:04</td></tr>
</table>
            </div>
            <div class="ke-dialog-footer">
                

            </div>
        
</div>
    </form>
</body>
</html>
