var rUsability = function () {
    "use strict";


	//Select2
    var runSelect2 = function() {
		$(".search-select,.small-select2").select2({
			allowClear: false
		});
		
		
	};

    return {
        init: function () {
            runSelect2();
        }
    };
}();

var rUsabilityList = function () {
    "use strict";

    //runUsability
    //日期选择
   var runDaterangepicker = function(){
		if($('.reportrange').length) {
			$('.reportrange').daterangepicker({
				ranges: {
					'今天': [moment(), moment()],
					'昨天': [moment().subtract('days', 1), moment().subtract('days', 1)],
					'过去7天': [moment().subtract('days', 6), moment()],
					'过去30天': [moment().subtract('days', 29), moment()],
					'这个月': [moment().startOf('month'), moment().endOf('month')],
					'上个月': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')]
				},
				locale : {
					daysOfWeek: ['日', '一', '二', '三', '四', '五','六'],
            		monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
            		applyLabel: '确定',
                	cancelLabel: '取消',
                	fromLabel: '从',
                	toLabel: '至',
                	customRangeLabel: '自定义时间段',
				},
				startDate: moment().subtract('days', 29),
				endDate: moment()
			}, function(start, end) {
				$('.reportrange span').html(start.format('YYYY, MMM D') + ' - ' + end.format('YYYY, MMM D') + ' ');
			});
		}
	};



	//Select2
    var runSelect2 = function() {
		$(".search-select,.small-select2").select2({
			allowClear: false,
			minimumResultsForSearch: Infinity
		});
		
		
	};
    //首页可用性报表
    var runDataTables = function() {
		var oTable = $('#homeusability-table').dataTable({
			"bAutoWidth": false,
			"bPaginate": true, 
			"bSortCellsTop": true,
			"BProcessing": true,
			"aaSorting" : [[0, 'asc']],
			"sDom": "<'dt_tools_top no-padding'<'col-md-6'l><'col-md-6 text-right'i>r>t<'dt_tools_bt'<'col-md-6'><'col-md-6'p>>",
		
			"oLanguage" : {
				"sLengthMenu" : "显示 _MENU_ 条",
				"sSearch" : "搜索 ",
				"sInfo" : "显示 _START_ 至 _END_ 共计 _TOTAL_ 条",
				"oPaginate" : {
					"sPrevious" : "",
					"sNext" : ""
				}
			},
			"iDisplayLength" : 10,
		});
		
		$('#homeusability-table_wrapper .dataTables_filter input').addClass("form-control form-inline input-sm").attr("placeholder", "关键字");
		$('#homeusability-table_wrapper .dataTables_length select').addClass("m-wrap form-control small-select2");
		$('#homeusability-table_wrapper .dataTables_length select').select2({minimumResultsForSearch: Infinity});
	};
	
	

    return {
        init: function () {
            runDaterangepicker();
            runSelect2();
            runDataTables();
        }
    };
}();