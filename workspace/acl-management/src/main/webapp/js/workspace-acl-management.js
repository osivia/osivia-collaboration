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
				
				
				$result.addClass("person");
				
				// Person avatar
				$personAvatar = $JQry(document.createElement("div"));
				$personAvatar.addClass("person-avatar");
				$personAvatar.appendTo($result);
				
				if (type === "GROUP") {
					$icon = $JQry(document.createElement("i"));
					$icon.addClass("glyphicons glyphicons-group")
					$icon.text("");
					$icon.appendTo($personAvatar);
				} else if (avatar) {
					$avatar = $JQry(document.createElement("img"));
					$avatar.attr("src", avatar);
					$avatar.attr("alt", "");
					$avatar.appendTo($personAvatar);
				} else {
					$icon = $JQry(document.createElement("i"));
					$icon.addClass("glyphicons glyphicons-user")
					$icon.text("");
					$icon.appendTo($personAvatar);
				}

				// Person title
				$personName = $JQry(document.createElement("div"));
				$personName.addClass("person-title");
				$personName.text(displayName);
				$personName.appendTo($result);
				
				// Person extra
				if (extra) {
					$personExtra = $JQry(document.createElement("div"));
					$personExtra.addClass("person-extra");
					$personExtra.text(extra);
					$personExtra.appendTo($result);
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
			$selection.addClass("person");
			
			// Person avatar
			$personAvatar = $JQry(document.createElement("div"));
			$personAvatar.addClass("person-avatar");
			$personAvatar.appendTo($selection);
			
			if (type === "GROUP") {
				$icon = $JQry(document.createElement("i"));
				$icon.addClass("glyphicons glyphicons-group")
				$icon.text("");
				$icon.appendTo($personAvatar);
			} else if (avatar) {
				$avatar = $JQry(document.createElement("img"));
				$avatar.attr("src", avatar);
				$avatar.attr("alt", "");
				$avatar.appendTo($personAvatar);
			} else {
				$icon = $JQry(document.createElement("i"));
				$icon.addClass("glyphicons glyphicons-user")
				$icon.text("");
				$icon.appendTo($personAvatar);
			}

			// Person title
			$personName = $JQry(document.createElement("div"));
			$personName.addClass("person-title");
			$personName.text(displayName);
			$personName.appendTo($selection);
			
			return $selection;
		};
		
		
		// Internationalization
		options["language"] = {};
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
		

		// Display collapsed buttons
		$element.on("select2:opening", function(event) {
			var $form = $element.closest("form"),
				$collapse = $form.find(".collapse");
			
			if (!$collapse.hasClass("in")) {
				$collapse.collapse("show");
			}
		});
		
		
		// Update fancybox
		parent.$JQry.fancybox.update();
	});
	
	
	$JQry(".workspace-acl-management input[type=checkbox][name=publicEntry]").change(function(event) {
		var $target = $JQry(event.target),
			$formGroup = $target.closest(".form-group"),
			$submit = $formGroup.find("input[type=submit]");
		
		$submit.click();
	});
	
	
	$JQry(".workspace-acl-management input[type=radio][name=inherited]").change(function(event) {
		var $target = $JQry(event.target),
			$formGroup = $target.closest(".form-group"),
			$submit = $formGroup.find("input[type=submit]");
		
		$submit.click();
	});
	
	
	$JQry(".workspace-acl-management select").change(function(event) {
		var $target = $JQry(event.target),
			$fieldset = $target.closest("fieldset"),
			$row = $fieldset.closest(".table-row"),
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
			$row = $fieldset.closest(".table-row"),
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
