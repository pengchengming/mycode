var isIE8 = false, isIE9 = false, inner = $(".main-wrapper > .inner"), supportTransition = true, closedbar = $(".closedbar"), isMobile = false, isIEMobile = false, $body = $("body"), $windowWidth, $windowHeight,  sideLeft = $('#pageslide-left'), sideRight = $('#pageslide-right'), mainNavigation = $('.main-navigation'), sidebarWidth = sideLeft.outerWidth(true), topBar = $(".topbar"), mainContainer = $(".main-container"), mainContent = $(".main-content"), footer = $(".main-wrapper > .footer");
var thisSlider, actualItemWidth, newItemWidth, activeAnimation = false, hoverSideBar = false;
;

// Debounce Function
(function($, sr) {"use strict";
	// debouncing function from John Hann
	// http://unscriptable.com/index.php/2009/03/20/debouncing-javascript-methods/
	var debounce = function(func, threshold, execAsap) {
		var timeout;
		return function debounced() {
			var obj = this, args = arguments;

			function delayed() {
				if(!execAsap)
					func.apply(obj, args);
				timeout = null;
			};

			if(timeout)
				clearTimeout(timeout);
			else if(execAsap)
				func.apply(obj, args);

			timeout = setTimeout(delayed, threshold || 100);
		};
	};
	// smartresize
	jQuery.fn[sr] = function(fn) {
		return fn ? this.on('resize', debounce(fn)) : this.trigger(sr);
	};

})(jQuery, 'espressoResize');

//Main Function
var Main = function() {"use strict";
	//function to init app
	var runInit = function() {
		// Detection for IE Version
		if(/MSIE (\d+\.\d+);/.test(navigator.userAgent)) {
			var ieversion = new Number(RegExp.$1);
			if(ieversion == 8) {
				isIE8 = true;
				$body.addClass('isIE8');
			} else if(ieversion == 9) {
				isIE9 = true;
				$body.addClass('isIE9');
			}
		}
		// Detection for Mobile Device
		if(/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)) {
			isMobile = true;
			$body.addClass('isMobile');
		};
		// Detection for CSS Transitions Support
		var thisBody = document.body || document.documentElement, thisStyle = thisBody.style;
		supportTransition = thisStyle.transition !== undefined || thisStyle.WebkitTransition !== undefined || thisStyle.MozTransition !== undefined || thisStyle.MsTransition !== undefined || thisStyle.OTransition !== undefined;
		// active perfectScrollbar only in desktop devices
		if($body.hasClass("isMobile") == false && mainNavigation.length) {
			var nice = mainNavigation.niceScroll({
				height: 'auto',
                wheelStep: 25,
                cursorcolor: "#bdc6d1",
                cursorborder: "0px solid #fff",
                cursorborderradius: "0",
                cursorwidth: "5px"
			});
			var nice = $(".right-wrapper").niceScroll({
				height: 'auto',
                wheelStep: 25,
                cursorcolor: "#bdc6d1",
                cursorborder: "0px solid #fff",
                cursorborderradius: "0",
                cursorwidth: "5px"
			});

		}
		// clones the horizontal menu and inserts it into left sidebar for mobile devices
		if($("#horizontal-menu").length) {
			if($(".main-navigation-menu").length) {
				$("#horizontal-menu").find(".nav").clone().removeClass("nav navbar-nav").addClass("main-navigation-menu core-menu").find("li.dropdown").removeClass("dropdown").find("a").removeClass("dropdown-toggle").removeAttr("data-toggle").end().end().find("ul.dropdown-menu").removeClass("dropdown-menu").addClass("sub-menu").end().addClass("hidden-md hidden-lg").insertBefore(".main-navigation-menu");
			} else if($(".user-profile").length) {
				$("#horizontal-menu").find(".nav").clone().removeClass("nav navbar-nav").addClass("main-navigation-menu core-menu").find("li.dropdown").removeClass("dropdown").find("a").removeClass("dropdown-toggle").removeAttr("data-toggle").end().end().find("ul.dropdown-menu").removeClass("dropdown-menu").addClass("sub-menu").end().addClass("hidden-md hidden-lg").insertAfter(".user-profile");
			} else {
				$("#horizontal-menu").find(".nav").clone().removeClass("nav navbar-nav").addClass("main-navigation-menu core-menu").find("li.dropdown").removeClass("dropdown").find("a").removeClass("dropdown-toggle").removeAttr("data-toggle").end().end().find("ul.dropdown-menu").removeClass("dropdown-menu").addClass("sub-menu").end().addClass("hidden-md hidden-lg").prependTo(".main-navigation");
			}

		}


		// Add Fade Animation to Dropdown
		$('.dropdown').on('show.bs.dropdown', function(e) {
			$(this).find('.dropdown-menu').first().stop(true, true).fadeIn(300);
		});
		$('.dropdown').on('hide.bs.dropdown', function(e) {
			$(this).find('.dropdown-menu').first().stop(true, true).fadeOut(300);
		});

		// change closebar height when footer appear
		if($.fn.appear) {
			if(isMobile == false) {
				footer.appear();
				footer.on("appear", function(event, $all_appeared_elements) {

					closedbar.css({
						bottom: (footer.outerHeight(true) + 1) + "px"
					});
				});
				footer.on("disappear", function(event, $all_disappeared_elements) {

					closedbar.css({
						bottom: 1 + "px"
					});
				});
			}
		}

	};
	//function to get viewport/window size (width and height)
	var viewport = function() {
		var e = window, a = 'inner';
		if(!('innerWidth' in window )) {
			a = 'client';
			e = document.documentElement || document.body;
		}
		return {
			width: e[a + 'Width'],
			height: e[a + 'Height']
		};
	};
	//function to close searchbox, pageslide-left and pageslide-right when the user clicks outside of them
	var documentEvents = function() {
		$("html").click(function(e) {
			if(! e.isDefaultPrevented()) {
				if($('.search-box').is(":visible")) {
					$('.search-box').velocity({
						scale: 0.9,
						opacity: 0
					}, 400, 'easeInBack', function() {
						$(this).hide();
					});
				}

				if($body.hasClass("right-sidebar-open") && !hoverSideBar && !isMobile) {
					$(".sb-toggle-right").trigger("click");
				} else if($body.hasClass("sidebar-mobile-open") && !hoverSideBar && !isMobile) {
					$("header .sb-toggle-left").trigger("click");
				}
			}
		});
		if(isMobile) {
			$("html").swipe({
				swipeLeft: function(event, direction, distance, duration, fingerCount) {
					if($body.hasClass("sidebar-mobile-open")) {
						$("header .sb-toggle-left").trigger("click");
					}
				},
				swipeRight: function(event, direction, distance, duration, fingerCount) {
					if($body.hasClass("right-sidebar-open")) {
						$(".sb-toggle-right").trigger("click");
					}
				}
			});
		}

	};

	// function to handle SlideBar Toggle
	var runSideBarToggle = function() {
		$(".sb_toggle").click(function() {
			var sb_toggle = $(this);
			$("#slidingbar").slideToggle("fast", function() {
				if(sb_toggle.hasClass('open')) {
					sb_toggle.removeClass('open');
				} else {
					sb_toggle.addClass('open');
				}
			});
		});
	};
	// function to adjust the template elements based on the window size
	var runElementsPosition = function() {
		$windowWidth = viewport().width;
		$windowHeight = viewport().height;
		runContainerHeight();

	};

	// 自动高度计算
	var runContainerHeight = function() {
		
		if($windowWidth > 991) {
			mainNavigation.css({
				height: $windowHeight - topBar.outerHeight(true) - $(".slide-tools").outerHeight(true)
			});
			$(".navbar-content").css({
				height: $windowHeight - topBar.outerHeight(true)
			});
		} else {
			mainNavigation.css({
				height: $windowHeight - $(".slide-tools").outerHeight(true)
			});
			$(".navbar-content").css({
				height: $windowHeight
			});
		}

		$(".right-wrapper").css({
			height: $windowHeight
		});

		
		if($("#horizontal-menu").length) {
			mainContent.css({
				"min-height": $windowHeight - topBar.outerHeight(true) - footer.outerHeight(true)
			});
		} else {
			mainContent.css({
				"min-height": $windowHeight - topBar.outerHeight(true) - footer.outerHeight(true)
			});
		}


	};

	
	
	//NiceScroll 滚动条定义
	var runNiceScroll = function() {
		var nice = $("html, .Nscroll, .table-responsive").niceScroll(
			{
				height: 'auto',
                wheelStep: 50,
                cursorcolor: "#75d5ff",
                cursorborder: "0px solid #fff",
                cursorborderradius: "0",
                cursorwidth: "8px",
                zindex: "100"
			}
		);


	};
	// panel 滚动条定义
	var runPanelScroll = function() {
		if($(".panel-scroll").length && $body.hasClass("isMobile") == false) {
			$('.panel-scroll').niceScroll({
				height: 'auto',
                wheelStep: 50,
                cursorcolor: "#bdc6d1",
                cursorborder: "0px solid #fff",
                cursorborderradius: "0",
                cursorwidth: "5px"
			});
		}
	};
	//panel 工具动作定义
	var runModuleTools = function() {
		// fullscreen
		$('body').on('click', '.panel-expand', function(e) {
			e.preventDefault();
			$('.panel-tools > a, .panel-tools .dropdown').hide();

			if($('.full-white-backdrop').length == 0) {
				$body.append('<div class="full-white-backdrop"></div>');
			}
			var backdrop = $('.full-white-backdrop');
			var wbox = $(this).parent().parents('.panel');
			wbox.attr('style', '');
			if(wbox.hasClass('panel-full-screen')) {
				backdrop.fadeIn(200, function() {
					$('.panel-tools > .tmp-tool').remove();
					$('.panel-tools > a, .panel-tools .dropdown').show();
					wbox.removeClass('panel-full-screen');
					backdrop.fadeOut(200, function() {
						backdrop.remove();
						$(window).trigger('resize');
					});
				});
			} else {

				backdrop.fadeIn(200, function() {

					$('.panel-tools').append("<a class='panel-expand tmp-tool' href='#'><i class='iconfont icon-laptop'></i></a>");
					backdrop.fadeOut(200, function() {
						backdrop.hide();
					});
					wbox.addClass('panel-full-screen').css({
						'max-height': $windowHeight,
						'overflow': 'auto'
					});
					$(window).trigger('resize');
				});
			}
		});
		// panel close
		$('body').on('click', '.panel-close', function(e) {
			$(this).parents(".panel").fadeOut();
			e.preventDefault();
		});
		// panel refresh
		$('body').on('click', '.panel-refresh', function(e) {
			var el = $(this).parents(".panel");
			el.block({
				overlayCSS: {
					backgroundColor: '#fff'
				},
				message: '<i class="fa fa-spinner fa-spin"></i>',
				css: {
					border: 'none',
					color: '#333',
					background: 'none'
				}
			});
			window.setTimeout(function() {
				el.unblock();
			}, 1000);
			e.preventDefault();
		});
		// modal refresh
		$('body').on('click', '.modal-refresh', function(e) {
			var el = $(this).parents(".modal-body");
			el.block({
				overlayCSS: {
					backgroundColor: '#000'
				},
				message: '<i class="iconfont icon-loading f24 text-white"></i> <span class="text-white">数据在玩命载入中...</span>',
				css: {
					border: 'none',
					color: '#333',
					background: 'none'
				}
			});
			window.setTimeout(function() {
				el.unblock();
			}, 10000);
			e.preventDefault();
		});
		// panel collapse
		$('body').on('click', '.panel-collapse', function(e) {
			e.preventDefault();
			var el = $(this);
			var bodyPanel = jQuery(this).parent().closest(".panel").children(".panel-body");
			if($(this).hasClass("collapses")) {
				bodyPanel.slideUp(200, function() {
					el.addClass("expand").removeClass("collapses").children("span").text("展开").end().children("i").addClass("rotate-180");
				});
			} else {
				bodyPanel.slideDown(200, function() {
					el.addClass("collapses").removeClass("expand").children("span").text("收起").end().children("i").removeClass("rotate-180");
				});
			}
		});
	};
	var runNavigationMenu = function() {
		if($("body").hasClass("single-page") == false) {
			$('.main-navigation-menu > li.active').addClass('open');
			$('.main-navigation-menu > li a').on('click', function() {

				if($(this).parent().children('ul').hasClass('sub-menu') && ((!$body.hasClass('navigation-small') || $windowWidth < 767) || !$(this).parent().parent().hasClass('main-navigation-menu'))) {
					if(!$(this).parent().hasClass('open')) {
						$(this).parent().addClass('open');
						$(this).parent().parent().children('li.open').not($(this).parent()).not($('.main-navigation-menu > li.active')).removeClass('open').children('ul').slideUp(200);
						$(this).parent().children('ul').slideDown(200, function() {
							if(mainNavigation.height() > $(".main-navigation-menu").outerHeight()) {

								mainNavigation.scrollTo($(this).parent("li"), 300, {
									onAfter: function() {
										if($body.hasClass("isMobile") == false) {
											mainNavigation.perfectScrollbar('update');
										}
									}
								});
							} else {

								mainNavigation.scrollTo($(this).parent("li"), 300, {
									onAfter: function() {
										if($body.hasClass("isMobile") == false) {
											mainNavigation.perfectScrollbar('update');
										}
									}
								});
							}
						});
					} else {
						if(!$(this).parent().hasClass('active')) {
							$(this).parent().parent().children('li.open').not($('.main-navigation-menu > li.active')).removeClass('open').children('ul').slideUp(200, function() {
								if(mainNavigation.height() > $(".main-navigation-menu").outerHeight()) {
									mainNavigation.scrollTo(0, 300, {
										onAfter: function() {
											if($body.hasClass("isMobile") == false) {
												mainNavigation.perfectScrollbar('update');
											}
										}
									});
								} else {
									mainNavigation.scrollTo($(this).parent("li").closest("ul").children("li:eq(0)"), 300, {
										onAfter: function() {
											if($body.hasClass("isMobile") == false) {
												mainNavigation.perfectScrollbar('update');
											}
										}
									});
								}
							});
						} else {
							$(this).parent().parent().children('li.open').removeClass('open').children('ul').slideUp(200, function() {
								if(mainNavigation.height() > $(".main-navigation-menu").outerHeight()) {
									mainNavigation.scrollTo(0, 300, {
										onAfter: function() {
											if($body.hasClass("isMobile") == false) {
												mainNavigation.perfectScrollbar('update');
											}
										}
									});
								} else {
									mainNavigation.scrollTo($(this).parent("li"), 300, {
										onAfter: function() {
											if($body.hasClass("isMobile") == false) {
												mainNavigation.perfectScrollbar('update');
											}
										}
									});
								}
							});
						}
					}
				} else {
					$(this).parent().addClass('active');
				}
			});
		} else {
			var url, ajaxContainer = $("#ajax-content");
			var start = $('.main-navigation-menu li.start');
			if(start.length) {
				start.addClass("active");
				if(start.closest('ul').hasClass('sub-menu')) {
					start.closest('ul').parent('li').addClass('active open');
				}
				url = start.children("a").attr("href");
				ajaxLoader(url, ajaxContainer);
			}
			$('.main-navigation-menu > li.active').addClass('open');
			$('.main-navigation-menu > li a').on('click', function(e) {
				e.preventDefault();
				var $this = $(this);

				if($this.parent().children('ul').hasClass('sub-menu') && (!$('body').hasClass('navigation-small') || !$this.parent().parent().hasClass('main-navigation-menu'))) {
					if(!$this.parent().hasClass('open')) {
						$this.parent().addClass('open');
						$this.parent().parent().children('li.open').not($this.parent()).not($('.main-navigation-menu > li.active')).removeClass('open').children('ul').slideUp(200);
						$this.parent().children('ul').slideDown(200, function() {
							runContainerHeight();
						});
					} else {
						if(!$this.parent().hasClass('active')) {
							$this.parent().parent().children('li.open').not($('.main-navigation-menu > li.active')).removeClass('open').children('ul').slideUp(200, function() {
								runContainerHeight();
							});
						} else {
							$this.parent().parent().children('li.open').removeClass('open').children('ul').slideUp(200, function() {
								runContainerHeight();
							});
						}
					}
				} else {

					$('.main-navigation-menu ul.sub-menu li').removeClass('active');
					$this.parent().addClass('active');
					var closestUl = $this.parent('li').closest('ul');
					if(closestUl.hasClass('main-navigation-menu')) {
						$('.main-navigation-menu > li.active').removeClass('active').removeClass('open').children('ul').slideUp(200);
						$this.parents('li').addClass('active');
					} else if(!closestUl.parent('li').hasClass('active') && !closestUl.parent('li').closest('ul').hasClass('sub-menu')) {
						$('.main-navigation-menu > li.active').removeClass('active').removeClass('open').children('ul').slideUp(200);
						$this.parent('li').closest('ul').parent('li').addClass('active');
					} else {

						if(closestUl.parent('li').closest('ul').hasClass('sub-menu')) {
							if(!closestUl.parents('li.open').hasClass('active')) {
								$('.main-navigation-menu > li.active').removeClass('active').removeClass('open').children('ul').slideUp(200);
								closestUl.parents('li.open').addClass('active');
							}
						}

					}
					url = $(this).attr("href");
					ajaxLoader(url, ajaxContainer);
				};
			});
		}
	};
	// function to load content with ajax
	var ajaxLoader = function(url, element) {
		element.removeClass("fadeIn shake");
		var backdrop = $('.ajax-white-backdrop');

		$(".main-container").append('<div class="ajax-white-backdrop"></div>');
		backdrop.show();

		if($body.hasClass("sidebar-mobile-open")) {
			var configAnimation, extendOptions, globalOptions = {
				duration: 200,
				easing: "ease",
				mobileHA: true,
				progress: function() {
					activeAnimation = true;
				}
			};
			extendOptions = {
				complete: function() {
					inner.attr('style', '').removeClass("inner-transform");
					// remove inner-transform (hardware acceleration) for restore z-index
					$body.removeClass("sidebar-mobile-open");
					loadPage(url, element);
					activeAnimation = false;
				}
			};
			configAnimation = $.extend({}, extendOptions, globalOptions);

			inner.velocity({
				translateZ: 0,
				translateX: [-sidebarWidth, 0]
			}, configAnimation);
		} else {
			loadPage(url, element);
		}
		function loadPage(url, element) {
			$.ajax({
				type: "GET",
				cache: false,
				url: url,
				dataType: "html",
				success: function(data) {
					backdrop.remove();

					element.html(data).addClass("fadeIn");

				},
				error: function(xhr, ajaxOptions, thrownError) {
					backdrop.remove();
					element.html('<h4>Could not load the requested content.</h4>').addClass("shake");

				}
			});
		};
	};


	//function to Right and Left PageSlide
	var runToggleSideBars = function() {
		var configAnimation, extendOptions, globalOptions = {
			duration: 150,
			easing: "ease",
			mobileHA: true,
			progress: function() {
				activeAnimation = true;
			}
		};
		$("#pageslide-left, #pageslide-right").on("mouseover", function(e) {
			hoverSideBar = true;
		}).on("mouseleave", function(e) {
			hoverSideBar = false;
		});
		$(".sb-toggle-left, .closedbar").on("click", function(e) {
			if(activeAnimation == false) {
				if($windowWidth > 991) {
					$body.removeClass("sidebar-mobile-open");
					if($body.hasClass("sidebar-close")) {
						if($body.hasClass("layout-boxed") || $body.hasClass("isMobile"))
						 {
							$body.removeClass("sidebar-close");
							closedbar.removeClass("open");
							$(window).trigger('resize');
						} 
						else {
							closedbar.removeClass("open").hide();
							closedbar.css({
								//translateZ: 0,
								left: -closedbar.width()
							});

							extendOptions = {
								complete: function() {
									$body.removeClass("sidebar-close");
									$(".main-container, #pageslide-left, #footer .footer-inner, #horizontal-menu .container, .closedbar").attr('style', '');
									$(window).trigger('resize');
									activeAnimation = false;
								}
							};
							configAnimation = $.extend({}, extendOptions, globalOptions);
							$(".main-container, .footer .footer-inner, #horizontal-menu .container").velocity({
								//translateZ: 0,
								marginLeft: sidebarWidth
							}, configAnimation);

						}

					} else {
						if($body.hasClass("layout-boxed") || $body.hasClass("isMobile")) {
							$body.addClass("sidebar-close");
							closedbar.addClass("open");
							$(window).trigger('resize');
						} else {
							sideLeft.css({
								zIndex: 0

							});
							extendOptions = {
								complete: function() {
									closedbar.show().velocity({
										//translateZ: 0,
										left: 0
									}, 100, 'ease', function() {
										activeAnimation = false;
										closedbar.addClass("open");
										$body.addClass("sidebar-close");
										$(".main-container, .footer .footer-inner, #horizontal-menu .container, .closedbar").attr('style', '');
										$(window).trigger('resize');
									});
								}
							};
							configAnimation = $.extend({}, extendOptions, globalOptions);
							$(".main-container, .footer .footer-inner, #horizontal-menu .container").velocity({
								//translateZ: 0,
								marginLeft: 0
							}, configAnimation);
						}
					}

				} else {
					if($body.hasClass("sidebar-mobile-open")) {
						if(supportTransition) {
							extendOptions = {
								complete: function() {
									inner.attr('style', '').removeClass("inner-transform");
									// remove inner-transform (hardware acceleration) for restore z-index
									$body.removeClass("sidebar-mobile-open");
									activeAnimation = false;
								}
							};
							configAnimation = $.extend({}, extendOptions, globalOptions);

							inner.velocity({
								translateZ: 0,
								translateX: [-sidebarWidth, 0]
							}, configAnimation);
						} else {
							$body.removeClass("sidebar-mobile-open");
						}
					} else {
						if(supportTransition) {
							inner.addClass("inner-transform");
							// add inner-transform for hardware acceleration
							extendOptions = {
								complete: function() {
									inner.attr('style', '');
									$body.addClass("sidebar-mobile-open");
									activeAnimation = false;
								}
							};
							configAnimation = $.extend({}, extendOptions, globalOptions);
							inner.velocity({
								translateZ: 0,
								translateX: [sidebarWidth, 0]
							}, configAnimation);
						} else {
							$body.addClass("sidebar-mobile-open");
						}

					}
				}
			}
			e.preventDefault();
		});
		$(".sb-toggle-right").on("click", function(e) {
			if(activeAnimation == false) {
				if($windowWidth > 991) {
					$body.removeClass("sidebar-mobile-open");
				}
				if($body.hasClass("right-sidebar-open")) {
					if(supportTransition) {
						extendOptions = {
							complete: function() {
								inner.attr('style', '').removeClass("inner-transform");
								// remove inner-transform (hardware acceleration) for restore z-index
								$body.removeClass("right-sidebar-open");
								activeAnimation = false;
							}
						};
						configAnimation = $.extend({}, extendOptions, globalOptions);
						inner.velocity({
							translateZ: 0,
							translateX: [sidebarWidth, 0]
						}, configAnimation);
					} else {
						$body.removeClass("right-sidebar-open");
					}
				} else {
					if(supportTransition) {
						inner.addClass("inner-transform");
						// add inner-transform for hardware acceleration
						extendOptions = {
							complete: function() {
								inner.attr('style', '');
								$body.addClass("right-sidebar-open");
								activeAnimation = false;
							}
						};
						configAnimation = $.extend({}, extendOptions, globalOptions);
						inner.velocity({
							translateZ: 0,
							translateX: [-sidebarWidth, 0]
						}, configAnimation);
					} else {
						$body.addClass("right-sidebar-open");
					}
				}
			}
			e.preventDefault();
		});
	};
	
	// function to activate the Go-Top button
	var runGoTop = function(e) {
		$('.go-top').on('click', function(e) {
			$("html, body").animate({
				scrollTop: 0
			}, "slow");
			e.preventDefault();
		});
	};

	// Window Resize Function
	var runWIndowResize = function(func, threshold, execAsap) {
		//wait until the user is done resizing the window, then execute
		$(window).espressoResize(function() {
			runElementsPosition();
			//runRefreshSliders();
		});
	};

	var runMsViewport = function() {
		if(navigator.userAgent.match(/IEMobile\/10\.0/)) {
			var msViewportStyle = document.createElement("style");
			msViewportStyle.appendChild(document.createTextNode("@-ms-viewport{width:auto!important}"));
			document.getElementsByTagName("head")[0].appendChild(msViewportStyle);
		}
	};

	

	return {
		//main function to initiate template pages
		init: function() {
			runWIndowResize();
			runInit();
			runToggleSideBars();
			runElementsPosition();
			runNavigationMenu();
			runGoTop();
			runModuleTools();
			runPanelScroll();
			runSideBarToggle();
			runMsViewport();
			documentEvents();
			runNiceScroll();
		}
	};
}();
