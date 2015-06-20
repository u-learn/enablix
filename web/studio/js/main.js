/**/
/* on document load */
/**/
$(function()
{
	$('select').select2({
		closeOnSelect: false
	});


	$('.popup-opener').on('click', function()
	{
		$($(this).attr('href')).addClass('active');
		return false;
	});
	$('.popup-closer').on('click', function()
	{
		$(this).closest('.popup').removeClass('active');
		return false;
	});
	
	
	$('.toggle').on('click', function()
	{
		$(this).toggleClass('active');
		$($(this).attr('href')).slideToggle('fast');
		return false;
	});
	$('.head-toggle').on('click', function()
	{
		if( event.target.nodeName != 'A' )
		{
			$(this).toggleClass('active');
			$(this).next().slideToggle('fast');
		}
	});
	
	
	/*$('#side-nav .active > ul').show();
	$('#side-nav').on('click', 'i', function()
	{
		if( !$(this).parent('a').length )
		{
			$(this).parent().toggleClass('active');
			$(this).next().next('ul').slideToggle('fast');
		}
	});*/
});


/**/
/* on window load */
/**/
$(window).load(function()
{
});


/**/
/* on window scroll */
/**/
$(window).scroll(function()
{
});


/**/
/* on window resize */
/**/
$(window).resize(function()
{
});