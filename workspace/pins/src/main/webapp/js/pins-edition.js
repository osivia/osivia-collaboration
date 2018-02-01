$JQry(function() {

	// Sortable
	$JQry("ul.pins-edition-sortable").sortable({
		cancel: "a",
		connectWith : "ul.pins-edition-sortable",
		cursor : "move",
		forcePlaceholderSize : true,
		items: "li",
		placeholder : "list-sortable-placeholder bg-info",
		tolerance : "pointer",
		
		update : function(event, ui) {
			var $target = $JQry(event.target),
				$item = ui.item,
				$activeInput = $item.find("input[type=hidden][name$=active]"),
				$updatedInput = $item.find("input[type=hidden][name$=updated]"),
				$sortedInput = $item.find("input[type=hidden][name$=sorted]"),
				$ul = $item.closest("ul"),
				active = $ul.hasClass("active-pins");
			
			$activeInput.val(active);
			
			if (!$target.is($ul)) {
				$updatedInput.val(true);
			}
			
			$sortedInput.val(true);
		},
		
		stop : function(event, ui) {
			var $target = $JQry(event.target),
				$formGroup = $target.closest(".form-group"),
				$activeTasks = $formGroup.find("ul.active-pins > li")
				$submit = $formGroup.find("input[type=submit][name=sort]");
				
			$activeTasks.each(function(index, element) {
				var $element = $JQry(element),
					$orderInput = $element.find("input[type=hidden][name$=order]");
				
				$orderInput.val(index + 1);
			});
			
			$submit.click();
		}
	});
	$JQry(".pins-edition-sortable").disableSelection();
	
	

	
	
    $JQry(".pins-management select.select2[name=documentWebId]").each(function(index, element) {
        var $element = $JQry(element),
            url = $element.data("url"),
            options = {
                theme : "bootstrap"
            };

        options["ajax"] = {
            url : url,
            dataType : "json",
            delay : 1000,
            data : function(params) {
                return  {
                    filter: params.term,
                    page: params.page
                };
            },
            processResults : function(data, params) {
                params.page = params.page || 1;

                return {
                    results: data.items,
                    pagination: {
                        more: (params.page * data.pageSize) < data.total
                    }
                };
            },
            cache : true
        };


        options["templateResult"] = function(params) {
            var $result = $JQry(document.createElement("div")),
                $vignetteContainer, $vignette, $titleContainer, $icon, $title, $description;

            if (params.loading) {
                $result.text(params.text);
            } else if (params.message) {
                $result.addClass("text-muted");
                $result.text(params.message);
            } else {
                $result.addClass("document clearfix");

                // Vignette container
                $vignetteContainer = $JQry(document.createElement("div"));
                $vignetteContainer.addClass("vignette");
                $vignetteContainer.appendTo($result);

                if (params.vignette) {
                    // Vignette
                    $vignette = $JQry(document.createElement("img"));
                    $vignette.attr("src", params.vignette);
                    $vignette.attr("alt", "");
                    $vignette.appendTo($vignetteContainer);
                }

                // Document title container
                $titleContainer = $JQry(document.createElement("div"));
                $titleContainer.addClass("title");
                $titleContainer.appendTo($result);

                if (params.icon) {
                    // Document icon
                    $icon = $JQry(document.createElement("i"));
                    $icon.addClass(params.icon);
                    $icon.text("");
                    $icon.appendTo($titleContainer);
                }

                // Document title
                $title = $JQry(document.createElement("span"));
                $title.text(params.title);
                $title.appendTo($titleContainer);

                // Document description
                if (params.description) {
                    $description = $JQry(document.createElement("div"));
                    $description.addClass("description small text-muted");
                    $description.text(params.description);
                    $description.appendTo($result);
                }
            }

            return $result;
        };


        options["templateSelection"] = function(params) {
            var $element = $JQry(params.element),
                $selection,
                icon, title,
                $titleContainer, $icon, $title;

            if (params.selected) {
                icon = $element.data("icon");
                title = params.text;
            } else {
                icon = params.icon;
                title = params.title;
            }

            // Selection
            $selection = $JQry(document.createElement("span"));
            $selection.addClass("document");

            // Document title container
            $titleContainer = $JQry(document.createElement("span"));
            $titleContainer.addClass("title");
            $titleContainer.appendTo($selection);

            if (icon) {
                // Document icon
                $icon = $JQry(document.createElement("i"));
                $icon.addClass(icon);
                $icon.text("");
                $icon.appendTo($titleContainer);
            }

            // Document title
            $title = $JQry(document.createElement("span"));
            $title.text(title);
            $title.appendTo($titleContainer);

            return $selection;
        };


        // Internationalization
        options["language"] = {};
        if ($element.data("error-loading") !== undefined) {
            options["language"]["errorLoading"] = function() {
                return $element.data("error-loading");
            }
        }
        if ($element.data("loading-more") !== undefined) {
            options["language"]["loadingMore"] = function() {
                return $element.data("loading-more");
            }
        }
        if ($element.data("searching") !== undefined) {
            options["language"]["searching"] = function() {
                return $element.data("searching");
            }
        }
        if ($element.data("no-results") !== undefined) {
            options["language"]["noResults"] = function() {
                return $element.data("no-results");
            }
        }


        // Force width to 100%
        $element.css({
            width: "100%"
        });


        $element.select2(options);
        
        $element.on("select2:select", function(event) {
			var $target = $JQry(event.target),
				$form = $target.closest("form"),
				$submit = $form.find("input[type=submit][name=addPin]");
				
			$submit.click();
        });
    });
    
    
    $JQry(".pins-management select.select2[name=filterType]").each(function(index, element) {
        var $element = $JQry(element),
            options = {
                theme : "bootstrap",
                width : "resolve"
            };

        $element.select2(options);
    });
	
	
});
