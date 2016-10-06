$JQry(function() {
	
	$JQry(".workspace-member-management select.select2").each(function(index, element) {
		var $element = $JQry(element),
			url = $element.data("url"),
 			options = {
				minimumInputLength: 3,
				theme: "bootstrap"
			};
		
		// Ajax
		options["ajax"] = {
			url: url,
			dataType: "json",
			delay: 250,
			data: function(params) {
				return {
					filter: params.term,
				};
			},
			processResults: function(data, params) {				
				return {
					results: data
				};
			},
			cache: true
		};
		
		
		// Tokenizer
		options["tokenizer"] = function(input, selection, callback) {
			var term = input.term;

			if ((term.indexOf(",") !== -1) || (term.indexOf(";") !== -1)) {
				if (term.length > 3) {
					$JQry.getJSON(url, {
						filter: term,
						tokenizer: true
					}, function(data, status, xhr) {
						$JQry.each(data, function(key, value) {
							callback(value);
						});
					});
				}
				
				return {
					term: ""
				};
			} else {
				return input;
			}
		};
		
		
		// Result template
		options["templateResult"] = function(params) {
			$result = $JQry(document.createElement("div"));
			
			if (params.loading) {
				$result.text(params.text);
			} else {
				$result.addClass("person");
				
				// Person avatar
				$personAvatar = $JQry(document.createElement("div"));
				$personAvatar.addClass("person-avatar");
				$personAvatar.appendTo($result);
				
				if (params.create) {
					// Icon
					$icon = $JQry(document.createElement("i"));
					$icon.addClass("glyphicons glyphicons-user-add");
					$icon.text("");
					$icon.appendTo($personAvatar);
				} else if (params.avatar !== undefined) {
					// Avatar
					$avatar = $JQry(document.createElement("img"));
					$avatar.attr("src", params.avatar);
					$avatar.attr("alt", "");
					$avatar.appendTo($personAvatar);
				}
				
				// Person title
				$personTitle = $JQry(document.createElement("div"));
				$personTitle.addClass("person-title");
				$personTitle.text(params.displayName);
				$personTitle.appendTo($result);
				
				// Person extra
				if (params.extra) {
					$personExtra = $JQry(document.createElement("div"));
					$personExtra.addClass("person-extra");
					$personExtra.text(params.extra);
					$personExtra.appendTo($result);
				}
			}

			return $result;
		};
		
		
		// Selection template
		options["templateSelection"] = function(params) {
			var avatarUrl;
			
			// Selection
			$selection = $JQry(document.createElement("div"));
			$selection.addClass("person");
			
			// Avatar URL
			if (params.avatar === undefined) {
				avatarUrl = $JQry(params.element).data("avatar");
			} else {
				avatarUrl = params.avatar;
			}
			
			if (avatarUrl !== undefined) {
				// Person avatar
				$personAvatar = $JQry(document.createElement("div"));
				$personAvatar.addClass("person-avatar");
				$personAvatar.appendTo($selection);
				
				// Avatar
				$avatar = $JQry(document.createElement("img"));
				$avatar.attr("src", avatarUrl);
				$avatar.attr("alt", "");
				$avatar.appendTo($personAvatar);
			}
			
			// Person title
			$personTitle = $JQry(document.createElement("div"));
			$personTitle.addClass("person-title");
			if (params.create) {
				$personTitle.text(params.id);
			} else if (params.displayName === undefined) {
				$personTitle.text(params.text);
			} else {
				$personTitle.text(params.displayName);
			}
			$personTitle.appendTo($selection);
			
			return $selection;
		};

		
		// Internationalization
		options["language"] = {};
		if ($element.data("input-too-short") !== undefined) {
			options["language"]["inputTooShort"] = function() {
				return $element.data("input-too-short");
			}
		}
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
		
		
		// Display collapsed buttons
		$element.on("select2:opening", function(event) {
			var $form = $element.closest("form"),
				$collapse = $form.find(".collapse");
			
			if (!$collapse.hasClass("in")) {
				$collapse.collapse("show");
			}
		});
	});
	
	
	$JQry(".workspace-member-management select").change(function(event) {
		var $target = $JQry(event.target),
			$row = $target.closest(".table-row"),
			$edited = $row.find("input[type=hidden][id$='.edited']"),
			$form = $row.closest("form"),
			$collapse = $form.find(".collapse");
		
		$edited.val(true);
		
		if (!$collapse.hasClass("in")) {
			$collapse.collapse("show");
		}
	});

	
	$JQry(".workspace-member-management button.delete").click(function(event) {
		var $target = $JQry(event.target),
			$fieldset = $target.closest("fieldset"),
			$row = $fieldset.closest(".table-row"),
			$deleted = $row.find("input[type=hidden][id$='.deleted']"),
			$buttons = $row.find("button"),
			$form = $fieldset.closest("form"),
			$collapse = $form.find(".collapse");
		
		$deleted.val(true);
		$buttons.hide();
		$fieldset.prop("disabled", true);
		
		if (!$collapse.hasClass("in")) {
			$collapse.collapse("show");
		}
	});
	
	
	$JQry(".workspace-member-management button.accept").click(function(event) {
		var $target = $JQry(event.target),
			$row = $target.closest(".table-row"),
			$accepted = $row.find("input[type=hidden][id$='.accepted']"),
			$buttons = $row.find("button"),
			$acceptedMessage = $row.find(".accepted-message"),
			$form = $row.closest("form"),
			$collapse = $form.find(".collapse");
		
		$accepted.val(true);
		
		$buttons.hide();
		$acceptedMessage.removeClass("hidden");
		$acceptedMessage.show();
		
		if (!$collapse.hasClass("in")) {
			$collapse.collapse("show");
		}
	});
	
	
	$JQry(".workspace-member-management button[type=reset]").click(function(event) {
		var $target = $JQry(event.target),
			$form = $target.closest("form");
		
		$form.find("fieldset[disabled]").prop("disabled", false);
		$form.find("input[type=hidden][value=true]").val(false);
		$form.find("select.select2").val(null).trigger("change");
		$form.find("button").show();
		$form.find(".accepted-message").hide();
	});
	
});
