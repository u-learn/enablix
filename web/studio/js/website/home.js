function resize(){
    if($(window).width() > 757){
    	$("#slogan").css({"min-height": $(window).height() - 120 + "px"});
    } else {
    	if($(window).width() > 488)
    		$("#slogan").css({"min-height": $(window).height() - 192 + "px"});
    	else
    		$("#slogan").css({"min-height": $(window).height() - 255 + "px"});
    }
}
$(document).ready(function(){
	resize();
    $(".fancybox").fancybox({
        'width': 350,
        'height': 600,
        'closeBtn': false
    });
});
$(window).on('resize', function() {
    resize();
})