$JQry(function() {
	
	$JQry(".workspace-local-group-management select.select2").each(function(index, element) {
		var $element = $JQry(element);
 		var options = {
				theme : "bootstrap"
			};
		
		
		// Result template
		options["templateResult"] = function(params) {
			var $element = $JQry(params.element);
			var displayName = $element.data("display-name");
			var avatar = $element.data("avatar");
			var extra = $element.data("extra");
			
			var $result = $JQry(document.createElement("div"));
			
			if (params.loading) {
				$result.text(params.text);
			} else {
				$result.addClass("person");
				
				// Person avatar
				var $personAvatar = $JQry(document.createElement("div"));
				$personAvatar.addClass("person-avatar");
				$personAvatar.appendTo($result);
				
				if (avatar) {
					// Avatar
					var $avatar = $JQry(document.createElement("img"));
					$avatar.attr("src", avatar);
					$avatar.attr("alt", "");
					$avatar.appendTo($personAvatar);
				} else {
					// Icon
					var $icon = $JQry(document.createElement("i"));
					$icon.addClass("glyphicons glyphicons-user");
					$icon.text("");
					$icon.append($personAvatar);
				}
				
				// Person title
				var $personTitle = $JQry(document.createElement("div"));
				$personTitle.addClass("person-title");
				$personTitle.text(displayName);
				$personTitle.appendTo($result);
				
				// Person extra
				if (extra) {
					var $personExtra = $JQry(document.createElement("div"));
					$personExtra.addClass("person-extra");
					$personExtra.text(extra);
					$personExtra.appendTo($result);
				}
			}

			return $result;
		};
		
		
		// Selection template
		options["templateSelection"] = function(params) {
			var $element = $JQry(params.element);
			var displayName = $element.data("display-name");
			
			// Selection
			var $selection = $JQry(document.createElement("div"));
			$selection.addClass("person");
			
			// Avatar URL
			var avatar;
			if (params.avatar === undefined) {
				avatar = $element.data("avatar");
			} else {
				avatar = params.avatar;
			}
			
			if (avatar) {
				// Person avatar
				var $personAvatar = $JQry(document.createElement("div"));
				$personAvatar.addClass("person-avatar");
				$personAvatar.appendTo($selection);
				
				// Avatar
				var $avatar = $JQry(document.createElement("img"));
				$avatar.attr("src", avatar);
				$avatar.attr("alt", "");
				$avatar.appendTo($personAvatar);
			}
			
			// Person title
			var $personTitle = $JQry(document.createElement("div"));
			$personTitle.addClass("person-title");
			$personTitle.text(displayName);
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
	});
	
});
