(function( factory ) {
	if ( typeof define === "function" && define.amd ) {
		define( ["jquery", "../jquery.validate"], factory );
	} else {
		factory( jQuery );
	}
}(function( $ ) {

/*
 * Translated default messages for the jQuery validation plugin.
 * Locale: ZH (Chinese, 中文 (Zhōngwén), 汉语, 漢語)
 */
$.extend($.validator.messages, {
	// required: "这是必填字段",
	// remote: "请修正此字段",
	// email: "请输入有效的电子邮件地址",
	// url: "请输入有效的网址",
	// date: "请输入有效的日期",
	// dateISO: "请输入有效的日期 (YYYY-MM-DD)",
	// number: "请输入有效的数字",
	// digits: "只能输入数字",
	// creditcard: "请输入有效的信用卡号码",
	// equalTo: "你的输入不相同",
	// extension: "请输入有效的后缀",
	// maxlength: $.validator.format("最多可以输入 {0} 个字符"),
	// minlength: $.validator.format("最少要输入 {0} 个字符"),
	// rangelength: $.validator.format("请输入长度在 {0} 到 {1} 之间的字符串"),
	// range: $.validator.format("请输入范围在 {0} 到 {1} 之间的数值"),
	// max: $.validator.format("请输入不大于 {0} 的数值"),
	// min: $.validator.format("请输入不小于 {0} 的数值")
	required: "This field is required.",
	remote: "Please fix this field.",
	email: "Please enter a valid email address.",
	url: "Please enter a valid URL.",
	date: "Please enter a valid date.",
	dateISO: "Please enter a valid date ( ISO ).",
	number: "Please enter a valid number.",
	digits: "Please enter only digits.",
	creditcard: "Please enter a valid credit card number.",
	equalTo: "Please enter the same value again.",
	maxlength: $.validator.format( "Please enter no more than {0} characters." ),
	minlength: $.validator.format( "Please enter at least {0} characters." ),
	rangelength: $.validator.format( "Please enter a value between {0} and {1} characters long." ),
	range: $.validator.format( "Please enter a value between {0} and {1}." ),
	max: $.validator.format( "Please enter a value less than or equal to {0}." ),
	min: $.validator.format( "Please enter a value greater than or equal to {0}." )
});

}));