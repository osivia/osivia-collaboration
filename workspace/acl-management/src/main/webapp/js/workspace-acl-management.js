$JQry(function() {

	$JQry(".workspace-acl-management select.select2").each(function(index, element) {
		var $element = $JQry(element),
			options = {
				theme : "bootstrap"
			};
		
		// Result template
		options["templateResult"] = function(params) {
			var $element = $JQry(params.element),
				type = $element.data("type"),
				displayName = $element.data("displayname"),
				avatar = $element.data("avatar"),
				extra = $element.data("extra");
			
			$result = $JQry(document.createElement("div"));
			
			if (params.loading) {
				$result.text(params.text);
			} else {
				// Update extra informations
				if (type === "USER") {
					if (extra) {
						extra = params.id + " – " + extra;
					} else {
						extra = params.id;
					}
				}
				
				
				$result.addClass("workspace-record clearfix");
				
				// Icon container
				$iconContainer = $JQry(document.createElement("div"));
				$iconContainer.addClass("form-control-static pull-left text-center icon");
				$iconContainer.appendTo($result)
				
				if (type === "GROUP") {
					$icon = $JQry(document.createElement("i"));
					$icon.addClass("glyphicons glyphicons-group")
					$icon.text("");
					$icon.appendTo($iconContainer);
				} else if (avatar) {
					$avatar = $JQry(document.createElement("img"));
					$avatar.addClass("avatar");
					$avatar.attr("src", avatar);
					$avatar.attr("alt", "");
					$avatar.appendTo($iconContainer);
				} else {
					$icon = $JQry(document.createElement("i"));
					$icon.addClass("glyphicons glyphicons-user")
					$icon.text("");
					$icon.appendTo($iconContainer);
				}

				// Display name
				$displayName = $JQry(document.createElement("div"));
				$displayName.addClass("text-overflow");
				if (!extra) {
					$displayName.addClass("form-control-static");
				}
				$displayName.text(displayName);
				$displayName.appendTo($result);
				
				// Extra
				if (extra) {
					$extra = $JQry(document.createElement("div"));
					$extra.addClass("text-overflow text-muted small");
					$extra.text(extra);
					$extra.appendTo($result);
				}
			}

			return $result;
		};
		
		
		// Selection template
		options["templateSelection"] = function(params) {
			var $element = $JQry(params.element),
				type = $element.data("type"),
				displayName = $element.data("displayname"),
				avatar = $element.data("avatar");

			// Selection
			$selection = $JQry(document.createElement("div"));		
			
			if (type === "GROUP") {
				$icon = $JQry(document.createElement("i"));
				$icon.addClass("glyphicons glyphicons-group")
				$icon.text("");
				$icon.appendTo($selection);
			} else if (avatar) {
				$avatar = $JQry(document.createElement("img"));
				$avatar.addClass("avatar");
				$avatar.attr("src", avatar);
				$avatar.attr("alt", "");
				$avatar.appendTo($selection);
			} else {
				$icon = $JQry(document.createElement("i"));
				$icon.addClass("glyphicons glyphicons-user")
				$icon.text("");
				$icon.appendTo($selection);
			}

			// Display name
			$displayName = $JQry(document.createElement("span"));
			$displayName.text(" " + displayName);
			$displayName.appendTo($selection);
			
			return $selection;
		};
		
		
		// Force width to 100%
		$element.css({
			width: "100%"
		});
		
		
		$element.select2(options);
		
		
		// Display collapsed buttons
		$element.on("select2:opening", function(event) {
			var $form = $element.closest("form"),
				$collapse = $form.find(".collapse");
			
			if (!$collapse.hasClass("in")) {
				$collapse.collapse("show");
			}
		});
	});
	
	
	$JQry(".workspace-acl-management select").change(function(event) {
		var $target = $JQry(event.target),
			$fieldset = $target.closest("fieldset"),
			$row = $fieldset.closest(".row"),
			$updated = $row.find("input[type=hidden][name$=updated]"),
			$form = $target.closest("form"),
			$collapse = $form.find(".collapse");
		
		$updated.val(true);
		
		if (!$collapse.hasClass("in")) {
			$collapse.collapse("show");
		}
	});
	
	
	$JQry(".workspace-acl-management button.delete").click(function(event) {
		var $target = $JQry(event.target),
			$fieldset = $target.closest("fieldset"),
			$row = $fieldset.closest(".row"),
			$deleted = $row.find("input[type=hidden][name$=deleted]"),
			$form = $target.closest("form"),
			$collapse = $form.find(".collapse");
		
		$deleted.val(true);
		$fieldset.prop("disabled", true);
		
		if (!$collapse.hasClass("in")) {
			$collapse.collapse("show");
		}
	});
	
	
	$JQry(".workspace-acl-management button[type=reset]").click(function(event) {
		var $target = $JQry(event.target),
			$form = $target.closest("form");
		
		$form.find("fieldset[disabled]").prop("disabled", false);
		$form.find("input[type=hidden][value=true]").val(false);
	});
	
});
