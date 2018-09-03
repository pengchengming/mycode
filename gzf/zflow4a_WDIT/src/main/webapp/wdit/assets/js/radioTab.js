var formRadioTab = function () {
    "use strict";

    var runRadioTab = function () {
        $(".radio-tab-nav :radio").click(function() {
            var num = $(this).val();  
            $(".radioTab>div").hide();   
            $(".radioTab>div").eq(num).show(); 
        });
    };

    return {
        init: function () {
            runRadioTab();
        }
    };
}();