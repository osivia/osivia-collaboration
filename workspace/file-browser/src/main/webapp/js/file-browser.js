var fileBrowserToolbarXhr;


$JQry(function() {
	var isChromeAndroid = /Chrome/i.test(navigator.userAgent) && /Mobile/i.test(navigator.userAgent) && /Android/i.test(navigator.userAgent);
	var $browser = $JQry(".file-browser");
	var previousIndex = -1;
	
	if (!isChromeAndroid && !$browser.data("loaded")) {
		// Draggable
		$JQry(".file-browser-draggable").draggable({
			addClasses: false,
			appendTo: "body",
			connectToFancytree: true,
			cursor: "move",
			distance: 10,
			revert: "invalid",
			revertDuration: 200,
			
			helper: function(event) {
				var $target = $JQry(event.target);
				var $draggable = $target.closest(".file-browser-draggable");
				var $selectee = $draggable.closest(".portal-table-selectable-filter");
				var $selectable = $selectee.closest(".portal-table-selectable");
				var offset = $draggable.offset();
				var click = {
					top : event.pageY - offset.top,
					left : event.pageX - offset.left
				};

				// Selected items
				var $selected;
				if ($selectee.hasClass("ui-selected")) {
					$selected = $selectable.find(".ui-selected");
				} else {
					$selected = $selectee;
				}

				// Identifiers, types & text
				var identifiers = "";
				var types = "";
				var text = "";
				$selected.each(function(index, element) {
					var $element = $JQry(element);
					
					if (index > 0) {
						identifiers += ",";
						types += ",";
						text += ", ";
					}
					
					identifiers += $element.data("id");
					types += $element.data("type");
					text += $element.data("text");
				});
				
				// Helper
				var $helper = $JQry(document.createElement("div"));
				$helper.addClass("file-browser-helper");
				$helper.data({
					identifiers: identifiers,
					types: types
				});
				$helper.css({
					height: 0,
					width: 0
				});
				
				// Helper content
				var $content = $JQry(document.createElement("div"));
				$content.addClass("bg-primary border-primary");
				$content.appendTo($helper);
				
				// Helper content animation
				$content.css({
					width: $selectee.width()
				});
				$content.animate({
					top: click.top + 1,
					left: click.left + 1,
					width: 300
				}, 300);
				
				// Text
				var $text = $JQry(document.createElement("div"));
				$text.addClass("text-overflow");
				$text.text(text);
				$text.appendTo($content);
				
				return $helper;
			},
			
			start: function(event, ui) {
				var $target = $JQry(event.target);
				var $selectee = $target.closest(".portal-table-selectable-filter");
				var $selectable = $selectee.closest(".portal-table-selectable");
				var $selected = $selectable.find(".ui-selected");
				var $browser = $target.closest(".file-browser");
				var $toolbar = $browser.find(".portal-table-toolbar-container");

				if ($selectee.hasClass("ui-selected")) {
					$selected.addClass("file-browser-dragged");
					
					// Abort previous AJAX request
					if (fileBrowserToolbarXhr && fileBrowserToolbarXhr.readyState != 4) {
						fileBrowserToolbarXhr.abort();
						
						// Disable toolbar
						$toolbar.find("a").addClass("disabled");
				    }
				} else {
					$selectee.addClass("ui-selected file-browser-dragged");
					
					// Deselect
					$selected.not($selectee).removeClass("ui-selected");
					
					// Disable toolbar
					$toolbar.find("a").addClass("disabled");
				}
			},
			
			stop: function(event, ui) {
				var $target = $JQry(event.target);
				var $selectee = $target.closest(".portal-table-selectable-filter");
				var $selectable = $selectee.closest(".portal-table-selectable");
				var $dragged = $selectable.find(".file-browser-dragged");
			
				$dragged.removeClass("file-browser-dragged");
				
				// Update toolbar
				updateTableToolbar($target);
			}
		});
		
		
		// Click on draggable shadowbox
		$JQry(".file-browser-draggable-shadowbox").click(function(event) {
			var $target = $JQry(event.target);
			var $selectee = $target.closest(".portal-table-selectable-filter");
			var $selectable = $selectee.closest(".portal-table-selectable");
			
			if (event.ctrlKey) {
				$selectee.removeClass("ui-selected");
			} else {
				$selectable.find(".ui-selected").each(function(index, element) {
					var $element = $JQry(element);
					
					if (!$element.is($selectee)) {
						$element.removeClass("ui-selected");
					}
				});
			}
			
			// Update toolbar
			updateTableToolbar($target);
		});
		
		
		// Droppable
		$JQry(".file-browser-droppable").droppable({
			addClasses: false,
			hoverClass: "bg-info border-info",
			tolerance: "pointer",
			
			accept: function($draggable) {
				var $droppable = $JQry(this);
				var $selectee = $droppable.closest(".portal-table-selectable-filter");
				var $selectable = $selectee.closest(".portal-table-selectable");
				var $selected = $selectable.find(".ui-selected");
				var targetAcceptedTypes = $droppable.data("accepted-types").split(",");
				var accepted = true;
					
				if ($draggable.hasClass("ui-sortable-helper") || $selectee.hasClass("file-browser-dragged")) {
					// Prevent drop on sortable or selected element
					accepted = false;
				} else {
					$selected.each(function(index, element) {
						var $element = $JQry(element);
						var sourceType = $element.data("type");
						var match = false;
						
						jQuery.each(targetAcceptedTypes, function(index, targetType) {
							if (sourceType === targetType) {
								match = true;
								return false;
							}
						});
						
						if (!match) {
							accepted = false;
							return false;
						}
					});
				}
				
				return accepted;
			},
			
			drop: function(event, ui) {
				// Source
				var $source = $JQry(ui.helper.context);
				var sourceIdentifiers = $source.data("identifiers");
					
				// Target
				var $target = $JQry(event.target);
				var targetId = $target.closest(".portal-table-selectable-filter").data("id");
					
				// AJAX parameters
				var container = null;
				var options = {
					requestHeaders : [ "ajax", "true", "bilto" ],
					method : "post",
					postBody : "sourceIds=" + sourceIdentifiers + "&targetId=" + targetId,
					onSuccess : function(t) {
						onAjaxSuccess(t, null);
					}
				};
				var url = $target.closest(".file-browser").data("drop-url");
				var eventToStop = null;
				var callerId = null;
				
				directAjaxCall(container, options, url, eventToStop, callerId);
			}
		});
		
		
		// File Upload
		$JQry(".file-browser-upload").fileupload({
			dropZone : ".file-browser-drop-zone",
			singleFileUploads : true,
			
			add: function(event, data) {
				var $target = $JQry(event.target);
				var $files = $target.find(".file-browser-upload-files");
				var $browser = $target.closest(".file-browser");
				var maxFileSize = $target.data("max-file-size");
				var messageWarningReplace = $target.data("warning-replace");
				var messageErrorSize = $target.data("error-size");

				$target.removeClass("hidden");
				
				data.context = $JQry(document.createElement("li"));
				data.context.appendTo($files);
				
				$JQry.each(data.files, function(index, file) {
					var $file = $JQry(document.createElement("p"));
					$file.appendTo(data.context);
					
					// Text
					var $text = $JQry(document.createElement("span"));
					$text.appendTo($file);
					
					// File name
					var $fileName = $JQry(document.createElement("span"));
					$fileName.text(file.name);
					$fileName.appendTo($text);
					
					if (file.size > maxFileSize) {
						// Error
						var $error = $JQry(document.createElement("strong"));
						$error.addClass("text-danger");
						$error.text(messageErrorSize);
						$error.appendTo($text);
					} else {
						if ($browser.find("[data-text=\"" + file.name + "\"]").length) {
							// Warning
							var $warning = $JQry(document.createElement("small"));
							$warning.addClass("text-warning");
							$warning.text(messageWarningReplace);
							$warning.appendTo($text);
						}
						
						if (!index) {
							// Start
							var $start = $JQry(document.createElement("button"));
							$start.attr("type", "button");
							$start.addClass("start hidden");
							$start.click(function() {
								data.submit(event);
							});
							$start.appendTo($file);
						}
					}
					
					if (!index) {
						// Cancel
						var $cancel = $JQry(document.createElement("button"));
						$cancel.attr("type", "button");
						$cancel.addClass("cancel btn btn-link btn-sm");
						$cancel.append($JQry(document.createElement("i")).addClass("glyphicons glyphicons-remove"));
						$cancel.click(function(event) {
							data.abort();
						})
						$cancel.appendTo($file);
					}
		        });
			},

			stop: function(event, data) {
				var $target = $JQry(event.target);
				var url = $target.data("callback-url");
				
				$target.addClass("hidden");
				
				// Refresh
				updatePortletContent(this, url);
			},
			
			progressall: function(event, data) {
				var progress = parseInt(data.loaded / data.total * 100, 10) + "%";
				$JQry(".file-browser .progress-bar").css("width", progress);
			}
		});
		
		$JQry(".file-browser-upload .fileupload-buttonbar button[type=reset]").click(function(event) {
			var $target = $JQry(event.target);
			var $form = $target.closest(".file-browser-upload");
			
			$form.addClass("hidden");
		})
		
		
		// Drag over
		$JQry(document).bind("dragover", function(e) {
			e.preventDefault();
			
			var $target = $JQry(e.target);
			var $hoveredDropZone = $target.closest(".file-browser-drop-zone");
			var $dropZone = $JQry(".file-browser-drop-zone");
			var timeout = window.dropZoneTimeout;
			
			if (!timeout) {
				$dropZone.addClass("in");
			} else {
				clearTimeout(timeout);
			}

			if ($hoveredDropZone.length) {
				$hoveredDropZone.find(".file-browser-upload-shadowbox").addClass("bg-info border-info");
			} else {
				$dropZone.find(".file-browser-upload-shadowbox").removeClass("bg-info border-info");
			}
			
			window.dropZoneTimeout = setTimeout(function() {
				window.dropZoneTimeout = null;
				$dropZone.removeClass("in");
				$dropZone.find(".file-browser-upload-shadowbox").removeClass("bg-info border-info");
			}, 1000);
		});
		
		
		// Drop
		$JQry(document).bind("drop", function(e) {
			var $dropZone = $JQry(".file-browser-drop-zone");
			
			$dropZone.removeClass("in");
			$dropZone.find(".file-browser-upload-shadowbox").removeClass("bg-info border-info");
		});
		
		
		// Loaded indicator
		$browser.data("loaded", true);
	}
});
